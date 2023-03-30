package com.lqz.gcs.test;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lqz.gcs.app.model.BasePoint;

/**
 * author : LQZ
 * e-mail : qzli@topxgun.com
 * date   : 2023/3/29 20:03
 * desc   :
 */
public class dingwei {


    private static LocationClient client = null;
    private static LocationClientOption mOption;
    private static LocationClientOption DIYoption;
    private Object objLock;
    /***
     * 初始化 LocationClient
     *
     * @param locationContext
     */
    public dingwei(Context locationContext) throws Exception {
        objLock = new Object();
        synchronized (objLock) {
            if (client == null) {
                LocationClient.setAgreePrivacy(true);
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    public void start() {
        registerListener(mBaiduListener);
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void stop() {
        unregisterListener(mBaiduListener);
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }


    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType( "gcj02" ); // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(3000); // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true); // 可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true); // 可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false); // 可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false); // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true); // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
            mOption.setIsNeedLocationDescribe(true); // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
            mOption.setIsNeedLocationPoiList(true); // 可选，默认false，设置是否需要POI结果，可以在BDLocation
            mOption.SetIgnoreCacheException(false); // 可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setOpenGps(true); // 可选，默认false，设置是否开启Gps定位
            mOption.setIsNeedAltitude(false); // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption;
    }

    /***
     * 注册定位监听
     *
     * @param listener
     * @return
     */

    public boolean registerListener(BDAbstractLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mBaiduListener = new BDAbstractLocationListener() {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            Log.e("dingwei", "onReceiveLocation: " + location.getLatitude() + "  " + location.getLongitude());

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                //    Log.d("catfishgps", "百度" + location.getAddrStr() + "：" + location.getLongitude() + ":" + location.getLatitude());
                BasePoint point = new BasePoint();
                //31.8271934：118.7739121

                point.initPointer(location.getLatitude(), location.getLongitude(), BasePoint.GEO_TYPE_GCJ);

                Log.e("dingwei", "onReceiveLocation: " + location.getLatitude() + "  " + location.getLongitude());


            }

            //      EventBus.getDefault().post(new LocationEvent(location.getLatitude(), location.getLongitude(), BasePoint.GEO_TYPE_GCJ));
            // EventBus.getDefault().post(currentLocation);

        }

    };
}
