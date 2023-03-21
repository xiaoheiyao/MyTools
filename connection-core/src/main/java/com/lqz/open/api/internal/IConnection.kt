package com.lqz.open.api.internal

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 13:54
 *    desc   : 连接的接口类
 */
interface IConnection {

    /**
     * 连接
     */
    fun connect()

    /**
     * 断开连接
     */
    fun disconnect()

    /**
     * 是否连接
     */
    fun hasConnected():Boolean
}