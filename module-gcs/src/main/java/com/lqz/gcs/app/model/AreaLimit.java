package com.lqz.gcs.app.model;

import java.util.List;

/**
 * Created by TOPXGUN on 2017/12/11.
 * 禁飞区
 */

public class AreaLimit {
    public int id;
    public String name;
    public int type;// 禁飞区类型 0-飞机场 1-圆形 2-扇形 3-多边形
    public long startDate;
    public long endDate;
    public PolygonLimit poly;//多边形
    public AirportLimit src;//机场元信息
    public CircleLimit circle;//圆形
    public SectorLimit sector;//扇形
    public List<Coordinate> coordinates;


    public class PolygonLimit{
        public List<double[]> coordinates;
        public float height;
    }
    public class AirportLimit{
        public String name;
        public String cityCode;
        public String trackNum;
        public double[] A1;
        public double[] A2;
        public double[] C2;
        public double radius1; //米;
        public double[] B2;
        public double[] B3;
        public double radius2; //米
        public double[] C3;
        public double[] A3;
        public double[] A4;
        public double[] C4;
        public double radius3;
        public double[] B4;
        public double[] B1;
        public double radius4;
        public double[] C1;
        public String effectiveDate;
    }

    public class SectorLimit{
        public double[] center;
        public double radius; //单位千米
        public double bearing1;
        public double bearing2;
        public double height;
    }

    public class CircleLimit{
        public double[] center;
        public double radius;//单位千米
        public double height;
    }

    public class Coordinate{
        public double x;
        public double y;
    }
}
