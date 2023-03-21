package com.lqz.commonsdk.log

import android.content.Context
import java.io.File

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 13:53
 *    desc   :
 */
object FileUtils {
    const val LOG = "log"

    fun getLogPath(context: Context): String {
        return context.getExternalFilesDir(LOG)?.absolutePath ?: ""
    }

    fun getLogCachePath(context: Context): String {
        return File(context.externalCacheDir, LOG).absolutePath
    }
}