package com.lqz.open.api.base

import com.lqz.open.connection.ConnectionDelegateListener
import com.lqz.open.utils.BytesUtils
import com.lqz.open.utils.CoreLog
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 16:38
 *    desc   : 连接委托抽象类
 */
abstract class ConnectionDelegate : IConnectionDelegate {

    //最大字节缓冲数
    private val MAX_BUFFER: Int = 1024 * 16

    //连接状态监听器队列
    protected val connectionListenerQueue: ConcurrentLinkedQueue<ConnectionDelegateListener> =
        ConcurrentLinkedQueue()


    /*是否进行数据通信标志*/
    private var isInitialized = false

    //连接状态:0:断开，1：正在连接,2:已连接
    private val connectStatus = AtomicInteger(0)

    public fun getConnectStatus(): Int {
        return connectStatus.get()
    }

    //后台发送数据线程
    private var sendThread: Thread? = null

    //后台接收数据线程
    private var readThread: Thread? = null

    //将要发送的数据包队列
    private val packetsToSend = LinkedBlockingQueue<ByteArray>()


    //连接任务
    private val connectTask = Runnable {
        if (connectStatus.get() == 0) { //如果当前是断开连接状态，就进行重连
            notifyConnectionLoading() //通知监听事件正在连接中
            connectStatus.set(1) //设置标志位为正在连接
            CoreLog.i("connecting")
            openConnection(object : IConnectStatus {
                override fun connectSuccess() {
                    //连接成功
                    notifyConnectionSuccess()
                }

                override fun connectFail() {
                    //连接失败
                    notifyConnectionFail()
                }

            })
        }
    }

    //发送任务对象
    private val sendTask = Runnable {

        while (isInitialized) {
            try {
                val buffer: ByteArray = packetsToSend.take()

                writeData(buffer)
                CoreLog.d("sendTask:${BytesUtils.bytesToHexString(buffer)}")
//                if (sendDataDelayTime > 0) { //fixme 发送延时功能，暂时不需要！！！
//                    Thread.sleep(sendDataDelayTime)
//                }
            } catch (e: IOException) {
                e.printStackTrace()
                CoreLog.e("sendTask:IOException = ${e.message}")
//                notifyConnectionDisconnect() //fixme 写入失败，调用断开连接？
            } catch (e: Exception) {
                e.printStackTrace()
                CoreLog.e("sendTask:Exception = ${e.message}")
            }
        }
    }

    //接收数据任务
    private val readTask = Runnable {

        val buffer = ByteArray(MAX_BUFFER)
        while (isInitialized) {
            try {
                val dataLength: Int = readData(buffer)//接收数据

                if (dataLength >= 1) {
                    val tempBuffer = Arrays.copyOfRange(buffer, 0, dataLength)
                    CoreLog.d("readTask:${BytesUtils.bytesToHexString(tempBuffer)}")
                    onReceiveData(tempBuffer) //将数据转发出去
                }
            } catch (e: IOException) {
                e.printStackTrace()
//                notifyConnectionDisconnect() //fixme 读取失败，调用断开连接
                CoreLog.e("readTask:IOException = ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                CoreLog.e("readTask:Exception = ${e.message}")
            }
        }
    }

    override fun addConnectionListener(connectionListener: ConnectionDelegateListener) {
        if (!connectionListenerQueue.contains(connectionListener)) {
            connectionListenerQueue.add(connectionListener)
        }
    }

    override fun removeConnectionListener(connectionListener: ConnectionDelegateListener) {
        if (connectionListenerQueue.contains(connectionListener)) {
            connectionListenerQueue.remove(connectionListener)
        }
    }

    override fun sendData(data: ByteArray) {
        //插入数据到发送队列中
        CoreLog.d("sendData:${BytesUtils.bytesToHexString(data)}")
        packetsToSend.offer(data)
    }

    override fun hasConnected(): Boolean {
        return connectStatus.get() == 2
    }

    /**
     * 调用连接方法
     */
    override fun connect() {
        CoreLog.i("connect connectStatus:${connectStatus.get()}")
        if (connectStatus.get() == 0) {
            //启动连接线程，连接飞控设备
            connectTask.run()
        }
    }

    /**
     * 调用断开连接方法
     */
    override fun disconnect() {
        CoreLog.i("disconnect")
        closeConnection() //调用断开连接
        notifyConnectionDisconnect() //发送断开连接消息
    }


    /**
     * 通知正在连接中
     */
    open fun notifyConnectionLoading() {
        if (connectionListenerQueue.isNotEmpty()) {
            for (listener in connectionListenerQueue) {
                listener.notifyConnectionLoading()
            }
        }
    }

    /**
     * 通知连接成功
     */
    fun notifyConnectionSuccess() {
        onConnected() ////先改变当前状态，再向监听事件传递结果
        if (connectionListenerQueue.isNotEmpty()) {
            for (listener in connectionListenerQueue) {
                listener.notifyConnectSuccess()
            }
        }
    }

    /**
     * 通知连接失败
     */
    private fun notifyConnectionFail() {
        onDisconnected() //先改变当前状态，再向监听事件传递结果
        if (connectionListenerQueue.isNotEmpty()) {
            for (listener in connectionListenerQueue) {
                listener.notifyConnectFailed()
            }
        }
    }

    /**
     * 通知断开连接
     */
    open fun notifyConnectionDisconnect() {
        onDisconnected() //先改变当前状态，再向监听事件传递结果
        if (connectionListenerQueue.isNotEmpty()) {
            for (listener in connectionListenerQueue) {
                listener.notifyDisconnected()
            }
        }
    }

    /**
     * 通知接收的数据
     */
    private fun onReceiveData(bytes: ByteArray) {
        if (connectionListenerQueue.isNotEmpty()) {
            for (listener in connectionListenerQueue) {
                listener.notifyDataReceive(bytes)
            }
        }
    }

    private fun setConnected(hasConnected: Boolean) {
        CoreLog.i("connect status:$hasConnected")
        if (hasConnected) {
            connectStatus.set(2)
        } else {
            connectStatus.set(0)
        }
    }

    /**
     * 连接成功
     */
    private fun onConnected() {
        if (hasConnected()) { //如果是已经连接状态，直接退出
            return
        }
        CoreLog.i("connected")
        setConnected(true)
        isInitialized = true

        //初始化后台发送线程

        //初始化后台发送线程
        sendThread = Thread(sendTask)
        sendThread!!.start()

        readThread = Thread(readTask)
        readThread!!.start()

    }

    /**
     * 断开连接
     */
    private fun onDisconnected() {
        CoreLog.i("disconnected")
        setConnected(false)
        isInitialized = false
        if (sendThread != null) {
            sendThread?.interrupt()
            sendThread = null
        }
        if (readThread != null) {
            readThread?.interrupt()
            readThread = null
        }
        //清空所有监听事件！！！
        connectionListenerQueue.clear()
    }

    @Throws(IOException::class)
    abstract fun writeData(data: ByteArray)

    /**
     * 读取数据
     */
    @Throws(IOException::class)
    abstract fun readData(data: ByteArray): Int

    /**
     * 打开连接
     */
    abstract fun openConnection(listener: IConnectStatus)

    /**
     * 关闭连接
     */
    abstract fun closeConnection()

    /**
     * 连接状态接口
     */
    interface IConnectStatus {
        /**
         * 连接成功
         */
        fun connectSuccess()

        /**
         * 连接失败，暂时不添加失败原因
         */
        fun connectFail()

    }
}