package com.lqz.open.api.model

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 10:55
 *    desc   :
 */
enum class ConnectType(var type: String) {
    TYPE_NULL("null"),
    TYPE_BT("bt"),
    TYPE_BLE("ble"),
    TYPE_4G("4g"),
    TYPE_USB("usb"),
    TYPE_WIFI("wifi"),
    TYPE_R1("R1"),
    TYPE_APOLLO("apollo"),
    TYPE_TCPSVR("tcpsvr"),
    TYPE_OTHER("other"),
    TYPE_FY("FY"),
    TYPE_TXG_RC("TXG-RC"),
    TYPE_TXG_RC_SUBS("TXG-RC-SBUS"),
    TYPE_TXG_RC_D2D("TXG-RC-D2D"),
    TYPE_TC2("tc2"),
    TYPE_UART3("uart3"),
    TYPE_GPS("gps");


    /**
     * 根据连接类型
     *
     * @param type 连接类型
     * @return 连接类型对象
     */
    open fun getType(type: String): ConnectType? {
        for (moduleType in values()) {
            if (moduleType.type == type) {
                return moduleType
            }
        }
        return null
    }

    /**
     * 根据索引获取连接类型对象
     *
     * @param index 索引
     * @return 连接类型对象
     */
    open fun getType(index: Int): ConnectType? {
        for (moduleType in values()) {
            if (moduleType.ordinal == index) {
                return moduleType
            }
        }
        return null
    }
}