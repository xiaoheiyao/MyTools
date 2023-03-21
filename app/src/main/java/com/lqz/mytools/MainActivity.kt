package com.lqz.mytools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.lqz.commonsdk.log.TXGLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v("LQZ","mainActivity onCreate:")
    }
}