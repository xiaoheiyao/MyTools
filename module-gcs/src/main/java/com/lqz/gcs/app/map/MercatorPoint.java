package com.lqz.gcs.app.map;


/**
 * Created by rjm4413 on 2016/4/14.
 * 墨卡托点
 */
public class MercatorPoint {
    public int no;
    public double x;
    public double y;
    public MercatorPoint(){
    }

    public MercatorPoint(double x, double y) {
        this.x =x;
        this.y =y;
    }
}
