package com.lqz.message

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 16:49
 *    desc   :
 */
interface LinkPacket {
    /**
     * 获取当前包序列（序号）
     */
    fun getSeq(): String

    /**
     * 数据包
     */
    fun encodePacket(): ByteArray
}