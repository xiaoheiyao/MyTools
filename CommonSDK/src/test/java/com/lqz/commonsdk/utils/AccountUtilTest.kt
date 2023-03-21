package com.lqz.commonsdk.utils

import org.junit.Assert
import org.junit.Test
import javax.validation.constraints.AssertTrue

/**
 * author : LQZ
 * e-mail : qzli@topxgun.com
 * date   : 2023/2/13 17:18
 * desc   :
 */
internal class AccountUtilTest {

    @Test
    fun testIsEmail() {
        Assert.assertEquals(AccountUtil.isEmail("lq.z@qq.com"),true)
        Assert.assertTrue(AccountUtil.isEmail("z@qq.com"))
    }

    @Test
    fun testIsEmail2() {
        Assert.assertTrue(AccountUtil.isEmail("z@qq.com"))
    }

    @Test
    fun testIsEmail3() {
        Assert.assertFalse(AccountUtil.isEmail("z@qq.com"))
    }
}