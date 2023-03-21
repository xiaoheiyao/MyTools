package com.lqz.open.utils

import kotlin.experimental.and

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 9:43
 *    desc   :
 */
object BytesUtils {

    /**
     * 将bytes数组转换成16进制String类型：如10 --> 0a
     */
    fun bytesToHexString(b: ByteArray?): String? {
        if (b == null) {
            return null
        }
        val result = StringBuffer()
        for (i in b.indices) {
            var str = Integer.toHexString((b[i] and 0xFF.toByte()).toInt())
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
}