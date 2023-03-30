package com.lqz.commonsdk.utils

import android.app.ActivityManager
import android.content.Context

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/3/29 18:11
 *    desc   :
 */
class AndroidUtil {

    /**
     * 判断应用是否在前台
     *
     * @param context
     * @return
     */
    fun isRunningForeground(context: Context): Boolean {
        val packageName = AppInfoUtil.getPackageName(context)
        val topActivityClassName: String? = getTopActivityName(context)
        println("packageName=$packageName,topActivityClassName=$topActivityClassName")
        return if (packageName != null && topActivityClassName != null
            && topActivityClassName.startsWith(packageName)
        ) {
            println("---> isRunningForeGround")
            true
        } else {
            println("---> isRunningBackGround")
            false
        }
    }


    /**
     * 获取栈顶的activity名称
     */
    fun getTopActivityName(context: Context): String? {
        var topActivityClassName: String? = null
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfos = activityManager.getRunningTasks(1)
        if (runningTaskInfos != null) {
            val f = runningTaskInfos[0].topActivity
            topActivityClassName = f!!.className
        }
        return topActivityClassName
    }
}