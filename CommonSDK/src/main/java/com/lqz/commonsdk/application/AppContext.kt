package com.lqz.commonsdk.application

import android.app.Application

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/3/29 15:17
 *    desc   :
 */
object AppContext {

    private var application: Application? = null

    fun initAppContext(app: Application) {
        application = app
    }

    @JvmStatic
    fun getInstance(): Application? {
        return application
    }

    @JvmStatic
    fun getResString(resId: Int): String? {
        return getInstance()?.getApplicationContext()?.getResources()?.getString(resId)
    }

    @JvmStatic
    fun getResArray(resId: Int): Array<String?>? {
        return getInstance()?.getApplicationContext()?.getResources()?.getStringArray(resId)
    }

    @JvmStatic
    fun getResBoolean(resId: Int): Boolean? {
        return getInstance()?.getApplicationContext()?.getResources()?.getBoolean(resId)
    }

    @JvmStatic
    fun getResInteger(resId: Int): Int? {
        return getInstance()?.getApplicationContext()?.getResources()?.getInteger(resId)
    }

    @JvmStatic
    fun getResColor(resId: Int): Int? {
        return getInstance()?.getApplicationContext()?.getResources()?.getColor(resId)
    }

}