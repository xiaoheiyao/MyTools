package com.lqz.message

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 16:49
 *    desc   :
 */
interface ILinkMessage {
    /**
     * 组装数据包
     */
    fun pack(): LinkPacket?
}