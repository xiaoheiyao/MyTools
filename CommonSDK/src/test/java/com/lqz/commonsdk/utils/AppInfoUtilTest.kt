package com.lqz.commonsdk.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.lqz.commonsdk.base.BaseTestRobolectricClass
import org.junit.Assert
import org.junit.Test
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

/**
 * author : LQZ
 * e-mail : qzli@topxgun.com
 * date   : 2023/2/13 16:13
 * desc   :
 */
//@Config(shadows = [], sdk = [23], application = BaseTestApplication::class)
@Config(shadows = [], sdk = [23])
internal class AppInfoUtilTest : BaseTestRobolectricClass() {

    @Test
    fun TestGetVersionCode() {
        Assert.assertEquals(AppInfoUtil.getAppName(context = mContext),"MyTools")
        Assert.assertEquals(AppInfoUtil.getAppName(context = mContext),"1.01")
    }

    @Test
    fun getAppVersionName() {
    }

    @Test
    fun getPackageName() {
    }

    @Test
    fun getAppName() {
    }
}