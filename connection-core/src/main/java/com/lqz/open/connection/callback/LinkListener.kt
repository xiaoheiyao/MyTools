package com.lqz.open.connection.callback

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 17:00
 *    desc   :
 */
interface LinkListener<T> {

    /**
     * 连接成功
     * @param 响应
     */
    fun onSuccess(response: T)

    /**
     * 连接失败
     * @param errorCode 失败编号
     */
    fun onFailed(errorCode: Int)

    /**
     * 连接失败
     */
    fun onTimeout()
}