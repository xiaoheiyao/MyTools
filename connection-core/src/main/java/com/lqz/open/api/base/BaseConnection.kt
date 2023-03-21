package com.lqz.open.api.base

import com.lqz.message.ILinkMessage
import com.lqz.message.LinkPacket
import com.lqz.open.api.internal.IConnection
import com.lqz.open.connection.ConnectionDelegateListener
import com.lqz.open.connection.callback.PacketListener
import java.util.concurrent.Executors

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 13:52
 *    desc   : 设备连接的基类
 *          将连接状态和委托类统一管理，
 *          后续还会将重连，超时等添加进来
 *          数据解析也会考虑放到这个类中来做！！！
 */
abstract class BaseConnection(
    private var delegate: ConnectionDelegate?
) : IConnection, ConnectionDelegateListener {

    //reconnect执行服务
    protected val reconnectExecutor = Executors.newSingleThreadScheduledExecutor()

    private var connectionListener: IConnectionListener? = null

    /**
     * 获得对应的委托类
     */
    open fun getDelegate(): IConnectionDelegate? {
        return delegate
    }

    /**
     * 获得连接监听事件
     */
    open fun getConnectionListener(): IConnectionListener? {
        return connectionListener
    }

    /**
     * 设置连接监听事件
     */
    open fun setConnectionListener(connectionListener: IConnectionListener?) {
        this.connectionListener = connectionListener
    }

    /**
     * 发送消息 todo 先简写一下，发送任何消息都行
     * @param message 需要发送的消息包
     * @param packetListener 消息包的监听事件
     */
    fun sendMessage(message: ILinkMessage?, packetListener: PacketListener?) {
        if (message == null) {
            return
        }
        val packet: LinkPacket? = message.pack()
        if (packet == null) {
            return
        }
        packetListener?.setMessage(message)

        val packetData = packet.encodePacket()

        sendDataByDelegate(packetData)

    }

    /**
     * 发送数据
     */
    fun sendMessage(data: ByteArray) {
        sendDataByDelegate(data)
    }

    /**
     * 是否正在连接中。。。
     */
    private fun isConnecting(): Boolean {
        return delegate?.getConnectStatus() == 1
    }


    /**
     * 将数据发送出去
     * @param 需要发送的数据
     */
    private fun sendDataByDelegate(data: ByteArray) {
        delegate?.sendData(data)
    }

    /**
     * 判断是否连接
     */
    override fun hasConnected(): Boolean {
        return delegate?.hasConnected() == true
    }

    /**
     * 连接
     */
    override fun connect() {
        reconnectExecutor.execute {
            if (isConnecting()) { //正在连接，直接退出
                return@execute
            }
            if (delegate == null) {
                return@execute
            }

            delegate?.addConnectionListener(this@BaseConnection)
            if (delegate?.hasConnected() == true) { //如果已经连接，直接返回连接成功
                onConnectWithFccSuccess()
            } else {
                delegate?.connect() // 如果没有连接，就调用连接方法
            }
        }
    }

    override fun notifyConnectionLoading() {
        connectionListener?.onConnecting()
    }

    override fun notifyConnectSuccess() {
        onConnectWithFccSuccess()
    }

    override fun notifyConnectFailed() {
        onConnectFailed() //连接失败
    }

    /**
     * 断开连接
     */
    override fun notifyDisconnected() {
        onDisConnected()
    }


    /**
     * 连接成功
     */
    private fun onConnectWithFccSuccess() {
        connectionListener?.onConnected()
    }

    private fun onConnectFailed() {
        //todo 连接失败，暂时不做任何处理！！！
        connectionListener?.onNotConnected() //发送一个没有连接的状态
    }

    private fun onDisConnected() {
        delegate?.removeConnectionListener(this)
        connectionListener?.onDisconnect()
    }

    /**
     * 关闭连接
     */
    open fun close() {
        delegate?.disconnect()
    }
}