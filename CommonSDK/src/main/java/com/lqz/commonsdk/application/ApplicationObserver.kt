package com.lqz.commonsdk.application

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.lqz.commonsdk.log.LogMonitor
import com.lqz.commonsdk.utils.AppInfoUtil
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mars.BuildConfig


/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/13 13:28
 *    desc   : 通过Lifecycle的方式绑定application，此方式只能监听onCreate，无法监听程序终止，低内存清除，配置文件改变等
 */
class ApplicationObserver(val application: Application) : LifecycleObserver {

    private val TAG = this.javaClass.name

    private lateinit var logMonitor: LogMonitor

    private val BUGLY_ID = "979949822f"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.e(TAG, "onCreate:")
        //todo 初始化一个AppContext类，供整个app调用！！！
        AppContext.initAppContext(application)
        logMonitor = LogMonitor(application)
        logMonitor.start()

        val userStrategy: CrashReport.UserStrategy = CrashReport.UserStrategy(application)
//        userStratus.setDeviceID()  //设置设备ID


        userStrategy.setAppVersion(AppInfoUtil.getAppVersionName(application))
        AppInfoUtil.getVersionCode(application)
        AppInfoUtil.getPackageName(application)
        AppInfoUtil.getAppName(application)
        CrashReport.initCrashReport(application, BUGLY_ID, BuildConfig.DEBUG)
    }

    //    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun onCreate(owner: LifecycleOwner) {
//        Timber.e("onCreate_ON_CREATE")
//    }

}



