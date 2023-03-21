package com.lqz.commonsdk.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/3/18 15:18
 *    desc   : 用于读取Android系统的一些参数
 */
object PropUtil {

    /**
     * 读取prop
     * @param prop 需要读取的字段名称
     */
    public fun readProp(prop: String): Int {
        try {
            val process = Runtime.getRuntime().exec("getprop $prop")
            val ir = InputStreamReader(process.inputStream)
            val input = BufferedReader(ir)
            return input.readLine().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 写入prop
     * @param prop 写入的字段名称
     * @param value 需要写入的值
     */
    public fun writeProp(prop: String, value: String) {
        try {
            val process = Runtime.getRuntime().exec("setprop $prop $value")
            val ir = InputStreamReader(process.inputStream)
            val input = BufferedReader(ir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}