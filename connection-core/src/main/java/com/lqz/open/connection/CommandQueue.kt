//package com.lqz.open.connection
//
//import com.lqz.message.LinkPacket
//import com.lqz.open.api.base.BaseConnection
//import com.lqz.open.connection.callback.PacketListener
//import java.util.concurrent.Executors
//import java.util.concurrent.Future
//
///**
// *    author : LQZ
// *    e-mail : qzli@topxgun.com
// *    date   : 2023/2/21 15:05
// *    desc   : 数据队列
// */
//class CommandQueue {
//
//    private var connection: BaseConnection? = null
//    private val ackTaskMap: Map<String, CommandQueue.AckFuture> = HashMap()
//
//    //定时器句柄
//    private val timeExecutor = Executors.newSingleThreadScheduledExecutor()
//
//    private val uiHandler: IPlatformHandler? = null
//
//    private val seqIntNo: Int = 0
//
//    private val seqByteNo: Byte = 0
//
//
//    private class AckFuture(val ackTask: AckTask, val future: Future<*>)
//
//
//    private class AckTask(private val listener: PacketListener, val cmd: String) :
//        Runnable {
//        private var errorCode = 0
//        private var ackResult = COMMAND_TIMED_OUT
//        private var packet: LinkPacket? = null
//
//        fun getListener(): PacketListener {
//            return listener
//        }
//
//        fun setAckResult(command: LinkPacket) {
//            packet = command
//            if (command is T1LinkPacket) {
//                val t1LinkPacket: T1LinkPacket = command as T1LinkPacket
//                if (t1LinkPacket.length === 1) {
//                    val result: Int = t1LinkPacket.data.getByte()
//                    if (result == 3) {
//                        ackResult = COMMAND_FAILED
//                        errorCode = TXGResultCode.TXG_CODE_CMD_UNSUPPORT
//                    } else if (result == 4) {
//                        ackResult = COMMAND_FAILED
//                        errorCode = TXGResultCode.TXG_CODE_CMD_CRC_ERROR
//                    } else {
//                        ackResult = COMMAND_SUCCEED
//                    }
//                } else {
//                    ackResult = COMMAND_SUCCEED
//                }
//            } else if (command is M2LinkPacket) {
//                val m2LinkPacket: M2LinkPacket = command as M2LinkPacket
//                if (m2LinkPacket.getLength() > 2) {
//                    val result: Int = m2LinkPacket.getData().getByte()
//                    if (result == M2AckResult.ACK_CMD_ACCEPTED) {
//                        ackResult = COMMAND_SUCCEED
//                    } else {
//                        ackResult = COMMAND_FAILED
//                        errorCode = result
//                    }
//                } else {
//                    ackResult = COMMAND_SUCCEED
//                }
//            } else if (command is ApolloResponsePacket) {
//                val apolloResposePacket: ApolloResponsePacket = command as ApolloResponsePacket
//                val response: ProtoWork.WorkResponse = apolloResposePacket.getWorkResponse()
//                if (response.getStatusCode() === 0) {
//                    ackResult = COMMAND_SUCCEED
//                } else {
//                    ackResult = COMMAND_FAILED
//                    errorCode = response.getStatusCode()
//                }
//            } else if (command is FYPacket) {
//                ackResult = COMMAND_SUCCEED
//            } else if (command is R20Message) {
//                ackResult = COMMAND_SUCCEED
//            } //todo lqz 这里加一个判断类型
//        }
//
//        override fun run() {
//            ackTaskMap.remove(cmd)
//            when (ackResult) {
//                COMMAND_TIMED_OUT -> //                    long currentRealtime = System.currentTimeMillis();
////                    long timeRange = currentRealtime - listener.getCreateTime();
//                    if ( /*(timeRange<= listener.getTimeOut() * listener.getReSendCount() || */listener.getReSendCount() < listener.getMaxSendCount() /*)*/ && connection.isDelegateConnected()) {
//                        //重发次数+1
//                        listener.setReSendCount(listener.getReSendCount() + 1)
//
//
//                        //重新发送
//                        connection.sendMessage(listener.getMessage(), listener)
//                    } else {
//                        uiHandler.runOnUIThread(Runnable { //                                    listener.onFaild();
//                            listener.onTimeout()
//                        })
//                    }
//                COMMAND_SUCCEED -> uiHandler.runOnUIThread(Runnable {
//                    listener.onSuccess(packet)
//                    if (connection != null) {
//                        if (connection is AircraftConnection) {
//                            val aircraftConnection: AircraftConnection =
//                                connection as AircraftConnection
//                            if (aircraftConnection.getMessageListener() != null) {
//                                aircraftConnection.getMessageListener().onAckMessage(packet)
//                            }
//                        }
//                    }
//                })
//                COMMAND_FAILED -> if (errorCode == TXGResultCode.TXG_CODE_CMD_CRC_ERROR && listener.getReSendCount() < listener.getMaxSendCount() && connection.isDelegateConnected()) {
//                    //CRC校验失败
//                    //重发次数+1
//                    listener.setReSendCount(listener.getReSendCount() + 1)
//                    //重新发送
//                    connection.sendMessage(listener.getMessage(), listener)
//                } else {
//                    uiHandler.runOnUIThread(Runnable {
//                        listener.onFaild()
//                        listener.onFaild(errorCode)
//                    })
//                }
//            }
//        }
//
//        companion object {
//            private const val COMMAND_TIMED_OUT = -1
//            private const val COMMAND_SUCCEED = 0
//            private const val COMMAND_FAILED = 1
//        }
//    }
//
//}