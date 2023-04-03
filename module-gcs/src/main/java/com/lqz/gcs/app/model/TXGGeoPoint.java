package com.lqz.gcs.app.model;

import java.io.Serializable;

public class TXGGeoPoint implements Serializable {
    //纬度值
    private double lat;

    //经度值
    private double lon;

    /**
     * 构造函数
     */
    public TXGGeoPoint() {

    }

    /**
     * 构造函数
     *
     * @param lat 纬度
     * @param lon 经度
     */
    public TXGGeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * 获取纬度值
     *
     * @return 纬度值
     */
    public double getLat() {
        return lat;
    }

    /**
     * 设置纬度值
     *
     * @param lat 纬度值
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * 获取经度值
     *
     * @return 经度值
     */
    public double getLon() {
        return lon;
    }

    /**
     * 设置经度值
     *
     * @param lon 经度值
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
