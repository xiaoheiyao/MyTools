package com.lqz.commonsdk.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/13 17:15
 *    desc   : 账号信息校验
 */
object AccountUtil {

    /**
     * 是否是手机号
     */
    fun isPhone(phone: String): Boolean {
        if (!TextUtils.isEmpty(phone)) {
            val pattern = Pattern.compile("^1[0-9]{10}")
            val matcher = pattern.matcher(phone)
            return matcher.matches()
        }
        return false
    }

    /**
     * 是否是email格式
     */
    fun isEmail(email: String?): Boolean {
        if (email == null || email.trim().length == 0) {
            return false
        }
        return Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email)
            .matches()
    }
}