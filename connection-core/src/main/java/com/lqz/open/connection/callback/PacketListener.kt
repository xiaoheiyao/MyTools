package com.lqz.open.connection.callback

import com.lqz.message.ILinkMessage

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 17:03
 *    desc   : todo 这个抽象类并不好，想办法将setMessage 改到接口中去
 */
abstract class PacketListener(
    private var timeOut: Long,
    private var maxSendCount: Int = 3
) : LinkListener<Any?> {

    private var createTime: Long = 0
    private var message: ILinkMessage? = null

    /**
     * 重发次数设定
     */
    private var reSendCount = 1

    init {
        createTime = System.currentTimeMillis()
    }

    fun setMaxSendCount(count: Int) {
        maxSendCount = count
    }

    /**
     * 获取重试次数
     * @return
     */
    open fun getReSendCount(): Int {
        return reSendCount
    }

    /**
     * 设置重试次数
     * @param reSendCount
     */
    open fun setReSendCount(reSendCount: Int) {
        this.reSendCount = reSendCount
    }

    open fun getMessage(): ILinkMessage? {
        return message
    }

    open fun setMessage(message: ILinkMessage?) {
        this.message = message
    }

    open fun getCreateTime(): Long {
        return createTime
    }

    open fun setCreateTime(createTime: Long) {
        this.createTime = createTime
    }

    open fun getTimeOut(): Long {
        return timeOut
    }

    open fun setTimeOut(timeOut: Long) {
        this.timeOut = timeOut
    }

    /**
     * 当前发送进度
     */
    abstract fun onSendProgress(percent: Int)
}