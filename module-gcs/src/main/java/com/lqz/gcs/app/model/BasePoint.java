package com.lqz.gcs.app.model;

import com.lqz.gcs.app.map.MercatorProjection;
import com.lqz.gcs.app.model.base.Base;

import com.topxgun.algorithm.geometry.Point;
import com.topxgun.algorithm.util.MathUtils;
import com.topxgun.imap.model.ILatLng;
import com.topxgun.imap.utils.IMapUtils;
import com.topxgun.imap.utils.geotransport.GCJPointer;
import com.topxgun.imap.utils.geotransport.WGSPointer;

/**
 * Created by rjm4413 on 2016/12/24.
 */

public class BasePoint extends Base {
    public static final int GEO_TYPE_GCJ = 0;
    public static final int GEO_TYPE_WGS = 1;

    protected GCJPointer mGCJPointer;
    protected WGSPointer mWGSPointer;

    public BasePoint(){
        mGCJPointer = new GCJPointer();
        mWGSPointer = new WGSPointer();
    }

    public BasePoint(double lat, double lon, int geoType){
        if(geoType==GEO_TYPE_GCJ){
            mGCJPointer =new GCJPointer(lat,lon);
            transportWgs();
        }else if(geoType==GEO_TYPE_WGS){
            mWGSPointer = new WGSPointer(lat,lon);
            transportGcj();
        }
    }


    public void initPointer(double lat,double lon,int geoType){
        if(geoType==GEO_TYPE_GCJ){
            mGCJPointer =new GCJPointer(lat,lon);
            transportWgs();
        }else if(geoType==GEO_TYPE_WGS){
            mWGSPointer = new WGSPointer(lat,lon);
            transportGcj();
        }
    }
    public void updateLatLngFromMap(ILatLng iLatLng) {
//        if (!CacheManager.getInstace().getMapReactifySwitch()) {
            mWGSPointer.setLatitude(iLatLng.latitude);
            mWGSPointer.setLongitude(iLatLng.longitude);
            transportGcj();
//        } else {
//            mGCJPointer.setLatitude(iLatLng.latitude);
//            mGCJPointer.setLongitude(iLatLng.longitude);
//            transportWgs();
//        }
    }
    public void updateLatLngFromEdit(double lat, double lon) {
        mWGSPointer.setLatitude(lat);
        mWGSPointer.setLongitude(lon);
        transportGcj();
    }

    public Point getMercatorPointForMap(){
        ILatLng latLng = getLatLngForMap();
        double x = MercatorProjection.longitudeToX(latLng.longitude);
        double y = MercatorProjection.latitudeToY(latLng.latitude);
        Point mercatorPoint = new Point(x, y);
        return mercatorPoint;
    }
    public Point getMercatorPointForWgs(){
        ILatLng latLng = getWgsLatLng();
        double x = MercatorProjection.longitudeToX(latLng.longitude);
        double y = MercatorProjection.latitudeToY(latLng.latitude);
        Point mercatorPoint = new Point(x, y);
        return mercatorPoint;
    }



    /**
     * 将gcj转化为wgs
     */
    protected void transportWgs() {
        mWGSPointer = mGCJPointer.toExactWGSPointer();
    }

    /**
     * 将wgs转化为gcj
     */
    protected void transportGcj() {
        mGCJPointer = mWGSPointer.toGCJPointer();
    }
    public ILatLng getWgsLatLng(){
        return new ILatLng(mWGSPointer.getLatitude(),mWGSPointer.getLongitude());
    }
    public ILatLng getGcjLatLng(){
        return new ILatLng(mGCJPointer.getLatitude(),mGCJPointer.getLongitude());
    }

    public ILatLng getLatLngForMap() {
//        if (!CacheManager.getInstace().getMapReactifySwitch()) {
            if(mWGSPointer==null){
                transportWgs();
            }
            return new ILatLng(mWGSPointer.getLatitude(),mWGSPointer.getLongitude());
//        } else {
//            if(mGCJPointer==null){
//                transportGcj();
//            }
//            return new ILatLng(mGCJPointer.getLatitude(),mGCJPointer.getLongitude());
//        }
    }
    public boolean isZeroLatLng(){
        return MathUtils.doubleZero(getWgsLatLng().latitude)&&MathUtils.doubleZero(getWgsLatLng().longitude);
    }

    public boolean isLegal(){
        return IMapUtils.checkILatLng(getWgsLatLng());
    }

    private String tag="";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
