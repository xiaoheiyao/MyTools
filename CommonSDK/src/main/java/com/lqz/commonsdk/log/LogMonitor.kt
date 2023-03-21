package com.lqz.commonsdk.log

import android.content.Context
import com.lqz.commonsdk.BuildConfig
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import java.io.*


/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/14 13:23
 *    desc   : 日志监视器，用于启动和销毁日志
 */
class LogMonitor(
    private val context: Context
) {

    private val loggers = HashMap<String, Log.LogInstance>()

    /*日志存储路径*/
    private val logPath: String = FileUtils.getLogPath(context)

    /*日志缓存路径*/
    private val cachePath: String = FileUtils.getLogCachePath(context)

    /**
     * 初始化
     */
    fun start() {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")

        Log.setLogImp(Xlog())

        TXGLog.setLogger(XLogNamedLogger(this))
    }

    fun stop() {
        Log.appenderClose()
    }

    class XLogNamedLogger(
        private val logMonitor: LogMonitor
    ) : ILogger {

        private fun getLogger(tag: String): Log.LogInstance {
            if (!logMonitor.loggers.containsKey(tag)) { //如果集合中没有这个tag
                var logTag = tag

                val logPath = logMonitor.logPath //fixme 所有日志先保存在同一文件夹下
                val cachePath = logMonitor.cachePath
                var logger: Log.LogInstance?
                if (BuildConfig.DEBUG) {
                    logger = Log.openLogInstance(
                        Xlog.LEVEL_ALL,
                        Xlog.AppednerModeAsync,
                        cachePath,
                        logPath,
                        logTag,
                        0
                    )
                    logger.setConsoleLogOpen(true)
                } else {
                    logger = Log.openLogInstance(
                        Xlog.LEVEL_INFO,
                        Xlog.AppednerModeAsync,
                        cachePath,
                        logPath,
                        logTag,
                        0
                    )
                    logger.setConsoleLogOpen(false)
                }

                logMonitor.loggers[tag] = logger //将日志加入集合中
            }
            return logMonitor.loggers[tag]!!
        }

        /**
         * 解析异常信息
         */
        private fun parseExceptionInfo(exception: Throwable): String {
            val outputStream = ByteArrayOutputStream()
            exception.printStackTrace(PrintStream(outputStream))
            val br =
                BufferedReader(InputStreamReader(ByteArrayInputStream(outputStream.toByteArray())))
            val outStringBuilder = StringBuilder()
            do {
                val info = br.readLine()
                outStringBuilder.appendLine(info)
            } while (info != null)

            return outStringBuilder.toString()
        }

        override fun v(tag: String, exception: Throwable) {
            v(tag, parseExceptionInfo(exception))
        }

        override fun v(tag: String, message: String, vararg values: Any) {
            getLogger(tag).v(tag, message, values)
        }

        override fun d(tag: String, exception: Throwable) {
            d(tag, parseExceptionInfo(exception))
        }

        override fun d(tag: String, message: String, vararg values: Any) {
            getLogger(tag).d(tag, message, values)
        }

        override fun i(tag: String, exception: Throwable) {
            i(tag, parseExceptionInfo(exception))
        }

        override fun i(tag: String, message: String, vararg values: Any) {
            getLogger(tag).i(tag, message, values)
        }

        override fun w(tag: String, exception: Throwable) {
            w(tag, parseExceptionInfo(exception))
        }

        override fun w(tag: String, message: String, vararg values: Any) {
            getLogger(tag).w(tag, message, values)
        }

        override fun e(tag: String, exception: Throwable) {
            e(tag, parseExceptionInfo(exception))
        }

        override fun e(tag: String, message: String, vararg values: Any) {
            getLogger(tag).e(tag, message, values)
        }


        override fun dump() {
            for (logger in logMonitor.loggers) {
                logger.value.appenderFlush() //异步flush
            }
        }

    }
}