package com.lqz.commonsdk.base

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.BeforeClass
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
//@Config(shadows = [ShadowLog::class], sdk = [23], application = BaseTestApplication::class)
@Config(shadows = [ShadowLog::class], sdk = [23])
abstract class BaseTestRobolectricClass {

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

}