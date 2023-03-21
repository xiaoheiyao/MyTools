package com.lqz.commonsdk.application

import android.content.res.Configuration

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 15:12
 *    desc   : application 监听接口
 */
interface IApplication {
    /**
     * 创建
     */
    fun onCreate()

    /**
     * 程序终止时
     */
    fun onTerminate()

    /**
     * 低内存时执行
     */
    fun onLowMemory()

    /**
     * 配置改变时触发
     */
    fun onConfigurationChanged(newConfig: Configuration)

    /**
     * 程序在进行内存清理时执行
     */
    fun onTrimMemory()
}