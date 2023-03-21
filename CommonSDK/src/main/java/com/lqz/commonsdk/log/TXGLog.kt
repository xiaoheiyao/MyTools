package com.lqz.commonsdk.log

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 14:56
 *    desc   : Log日志对外的类，调用这个类打印日志
 */
object TXGLog {
    private var defaultLogger: ILogger? = null

    @JvmStatic
    fun setLogger(logger: ILogger) {
        defaultLogger = logger
    }

    @JvmStatic
    fun i(tag: String, message: String, vararg values: Any) {
        defaultLogger?.i(tag, message, *values)
    }

    @JvmStatic
    fun i(tag: String, exp: Throwable) {
        defaultLogger?.i(tag, exp)
    }

    @JvmStatic
    fun e(tag: String, message: String, vararg values: Any) {
        defaultLogger?.e(tag, message, *values)
    }

    @JvmStatic
    fun e(tag: String, exp: Throwable) {
        defaultLogger?.e(tag, exp)
    }

    @JvmStatic
    fun w(tag: String, message: String, vararg values: Any) {
        defaultLogger?.w(tag, message, *values)
    }

    @JvmStatic
    fun w(tag: String, exp: Throwable) {
        defaultLogger?.w(tag, exp)
    }

    @JvmStatic
    fun v(tag: String, message: String, vararg values: Any) {
        defaultLogger?.v(tag, message, *values)
    }

    @JvmStatic
    fun v(tag: String, exp: Throwable) {
        defaultLogger?.v(tag, exp)
    }

    @JvmStatic
    fun d(tag: String, message: String, vararg values: Any) {
        defaultLogger?.d(tag, message, *values)
    }

    @JvmStatic
    fun d(tag: String, exp: Throwable) {
        defaultLogger?.d(tag, exp)
    }

    @JvmStatic
    fun dump() {
        defaultLogger?.dump()
    }

}