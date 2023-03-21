package com.lqz.commonsdk.log

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 13:19
 *    desc   : 日志打印接口
 */
interface ILogger {
    fun v(tag: String, exception: Throwable)
    fun v(tag: String, message: String, vararg values: Any)
    fun d(tag: String, exception: Throwable)
    fun d(tag: String, message: String, vararg values: Any)
    fun i(tag: String, exception: Throwable)
    fun i(tag: String, message: String, vararg values: Any)
    fun w(tag: String, exception: Throwable)
    fun w(tag: String, message: String, vararg values: Any)
    fun e(tag: String, exception: Throwable)
    fun e(tag: String, message: String, vararg values: Any)

    /**
     * 快速存储日志，（日志会先存在缓存中，如果想快速更新到文件，走这个方法）
     */
    fun dump()
}