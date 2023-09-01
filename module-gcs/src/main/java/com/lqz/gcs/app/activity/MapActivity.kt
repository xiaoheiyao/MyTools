package com.lqz.gcs.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lqz.commonsdk.application.AppContext
import com.lqz.gcs.R
import com.topxgun.imap.core.MapView
import com.topxgun.imap.core.internal.IMapDelegate
import com.topxgun.imap.core.internal.IMapViewDelegate
import com.topxgun.imap.model.MapType
import com.topxgun.imap_arcgis.ArcgisMapView
import com.topxgun.imap_arcgis.ArcgisMapWrapper
import java.util.*

class MapActivity : AppCompatActivity() {
    lateinit var mapView: MapView

    protected var mIMapViewDelegate: IMapViewDelegate? = null // 地图视图生命周期委托类

    protected var mIMap: IMapDelegate? = null //地图委托类

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.imap)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        mapView.initialize(getMapViewImp()) //初始化地图控件

        mapView.onCreate(savedInstanceState) //创建地图控件

        mIMapViewDelegate = mapView.getIMapView() //获取地图视图

        setUpMapIfNeeded() //设置地图

    }

    private fun getMapViewImp(): IMapViewDelegate {
        return ArcgisMapView(AppContext.getInstance())
    }

    /**
     * 如果需要设置地图
     */
    private fun setUpMapIfNeeded() {
        mIMapViewDelegate!!.getMapAsyc { map ->
            mIMap = map
            mIMap!!.uiSettings.setZoomControlsEnabled(false) //设置空间缩放
            mIMap!!.uiSettings.setRotateGesturesEnabled(true) //设置旋转
            mIMap!!.uiSettings.setCompassEnabled(true) //设置指南针
            /*设置地图加载监听器*/mIMap!!.setOnMapLoadedListener {
            //afterMapLoaded();
            mapView.postDelayed(Runnable {
                onBaseMapLoaded() //在已加载的地图上执行操作
            }, 500)
        }
            showMapType(mIMap!!) //设置地图类型
        }
    }

    /**
     * 设置地图类型
     * @param map 地图委托类
     * @param type 设置类型
     */
    fun showMapType(map: IMapDelegate) {
        if (map is ArcgisMapWrapper) {
            val wrapper = map
            wrapper.setLanguage(Locale.getDefault().language)
            wrapper.setSource("cn")
        }
        map.setMapType(MapType.MAP_TYPE_SATELLITE)
    }

    private fun onBaseMapLoaded() {

    }
}