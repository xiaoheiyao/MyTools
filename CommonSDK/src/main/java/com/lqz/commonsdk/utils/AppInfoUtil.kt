package com.lqz.commonsdk.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/13 15:40
 *    desc   :
 */
object AppInfoUtil {

    /**
     * 获得当前version code
     */
    fun getVersionCode(context: Context): Long {
        var versionCode: Long = 0L
        try {
            val packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.longVersionCode
            } else {
                versionCode = packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     * 获取app版本名称
     */
    fun getAppVersionName(ctx: Context): String? {
        var versionName: String? = null
        try {
            val packageInfo = ctx.applicationContext
                .packageManager
                .getPackageInfo(ctx.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * 获得当前app包名
     */
    fun getPackageName(context: Context): String? {
        return context.packageName
//        var packageName: String? = null
//        try {
//            val packageInfo = context.applicationContext
//                .packageManager
//                .getPackageInfo(context.packageName, 0)
//            packageName = packageInfo.packageName
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return packageName
    }

    /**
     * 获得当前app名称
     */
    fun getAppName(context: Context): String? {
        var appName: String? = null
        try {
            val packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(context.packageName, 0)
            val applicationInfo = packageInfo.applicationInfo
            appName = context.applicationContext.packageManager.getApplicationLabel(applicationInfo)
                .toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appName
    }

}