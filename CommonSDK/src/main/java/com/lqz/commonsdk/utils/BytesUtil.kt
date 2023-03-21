package com.lqz.commonsdk.utils

import kotlin.experimental.and

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/21 13:44
 *    desc   : byte工具类
 */
object BytesUtil {

    /**
     * 将byte数组转化成Ascii对应的String
     */
    fun bytesToAsciiString(bytes: ByteArray): String {
        return String(bytes)
    }

    /**
     * 将字符串转化为bytes
     */
    fun stringToBytes(str: String): ByteArray {
        return str.toByteArray()
    }

    /**
     * 将bytes转化成10进制的String输出
     */
    fun bytesToString(bytes: ByteArray): String {
        return bytes.contentToString()
    }

    /**
     * 以16进制字符串的形式返回bytes数组
     */
    fun bytesToHexString(bytes: ByteArray?): String? {
        if (bytes == null) {
            return null
        }
        val result = StringBuffer()

        for (i in bytes.indices) {
            var str = Integer.toHexString((bytes[i] and 0xFF.toByte()).toInt())
            if (str.length == 1) {
                result.append("0$str ")
            } else if (str.length > 2) {
                str = str.substring(str.length - 2, str.length)
                result.append("$str ")
            } else {
                result.append("$str ")
            }

        }
        return result.toString()
    }

    /**
     * 16进制字符串转化成byte数组
     * todo 注意：此方法只能转化标准的010203这种连续的，两位为一个byte位的字符串
     */
    fun hexStringToByte(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] =
                ((hex[i].digitToIntOrNull(16)
                    ?: (-1 shl 4)) + hex[i + 1].digitToIntOrNull(16)!!).toByte()
            i += 2
        }
        return data
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    fun intToLowBytes(value: Int): ByteArray {
        val src = ByteArray(4)
        src[3] = (value shr 24 and 0xFF).toByte()
        src[2] = (value shr 16 and 0xFF).toByte()
        src[1] = (value shr 8 and 0xFF).toByte()
        src[0] = (value and 0xFF).toByte()
        return src
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    fun intToHighBytes(i: Int): ByteArray {
        val result = ByteArray(4)
        result[0] = (i shr 24 and 0xFF).toByte()
        result[1] = (i shr 16 and 0xFF).toByte()
        result[2] = (i shr 8 and 0xFF).toByte()
        result[3] = (i and 0xFF).toByte()
        return result
    }
}