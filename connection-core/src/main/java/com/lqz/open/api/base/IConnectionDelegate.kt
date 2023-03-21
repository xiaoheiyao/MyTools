package com.lqz.open.api.base

import com.lqz.open.api.model.ConnectType
import com.lqz.open.connection.ConnectionDelegateListener

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 16:19
 *    desc   : 连接委托类的接口，fixme 这个可以是否拆分成两个，一个被动接收数据用，一个主动调用方法用
 */
interface IConnectionDelegate {

    /**
     * 添加监听事件
     */
    fun addConnectionListener(connectionListener: ConnectionDelegateListener)

    /**
     * 删除监听事件
     */
    fun removeConnectionListener(connectionListener: ConnectionDelegateListener)

    /**
     * 发送数据
     */
    fun sendData(data: ByteArray)

    /**
     * 是否连接
     */
    fun hasConnected(): Boolean

    /**
     * 连接 PS:connect --》openConnection
     */
    fun connect()

    /**
     * 断开连接 PS：disconnect-->closeConnect
     */
    fun disconnect()

    /**
     * 连接类型
     */
    fun getConnectType(): ConnectType

//    /**
//     * 是否可以重连 true：可以重连 false：不可以重连
//     */
//    fun canReconnect(): Boolean
}