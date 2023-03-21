package com.lqz.open.android

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.lqz.open.api.base.ConnectionDelegate
import com.lqz.open.api.model.ConnectType
import com.lqz.open.utils.AndroidLog
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.UnknownHostException
import java.util.*

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/2/16 10:12
 *    desc   : 蓝牙通道连接
 */
class BluetoothConnection(
    val mContext: Context,
    private val bluetoothAddress: String
) : ConnectionDelegate() {

    private val BLUE = "BLUETOOTH"
    private val UUID_SPP_DEVICE = "00001101-0000-1000-8000-00805F9B34FB"

    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var output: OutputStream? = null
    private var input: InputStream? = null
    private var bluetoothSocket: BluetoothSocket? = null

//    private val mContext: Context? = null

    init {
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothAdapter =
            (mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        AndroidLog.i("init BluetoothConnection address:$bluetoothAddress")
    }

    /**
     * 获得当前连接蓝牙的地址
     */
    fun getBluetoothAddress(): String {
        return bluetoothAddress
    }

    /**
     * 获得蓝牙名称
     */
    @SuppressLint("MissingPermission")
    fun getBluetoothName(): String? {
        val device: BluetoothDevice
        try {
            device = mBluetoothAdapter!!.getRemoteDevice(bluetoothAddress)
            return device.name
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            AndroidLog.i("getBluetoothName:IllegalArgumentException = ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidLog.i("getBluetoothName:Exception = ${e.message}")
        }
        return ""
    }

    override fun writeData(data: ByteArray) {
        try {
            output?.write(data)
        } catch (e: IOException) {
            AndroidLog.e("BluetoothConnection writeData failed:${e.message}")
            throw e
        }
    }

    override fun readData(data: ByteArray): Int {
        var length = 0
        try {
            length = input?.read(data) ?: 0

        } catch (e: IOException) {
            e.printStackTrace()
            AndroidLog.e("BluetoothConnection readData failed:${e.message}")
            throw e
        }
        return length
    }

    /**
     * 建立与蓝牙的连接
     */
    @SuppressLint("MissingPermission")
    override fun openConnection(listener: IConnectStatus) {
        AndroidLog.i("openConnection")
        disConnectionBluetooth() //连接之前，如果有其他连接，先断开

        var device: BluetoothDevice? = null
        try {
            device = mBluetoothAdapter?.getRemoteDevice(bluetoothAddress)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            AndroidLog.e("openConnection:IllegalArgumentException = ${e.message}")
            listener.connectFail()
        }

        try {
            if (device == null) {
                device = findSerialBluetoothBoard()
            }
            bluetoothSocket =
                device!!.createInsecureRfcommSocketToServiceRecord(UUID.fromString(UUID_SPP_DEVICE))
            mBluetoothAdapter?.cancelDiscovery()
            if (null == bluetoothSocket) {
                AndroidLog.e("Bluetooth socket is null, connect failure.")
                listener.connectFail()
            }
            bluetoothSocket!!.connect()
            output = bluetoothSocket!!.outputStream
            input = bluetoothSocket!!.inputStream
        } catch (e: Exception) {
            e.printStackTrace()
            AndroidLog.e("Connect with the bluetooth remote device failed." + e.message)
            listener.connectFail()
        }
        AndroidLog.i("openConnection:Connect the bluetooth device success.")
        listener.connectSuccess()
    }

    override fun closeConnection() {
        AndroidLog.i("closeConnection bluetooth")
        disConnectionBluetooth()
    }

    override fun getConnectType(): ConnectType {
        return ConnectType.TYPE_BT
    }

//    override fun canReconnect(): Boolean {
//        return true //todo 蓝牙连接也要设置成循环连接？？？这个需要在评估一下！！！
//    }

    @SuppressLint("NewApi", "MissingPermission")
    @Throws(UnknownHostException::class)
    private fun findSerialBluetoothBoard(): BluetoothDevice? {
        val pairedDevices = mBluetoothAdapter?.bondedDevices
        if (pairedDevices.isNullOrEmpty()) {
            for (device in pairedDevices!!) {
                val deviceUuids = device.uuids
                if (deviceUuids.isNullOrEmpty()) {
                    for (id in device.uuids) {
                        if (id.toString().equals(UUID_SPP_DEVICE)) {
                            return device
                        }
                    }
                }
            }
        }
        throw UnknownHostException("No Bluetooth Device found")
    }

    /**
     * 断开蓝牙连接
     */
    private fun disConnectionBluetooth() {
        try {
            if (input != null) {
                input?.close()
                input = null
            }
            if (output != null) {
                output?.close()
                output = null
            }
            if (bluetoothSocket != null) {
                bluetoothSocket?.close()
                bluetoothSocket = null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}