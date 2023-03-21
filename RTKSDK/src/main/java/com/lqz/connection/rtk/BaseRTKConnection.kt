package com.lqz.connection.rtk

import com.lqz.open.api.base.BaseConnection
import com.lqz.open.api.base.ConnectionDelegate

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 13:50
 *    desc   : RTKConnection 的基类
 *              用于封装千寻服务，一些RT看的基本操作，以及RTK的通用操作
 */
abstract class BaseRTKConnection(
    private var delegate: ConnectionDelegate?
) : BaseConnection(delegate) {
}