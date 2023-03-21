package com.lqz.open.android

import android.net.LocalSocket
import android.net.LocalSocketAddress
import com.lqz.open.api.base.ConnectionDelegate
import com.lqz.open.api.model.ConnectType
import com.lqz.open.utils.AndroidLog
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/3/18 16:38
 *    desc   : 串口三通道连接
 */
class Uart3Connection : ConnectionDelegate() {

    private var localSocket: LocalSocket? = null

    private var output: OutputStream? = null
    private var input: InputStream? = null

    override fun writeData(data: ByteArray) {
        try {
            output?.write(data)
        } catch (e: IOException) {
            AndroidLog.e("Uart3Connection writeData failed:${e.message}")
            throw e
        }
    }

    override fun readData(data: ByteArray): Int {
        var length = 0
        try {
            length = input?.read(data) ?: 0

        } catch (e: IOException) {
            e.printStackTrace()
            AndroidLog.e("Uart3Connection readData failed:${e.message}")
            throw e
        }
        return length
    }

    override fun openConnection(listener: IConnectStatus) {
        try {
            localSocket = LocalSocket(LocalSocket.SOCKET_DGRAM)
            //            //与Uart3 通讯
            localSocket?.bind(LocalSocketAddress("uart3app"))
            localSocket?.connect(LocalSocketAddress("uart3endpoint"))
            output = localSocket?.outputStream
            input = localSocket?.inputStream
            AndroidLog.i("Connect with the uart3 success.")
            listener.connectSuccess()
        } catch (e: IOException) {
            e.printStackTrace()
            AndroidLog.e("Connect with the uart3 failed." + e.message)
            listener.connectFail()
        }
    }

    override fun closeConnection() {
        AndroidLog.i("closeConnection uart3")
        try {
            localSocket?.shutdownOutput()
            localSocket?.shutdownInput()
            localSocket?.close()
            localSocket = null
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            input?.close()
            input = null
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            output?.close()
            output = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getConnectType(): ConnectType {
        return ConnectType.TYPE_UART3
    }
}