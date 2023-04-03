package com.lqz.mytools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.baidu.location.LocationClient
import com.lqz.commonsdk.log.TXGLog
import com.lqz.gcs.app.map.MercatorPoint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v("LQZ","mainActivity onCreate:")

    }
}