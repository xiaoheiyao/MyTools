package com.lqz.open.api.base

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 15:31
 *    desc   : 连接监听类
 */
interface IConnectionListener {

    /**
     * 正在连接
     */
    fun onConnecting()

    /**
     * 已连接
     */
    fun onConnected()

    /**
     * 未连接
     */
    fun onNotConnected()

    /**
     * 设备通信数据
     */
    fun onReceiverTelemetryData(data: ByteArray)

    /**
     * 断开连接
     */
    fun onDisconnect()
}