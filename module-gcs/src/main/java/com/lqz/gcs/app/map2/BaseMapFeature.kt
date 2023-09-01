package com.lqz.gcs.app.map2

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.lqz.gcs.app.service.LocationService

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/4/7 17:40
 *    desc   : 用kt写一遍展示基类
 */
open class BaseMapFeature : AMapLocationListener {

//    companion object{
protected val Z_INDEX_PROJECTION: Int = 16
    protected val Z_INDEX_PLANE = 15
    protected val Z_INDEX_BREAK_POINT = 13


    protected val Z_INDEX_GPS = 12
    protected val Z_INDEX_PERSON = 12
    protected val Z_INDEX_HOME = 11

    protected val Z_INDEX_SPARY_LINE = 10
    protected val Z_INDEX_PATH_LINE = 9
    protected val Z_INDEX_GROUND_CHECK = 19

    protected val Z_INDEX_GROUND_ROUTE_START_END = 8
    protected val Z_INDEX_SPLIT_GROUND = 7
    protected val Z_INDEX_REFRENCE = 7
    protected val Z_INDEX_AB_PLAN = 7
    protected val Z_INDEX_GROUND_LINE_MARKER = 6
    protected val Z_INDEX_GROUND_LINE_EDIT_MARKER = 5
    protected val Z_INDEX_GROUND_ROUTE = 5
    protected val Z_INDEX_GROUND_AREA_MARKER = 4
    protected val Z_INDEX_OBSTACLE = 4
    protected val Z_INDEX_GROUND_POINT_MARKER = 3
    protected val Z_INDEX_GROUND_SELECT = 3
    protected val Z_INDEX_GROUND = 2
    private val locationService: LocationService? = null
    protected val Z_INDEX_AREA_LIMIT = 1

    /**
     * 地图显示模式-普通地图
     */
    val MAP_NOMAL = 1

    /**
     * 地图显示模式- 卫星地图
     */
    val MAP_SATELLITE = 2
//    }

    override fun onLocationChanged(p0: AMapLocation?) {
        TODO("Not yet implemented")
    }
}