package com.lqz.open.connection

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 16:20
 *    desc   : 连接状态监听器
 */
interface ConnectionDelegateListener {

    /**
     * 准备连接（正在连接中！！！）
     */
    fun notifyConnectionLoading()

    /**
     * 连接成功
     */
    fun notifyConnectSuccess()

    /**
     * 连接失败
     */
    fun notifyConnectFailed()

    /**
     * 断开连接
     */
    fun notifyDisconnected()

    /**
     * 接收数据
     */
    fun notifyDataReceive(data: ByteArray)
}