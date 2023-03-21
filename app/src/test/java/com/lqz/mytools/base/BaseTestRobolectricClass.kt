package com.lqz.mytools.base

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.lqz.commonsdk.utils.AppInfoUtil
import com.lqz.mytools.application.MyApplication
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/13 16:19
 *    desc   :
 */
@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowLog::class], sdk = [23], application = MyApplication::class)
//@Config(shadows = [ShadowLog::class], sdk = [23])
class BaseTestRobolectricClass {

    protected val mContext: Context = ApplicationProvider.getApplicationContext()

    companion object {
        @JvmStatic
        protected val TAG: String = this::class.java.simpleName

        @BeforeClass
        @JvmStatic
        fun setup() {
            ShadowLog.stream = System.out
        }
    }

    @Test
    fun lqz() {
        Assert.assertEquals(AppInfoUtil.getAppName(mContext), "MyTools")
    }

}