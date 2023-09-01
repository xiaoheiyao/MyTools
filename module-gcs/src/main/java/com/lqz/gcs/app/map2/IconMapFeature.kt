package com.lqz.gcs.app.map2

import android.view.View
import com.lqz.gcs.R
import com.lqz.gcs.app.map2.interf.IIconMapFeature
import com.lqz.gcs.app.model.BasePoint
import com.topxgun.imap.model.IBitmapDescriptorFactory
import com.topxgun.imap.model.IMarkerOptions

/**
 *    author : LQZ
 *    e-mail : qzli@topxgun.com
 *    date   : 2023/4/7 17:45
 *    desc   : 通用的地图：人的定位，飞机的显示，RTK的展示，指南针的展示
 */
class IconMapFeature: BaseMapFeature(), IIconMapFeature {


    /**
     * 展示人的位置
     * @param point 人的坐标
     */
    override fun showPerson(point: BasePoint) {

//        personLatLng = point.latLngForMap
//        if (personMarker == null) {
//            if (null == aMap) return
//            val markerOptions = IMarkerOptions()
//            markerOptions.position(personLatLng)
//            val personView = View.inflate(mMapView.getContext(), R.layout.view_person_point, null)
//            markerOptions.icon(IBitmapDescriptorFactory.fromView(personView))
//            markerOptions.draggable(false)
//            markerOptions.anchor(0.5f, 0.5f)
//            markerOptions.zIndex(Z_INDEX_PERSON.toFloat())
//            personMarker = aMap.addMarker(markerOptions)
//        } else {
//            personMarker.setPosition(personLatLng)
//        }
    }
}