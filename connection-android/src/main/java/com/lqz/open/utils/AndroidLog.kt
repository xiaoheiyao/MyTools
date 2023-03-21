package com.lqz.open.utils

import android.util.Log

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 9:15
 *    desc   : 日志，该模块下的日志都直接调用这里的方法，方便以后修改日志模块
 */
object AndroidLog {

    val TAG: String = "connection-android"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun e(message: String) {
        Log.e(TAG, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}