package com.lqz.commonsdk.application

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import com.lqz.commonsdk.log.LogMonitor
import com.lqz.commonsdk.log.TXGLog
import org.robolectric.RuntimeEnvironment.application

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 15:32
 *    desc   : application基类，暂时并不完整，先按照这种不规范的形式实现
 */
class CommonApplication(
    val application: Application
) : IApplication {

    private val TAG = this.javaClass.name

    override fun onCreate() {
        Log.e(TAG, "onCreate:")
    }

    override fun onTerminate() {
        Log.e(TAG, "onTerminate:")
    }

    override fun onLowMemory() {
        Log.e(TAG, "onLowMemory:")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.e(TAG, "onConfigurationChanged:")
    }

    override fun onTrimMemory() {
        Log.e(TAG, "onTrimMemory:")
    }
}