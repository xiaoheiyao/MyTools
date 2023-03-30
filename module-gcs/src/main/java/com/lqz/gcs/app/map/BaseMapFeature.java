//package com.lqz.gcs.app.map;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.location.LocationProvider;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.core.app.ActivityCompat;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.baidu.location.BDAbstractLocationListener;
//import com.baidu.location.BDLocation;
//import com.lqz.commonres.utils.CommonUtil;
//import com.lqz.commonres.utils.DensityUtil;
//import com.lqz.commonsdk.application.AppContext;
//import com.lqz.commonres.R;
//import com.lqz.gcs.app.model.AreaLimit;
//import com.lqz.gcs.app.model.BasePoint;
//import com.lqz.gcs.app.service.AreaLimitManager;
//import com.lqz.gcs.app.service.LocationService;
//import com.topxgun.agservice.gcs.app.event.ZoomLevel;
//
//import com.topxgun.algorithm.geometry.Point;
//import com.topxgun.algorithm.util.TimeLogUtil;
//import com.topxgun.commonres.utils.DensityUtil;
//import com.topxgun.commonsdk.connection.TXGSdkManagerApollo;
//import com.topxgun.imap.core.internal.ICircleDelegate;
//import com.topxgun.imap.core.internal.IMapDelegate;
//import com.topxgun.imap.core.internal.IMapViewDelegate;
//import com.topxgun.imap.core.internal.IMarkerDelegate;
//import com.topxgun.imap.core.internal.IOverlayDelegate;
//import com.topxgun.imap.core.internal.IPolygonDelegate;
//import com.topxgun.imap.core.internal.IPolylineDelegate;
//import com.topxgun.imap.core.listener.OnCameraChangeListener;
//import com.topxgun.imap.core.listener.OnMapMarkerClickListener;
//import com.topxgun.imap.model.IBitmapDescriptorFactory;
//import com.topxgun.imap.model.ICameraUpdate;
//import com.topxgun.imap.model.ICameraUpdateFactory;
//import com.topxgun.imap.model.ICircleOptions;
//import com.topxgun.imap.model.ILatLng;
//import com.topxgun.imap.model.ILatLngBounds;
//import com.topxgun.imap.model.IMarkerOptions;
//import com.topxgun.imap.model.IPolygonOptions;
//import com.topxgun.imap.model.IPolylineOptions;
//import com.topxgun.imap.model.MapType;
//import com.topxgun.imap.utils.IMapUtils;
//import com.topxgun.newui.view.weight.CompassView;
//import com.topxgun.protocol.model.TXGGeoPoint;
//import com.topxgun.protocol.model.TXGWhiteListZone;
//
//import org.simple.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
///**
// * Created by rjm4413 on 2016/12/21.
// */
//
//public class BaseMapFeature implements AMapLocationListener {
//    protected final static int Z_INDEX_PROJECTION = 16;
//    protected final static int Z_INDEX_PLANE = 15;
//    protected final static int Z_INDEX_BREAK_POINT = 13;
//
//
//    protected final static int Z_INDEX_GPS = 12;
//    protected final static int Z_INDEX_PERSON = 12;
//    protected final static int Z_INDEX_HOME = 11;
//
//    protected final static int Z_INDEX_SPARY_LINE = 10;
//    protected final static int Z_INDEX_PATH_LINE = 9;
//    protected final static int Z_INDEX_GROUND_CHECK = 19;
//
//    protected final static int Z_INDEX_GROUND_ROUTE_START_END = 8;
//    protected final static int Z_INDEX_SPLIT_GROUND = 7;
//    protected final static int Z_INDEX_REFRENCE = 7;
//    protected final static int Z_INDEX_AB_PLAN = 7;
//    protected final static int Z_INDEX_GROUND_LINE_MARKER = 6;
//    protected final static int Z_INDEX_GROUND_LINE_EDIT_MARKER = 5;
//    protected final static int Z_INDEX_GROUND_ROUTE = 5;
//    protected final static int Z_INDEX_GROUND_AREA_MARKER = 4;
//    protected final static int Z_INDEX_OBSTACLE = 4;
//    protected final static int Z_INDEX_GROUND_POINT_MARKER = 3;
//    protected final static int Z_INDEX_GROUND_SELECT = 3;
//    protected final static int Z_INDEX_GROUND = 2;
//    private LocationService locationService;
//    protected final static int Z_INDEX_AREA_LIMIT = 1;
//
//    /**
//     * 地图显示模式-普通地图
//     */
//    public static final int MAP_NOMAL = 1;
//
//    /**
//     * 地图显示模式- 卫星地图
//     */
//    public static final int MAP_SATELLITE = 2;
//
//    protected IMapViewDelegate mMapView;
//    protected IMapDelegate aMap;
//    //protected MapScaleView mMapScaleView;
//    protected CompassView compassView;
//    protected IMarkerDelegate personMarker;
//
//    //声明AMapLocationClient类对象
//    public AMapLocationClient mLocationClient = null;
//
//    //声明mLocationOption对象
//    public AMapLocationClientOption mLocationOption = null;
//
//    protected IMarkerDelegate planeMarker;
//    protected IPolylineDelegate pathLine;
//    protected List<IOverlayDelegate> noflyZoneList = new ArrayList<>();
//    protected List<IOverlayDelegate> noflyWhiteZoneList = new ArrayList<>();
//
//    private boolean isDrawPathLine;
//    private long lastUpdateTime;
//
//    protected boolean isFocusToLocation = true;
//    private boolean isFirstLocation = true;
//    private boolean isShowPlane = true;
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////
//    private static final int CHECK_INTERVAL = 1000 * 30;
//
//    //定位管理对象
//    private LocationManager locationManager = null;
//
//    //卫星定位方式
//    private LocationProvider gpsProvider = null;
//
//    //卫星定位监听器
//    private AppLocationListener gpsListener = new AppLocationListener();
//
//    //网络定位方式
//    private LocationProvider networkProvider = null;
//
//    //网络定位监听器
//    private AppLocationListener networkListener = new AppLocationListener();
//
//    //当前位置
//    private Location currentLocation = null;
//
//    private boolean focusPerson = false;
//
//    protected double lastRotate;
//    ILatLng TmarkLocation;
//    private HashMap<String, IPolylineDelegate> routeMap = new HashMap<>();
//
//    //  private HashMap<String, List<IPolylineDelegate>> planeRouteMap= new HashMap<>();
//    private HashMap<String, HashMap<String, IPolylineDelegate>> planeRouteMap = new HashMap<>();
//    private HashMap<String, IMarkerDelegate> flightMarkerMap = new HashMap<>();
//    // private HashMap<String,IMarkerDelegate> planeMarkerMap = new HashMap<>();
//
//    private HashMap<String, IMarkerDelegate> areaMarkerMap = new HashMap<>();
//    private boolean isShowArea;
//    public ILatLng personLatLng;
//
//    public BaseMapFeature(IMapViewDelegate mapView, IMapDelegate mapDelegate) {
//        mMapView = mapView;
//        aMap = mapDelegate;
//
//        if (null == mapDelegate) return;
//
//        aMap.setOnCameraChangeListener(new OnCameraChangeListener() {
//            @Override
//            public void onCameraChanged(ILatLng latLng, float zoom, double rotate) {
////                if (mMapScaleView != null) {
////                    ICameraPosition cameraPosition = aMap.getCameraPosition();
////                    mMapScaleView.update((float) cameraPosition.zoom + 1, cameraPosition.target.latitude);
////                }
//                if (compassView != null) {
//                    compassView.setAngle((float) rotate);
//                }
////                if(planeMarker!=null&&lastRotate!=rotate&&!followNose){
////                    planeMarker.setRotate((float) (planeMarker.getRotate()+rotate));
////                }
//                lastRotate = rotate;
//
//                //calculationMarker(zoom);
//
//             /*   if (zoom>=-9){
//                    isShowArea = true;
//                    //显示
//                    for (String key : planeMarkerMap.keySet()) {
//                        if (planeMarkerMap.get(key)!=null){
//                          // planeMarkerMap.get(key).;
//
//                        }
//
//                    }
//                    for (String key : flightMarkerMap.keySet()) {
//                        if (flightMarkerMap.get(key)!=null){
//
//                        }
//
//                    }
//
//
//                }else{
//                    isShowArea = false;
//                    for (String key : planeMarkerMap.keySet()) {
//                        if (planeMarkerMap.get(key)!=null){
//
//                        }
//                    }
//                    for (String key : flightMarkerMap.keySet()) {
//                        if (flightMarkerMap.get(key)!=null){
//
//                        }
//                    }
//                }*/
//            }
//
//            @Override
//            public void onCameraChangedFinish(ILatLng latLng, float zoom, double rotate) {
//
//            }
//
//        });
//        aMap.getUiSettings().setRotateGesturesEnabled(true);
//        aMap.getUiSettings().setScaleControlsEnabled(true);
//        mMapView.getSelfView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                drawNoflyZone();
//                drawNoflyWhiteListZone();
//            }
//        }, 2000);
//
//        aMap.setOnMarkerClickListener(new OnMapMarkerClickListener() {
//            @Override
//            public boolean onMapMarkerClick(IMarkerDelegate iMarkerDelegate) {
//
//                float z = (float) aMap.getCameraPosition().zoom;
//                mapDelegate.moveCamera(ICameraUpdateFactory.newLatLngZoom(iMarkerDelegate.getPosition(),
//                        (z * -1) + 2));
//                EventBus.getDefault().post(new ZoomLevel((z * -1) + 2));
//                return true;
//            }
//        });
//
//    }
//
//
//    public BaseMapFeature() {
//    }
//
//
//    public IMapDelegate getMap() {
//        return aMap;
//    }
//
//    public void showMapType(int type) {
//        if (type == MAP_NOMAL) {
//            aMap.setMapType(MapType.MAP_TYPE_NORMAL);
//        } else if (type == MAP_SATELLITE) {
//            aMap.setMapType(MapType.MAP_TYPE_SATELLITE);
//        }
//
//    }
//
//    protected Context getContext() {
//        return mMapView.getContext();
//    }
//
//    public void zoomOut() {
//        aMap.moveCamera(ICameraUpdateFactory.zoomOut());
//
//    }
//
//    public void zoomIn() {
//        aMap.moveCamera(ICameraUpdateFactory.zoomIn());
//    }
//
////    public void setScaleView(MapScaleView mapScaleView) {
////        mMapScaleView = mapScaleView;
////        ICameraPosition cameraPosition = aMap.getCameraPosition();
////// need to pass zoom and latitude
////        mMapScaleView.update((float) cameraPosition.zoom, cameraPosition.target.latitude);
////    }
//
//    protected boolean followNose;
//
//    public void setCompassView(final CompassView compassView) {
//        if (null != this.compassView) {
//            this.compassView.setOnClickListener(null);
//        }
//
//        this.compassView = compassView;
//
//        if (null != this.compassView) {
//            compassView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (compassView.getAngle() == 0 || compassView.getAngle() == 360) {
//                        //地图跟随机头朝向
//                        followNose = !followNose;
//                    } else {
//                        followNose = false;
//                        rotateMap(null, 0, true);
//                    }
//                    compassView.setFollowNose(followNose);
//                }
//            });
//        }
//    }
//
//    public void showPerson(BasePoint point) {
//        if (!isShowPerson) {
//            return;
//        }
//        personLatLng = point.getLatLngForMap();
//        if (personMarker == null) {
//            if (null == aMap) return;
//
//            IMarkerOptions markerOptions = new IMarkerOptions();
//            markerOptions.position(personLatLng);
//            View personView = View.inflate(mMapView.getContext(), R.layout.view_person_point, null);
//            markerOptions.icon(IBitmapDescriptorFactory.fromView(personView));
//            markerOptions.draggable(false);
//            markerOptions.anchor(0.5f, 0.5f);
//            markerOptions.zIndex(Z_INDEX_PERSON);
//            personMarker = aMap.addMarker(markerOptions);
//        } else {
//            personMarker.setPosition(personLatLng);
//        }
//    }
//
//    boolean isShowPerson = true;
//
//    public void setShowPerson(boolean isShowPerson) {
//        this.isShowPerson = isShowPerson;
//    }
//
//    private IMarkerDelegate externalGpsMarker;
//
//    boolean isShowExternalGps = false;
//
//    public void setShowExternalGps(boolean isShowExternalGps) {
//        this.isShowExternalGps = isShowExternalGps;
//    }
//
//    public void showExternalGps(BasePoint point, int layoutIdPoint) {
//        if (!isShowExternalGps) {
//            return;
//        }
//        if (externalGpsMarker == null) {
//            IMarkerOptions markerOptions = new IMarkerOptions();
//            markerOptions.position(point.getLatLngForMap());
//            View personView = View.inflate(mMapView.getContext(), layoutIdPoint, null);
//            markerOptions.icon(IBitmapDescriptorFactory.fromView(personView));
//            markerOptions.draggable(false);
//            markerOptions.anchor(0.5f, 0.5f);
//            markerOptions.zIndex(Z_INDEX_GPS);
//            externalGpsMarker = aMap.addMarker(markerOptions);
//            TmarkLocation = point.getLatLngForMap();
//        } else {
//            externalGpsMarker.setPosition(point.getLatLngForMap());
//            TmarkLocation = point.getLatLngForMap();
//        }
//
//    }
//
//    public void moveToExternalGps() {
//        if (externalGpsMarker != null) {
//            ILatLng latLng = externalGpsMarker.getPosition();
//            moveToPoint(latLng, 18);
//        } else {
//        }
//    }
//
//    public void moveToTmark() {
//        if (TmarkLocation != null) {
//            moveToPoint(TmarkLocation, 18);
//        }
//    }
//
//
//    public void hideExternalGps() {
//        if (externalGpsMarker != null) {
//            externalGpsMarker.remove();
//            externalGpsMarker = null;
//        }
//    }
//
//    float planeAngle;
//    View planeView;
//
////    protected Handler mainUI = new Handler(Looper.getMainLooper());
//
//    public void showPlane(BasePoint point, float angle) {
////        mainUI.post(new Runnable() {
////            @Override
////            public void run() {
//                if (angle < 0 || angle > 360) {
//                    return;
//                }
//
//                if (!isShowPlane) {
//                    return;
//                }
//                if (planeView == null) {
//                    planeView = View.inflate(mMapView.getContext(), R.layout.view_plane_marker, null);
//                }
//                planeAngle = angle;
//                double rotate = angle;
//
////        planeIv.setRotation(angle);
//                if (planeMarker == null) {
//                    IMarkerOptions markerOptions = new IMarkerOptions();
//                    markerOptions.position(point.getLatLngForMap());
//                    markerOptions.draggable(false);
//                    markerOptions.anchor(0.5f, 0.5f);
//                    markerOptions.zIndex(Z_INDEX_PLANE);
//                    markerOptions.icon(IBitmapDescriptorFactory.fromView(planeView));
//                    if (!followNose) {
//                        markerOptions.setRotate((float) rotate);
//                    }
//                    planeMarker = aMap.addMarker(markerOptions);
//                } else {
////            planeMarker.setIcon(planeView);
//                    planeMarker.setPosition(point.getLatLngForMap());
//                    if (!followNose) {
//                        rotate = getMap().getCameraPosition().bearing + angle;
//                        planeMarker.setRotate((float) rotate);
//                    } else {
//                        planeMarker.setRotate(0);
//                        rotateMap(point.getLatLngForMap(), 360 - angle, true);
//                    }
//
//                }
//
//                drawPathLine(point);
////            }
////        });
//    }
//
//
//    protected boolean isShowPathLine() {
//        return isDrawPathLine;
//    }
//
//    ILatLng curLatLng = new ILatLng(0, 0);
//    List<ILatLng> pathPintList = new ArrayList<>();
//
//    private void drawPathLine(BasePoint point) {
//        if (!isShowPathLine() || point.isZeroLatLng()) {
//            return;
//        }
//        ILatLng latLng = point.getLatLngForMap();
//        if (System.currentTimeMillis() - lastUpdateTime > 400 && !point.isZeroLatLng() && !latLng.equals(curLatLng)) {
//            TimeLogUtil.start();
//            pathPintList.add(point.getLatLngForMap());
//            lastUpdateTime = System.currentTimeMillis();
//            curLatLng = point.getLatLngForMap();
//            if (pathLine == null) {
//                IPolylineOptions polylineOptions = new IPolylineOptions();
//                polylineOptions.color(getContext().getResources().getColor(R.color.color_path_line));
//                polylineOptions.setDottedLine(false);
//                polylineOptions.width(1.5f);
//                polylineOptions.zIndex(Z_INDEX_PATH_LINE);
//                polylineOptions.addAll(pathPintList);
//                pathLine = aMap.addPolyline(polylineOptions);
//            } else {
//                pathLine.add(latLng);
//            }
//            TimeLogUtil.end("drawPathLine-");
//        }
//
//
//    }
//
//    public void clearPathLine() {
//        pathPintList.clear();
//        if (pathLine != null) {
//            pathLine.setPoints(new ArrayList<ILatLng>());
//            pathLine.remove();
//            pathLine = null;
//        }
//    }
//
//    public void clearRouteLine(int index) {
//        pathPintList.clear();
//        if (pathLine != null) {
//            pathLine.setPoints(new ArrayList<ILatLng>());
//            pathLine.remove();
//            pathLine = null;
//        }
//
//    }
//
//    public void drawPathLine(List<BasePoint> basePoints) {
//        if (!isShowPathLine() || basePoints == null || basePoints.size() == 0) {
//            return;
//        }
//        List<ILatLng> latLngs = new ArrayList<>();
//        for (BasePoint basePoint : basePoints) {
//            if (!basePoint.isZeroLatLng()) {
//                latLngs.add(basePoint.getLatLngForMap());
//            }
//        }
//
//
//        if (pathLine == null) {
//            IPolylineOptions polylineOptions = new IPolylineOptions();
//            polylineOptions.color(getContext().getResources().getColor(R.color.color_path_line));
//            polylineOptions.setDottedLine(false);
//            polylineOptions.width(1.5f);
//            polylineOptions.zIndex(Z_INDEX_PATH_LINE);
//            polylineOptions.addAll(latLngs);
//            pathLine = aMap.addPolyline(polylineOptions);
//        } else {
//            pathLine.setPoints(latLngs);
//            lastUpdateTime = System.currentTimeMillis();
//            if (latLngs.size() >= 1) {
//                curLatLng = latLngs.get(latLngs.size() - 1);
//            }
//
//        }
//
//    }
//
//
//    public void drawRouteLine(List<BasePoint> basePoints, String key, String color) {
//        if (routeMap.containsKey(key)) {
//            return;
//        }
//        if (!isShowPathLine() || basePoints == null || basePoints.size() == 0) {
//            return;
//        }
//        List<ILatLng> latLngs = new ArrayList<>();
//        for (BasePoint basePoint : basePoints) {
//            if (!basePoint.isZeroLatLng()) {
//                latLngs.add(basePoint.getLatLngForMap());
//            }
//        }
//        IPolylineOptions polylineOptions = new IPolylineOptions();
//
//        polylineOptions.color(Color.parseColor(color));
//        polylineOptions.setDottedLine(false);
//        polylineOptions.width(1.5f);
//        polylineOptions.zIndex(Z_INDEX_PATH_LINE);
//        polylineOptions.addAll(latLngs);
//
//        if (routeMap == null) {
//            routeMap = new HashMap();
//        }
//        pathLine = aMap.addPolyline(polylineOptions);
//        routeMap.put(key, pathLine);
//
//
//    }
//
//    /*public void drawPlaneLine(List <List<BasePoint>> basePointsList, String key, String color) {
//
//       if (planeRouteMap.containsKey(key)){
//           return;
//       }
//
//        for (List<BasePoint> basePoints : basePointsList) {
//            List<ILatLng> latLngs = new ArrayList<>();
//            for (BasePoint basePoint : basePoints) {
//                if (!basePoint.isZeroLatLng()) {
//                    latLngs.add(basePoint.getLatLngForMap());
//                }
//            }
//            IPolylineOptions polylineOptions = new IPolylineOptions();
//            polylineOptions.color(Color.parseColor(color));
//            polylineOptions.setDottedLine(false);
//            polylineOptions.width(1.5f);
//            polylineOptions.zIndex(Z_INDEX_PATH_LINE);
//            polylineOptions.addAll(latLngs);
//
//            if (planeRouteMap == null) {
//                planeRouteMap = new HashMap();
//            }
//            pathLine = aMap.addPolyline(polylineOptions);
//            if (planeRouteMap.containsKey(key)) {
//
//                planeRouteMap.get(key).add(pathLine);
//            } else {
//                List<IPolylineDelegate> list = new ArrayList<>();
//                list.add(pathLine);
//
//                planeRouteMap.put(key, list);
//
//            }
//
//
//        }
//
//
//    }*/
//
//    public void drawPlaneLine2(List<BasePoint> basePointsList, String key, String index, String color) {
//
//        if (planeRouteMap.containsKey(key) && planeRouteMap.get(key).containsKey(index)) {
//            return;
//        }
//
//        List<ILatLng> latLngs = new ArrayList<>();
//        for (BasePoint basePoint : basePointsList) {
//            if (!basePoint.isZeroLatLng()) {
//                latLngs.add(basePoint.getLatLngForMap());
//            }
//        }
//        IPolylineOptions polylineOptions = new IPolylineOptions();
//        polylineOptions.color(Color.parseColor(color));
//        polylineOptions.setDottedLine(false);
//        polylineOptions.width(1.5f);
//        polylineOptions.zIndex(Z_INDEX_PATH_LINE);
//        polylineOptions.addAll(latLngs);
//
//        if (planeRouteMap == null) {
//            planeRouteMap = new HashMap();
//        }
//        pathLine = aMap.addPolyline(polylineOptions);
//        if (planeRouteMap.containsKey(key)) {
//            if (planeRouteMap.get(key).containsKey(index)) {
//                return;
//            } else {
//                planeRouteMap.get(key).put(index, pathLine);
//            }
//        } else {
//            HashMap<String, IPolylineDelegate> map = new HashMap<>();
//            map.put(index, pathLine);
//            planeRouteMap.put(key, map);
//
//        }
//
//
//    }
//
//    public void drawAreaMarker(ILatLng latLng, String key, float area) {
//        /*if (areaMarkerMap.containsKey(key)){
//            return;
//        }*/
//        TextView areaTv = new TextView(getContext());
//      /*  areaTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("catfish","点击了我");
//            }
//        });*/
//        int tPading = (int) DensityUtil.dp2px(getContext(), 6);
//        int hPading = (int) DensityUtil.dp2px(getContext(), 11);
//        int vPading = (int) DensityUtil.dp2px(getContext(), 1);
//
//        areaTv.setPadding(hPading, tPading, hPading, vPading);
//        areaTv.setBackgroundResource(R.drawable.icon_area);
//        areaTv.setTextColor(getContext().getResources().getColor(R.color.text_area_color));
//        ILatLngBounds.Builder builder = new ILatLngBounds.Builder();
//
//
//
//
//       /* BasePoint basePoint = new BasePoint();
//        basePoint.initPointer(latLng.latitude, latLng.longitude, BasePoint.GEO_TYPE_WGS);
//        latLng =basePoint.getLatLngForMap();*/
//
//        //  if (!basePoint.isZeroLatLng()) {
//        //  ILatLng latLng =   basePoint.getLatLngForMap();
//        //    }
//        builder.include(latLng);
//        ILatLng centerLatLng = IMapUtils.calPolygonCenterPoint(builder.getLatLngList());
//        // areaFormat = com.topxgun.commonres.utils.CommonUtil.getAreaFormat(getContext(), area);
//        areaTv.setText(CommonUtil.getAreaFormat2(getContext(), area));
//        areaTv.setTextColor(Color.WHITE);
//        areaTv.setTextSize(10);
//        IMarkerOptions markerOptions = new IMarkerOptions();
//        markerOptions.position(centerLatLng);
//        markerOptions.icon(IBitmapDescriptorFactory.fromView(areaTv));
//        markerOptions.anchor(0.5f, 0.5f);
//        markerOptions.draggable(false);
//        markerOptions.visible(true);
//
//        markerOptions.zIndex(Z_INDEX_HOME);
//        IMarkerDelegate zoneAreaMarker = aMap.addMarker(markerOptions);
//
//        zoneAreaMarker.isVisible();
//        //  zoneAreaMarker.remove();
//
//        areaMarkerMap.put(key, zoneAreaMarker);
//
//    }
//
//
//    public void cleanAreaMarker() {
//
//
//        for (String s : areaMarkerMap.keySet()) {
//            areaMarkerMap.get(s).remove();
//
//        }
//        areaMarkerMap.clear();
//
//    }
//
//
//    public ILatLng getCenterLatlng(List<BasePoint> basePoints) {
//        ILatLngBounds.Builder builder = new ILatLngBounds.Builder();
//        List<ILatLng> latLngs = new ArrayList<>();
//        for (BasePoint basePoint : basePoints) {
//            if (!basePoint.isZeroLatLng()) {
//                latLngs.add(basePoint.getLatLngForMap());
//            }
//        }
//        for (ILatLng iLatLng : latLngs) {
//            builder.include(iLatLng);
//        }
//        ILatLng centerLatLng = IMapUtils.calPolygonCenterPoint(builder.getLatLngList());
//        return centerLatLng;
//    }
//
//
//    public void drawFlightMarker(List<BasePoint> basePoints, String key, float area) {
//        TextView areaTv = new TextView(getContext());
//        int hPading = (int) DensityUtil.dp2px(getContext(), 11);
//        int vPading = (int) DensityUtil.dp2px(getContext(), 2);
//        areaTv.setPadding(hPading, vPading, hPading, vPading);
//        areaTv.setBackgroundResource(R.drawable.bg_zone_area);
//        areaTv.setTextColor(getContext().getResources().getColor(R.color.text_area_color));
//        ILatLngBounds.Builder builder = new ILatLngBounds.Builder();
//
//        List<ILatLng> latLngs = new ArrayList<>();
//        for (BasePoint basePoint : basePoints) {
//            if (!basePoint.isZeroLatLng()) {
//                latLngs.add(basePoint.getLatLngForMap());
//            }
//        }
//        for (ILatLng iLatLng : latLngs) {
//            builder.include(iLatLng);
//        }
//        ILatLng centerLatLng = IMapUtils.calPolygonCenterPoint(builder.getLatLngList());
//        // areaFormat = com.topxgun.commonres.utils.CommonUtil.getAreaFormat(getContext(), area);
//        areaTv.setText(CommonUtil.getAreaFormat(getContext(), area));
//        areaTv.setTextSize(10);
//        IMarkerOptions markerOptions = new IMarkerOptions();
//        markerOptions.position(centerLatLng);
//        markerOptions.icon(IBitmapDescriptorFactory.fromView(areaTv));
//        markerOptions.anchor(0.5f, 0.5f);
//        markerOptions.draggable(false);
//        markerOptions.visible(true);
//        markerOptions.zIndex(Z_INDEX_HOME);
//        IMarkerDelegate zoneAreaMarker = aMap.addMarker(markerOptions);
//        flightMarkerMap.put(key, zoneAreaMarker);
//
//    }
//
//    public void deletePlaneLine(String key) {
//        for (String k : planeRouteMap.keySet()) {
//            if (k.equals(key)) {
//                HashMap<String, IPolylineDelegate> map = planeRouteMap.get(k);
//                for (String index : map.keySet()) {
//                    IPolylineDelegate line = map.get(index);
//                    if (line != null) {
//                        line.setPoints(new ArrayList<ILatLng>());
//                        line.remove();
//                        line = null;
//                    }
//                }
//
//
//            }
//        }
//        planeRouteMap.remove(key);
//    }
//
//
//    public void deleteAllPlaneLine() {
//
//
//        try {
//            for (String k : planeRouteMap.keySet()) {
//
//                HashMap<String, IPolylineDelegate> map = planeRouteMap.get(k);
//                for (String index : map.keySet()) {
//                    IPolylineDelegate line = map.get(index);
//                    if (line != null) {
//                        line.setPoints(new ArrayList<ILatLng>());
//                        line.remove();
//                        line = null;
//                    }
//                }
//            }
//            planeRouteMap.clear();
//        } catch (Exception e) {
//            deleteAllPlaneLine();
//        }
//    }
//
//
//    public void deleteAllFlightAreaMarker() {
//        for (String s : flightMarkerMap.keySet()) {
//            flightMarkerMap.get(s).remove();
//        }
//
//        flightMarkerMap.clear();
//    }
//
//    public void deleteAllPlaneAreaMarker() {
//        for (String s : areaMarkerMap.keySet()) {
//            areaMarkerMap.get(s).remove();
//        }
//        areaMarkerMap.clear();
//    }
//
//
//    public void deletePlaneMarker(String key) {
//        areaMarkerMap.get(key).remove();
//        areaMarkerMap.remove(key);
//    }
//
//    public void deleteRouteLine(String key) {
//        //  pathPintList.landClear();
//
//        IPolylineDelegate line = routeMap.get(key);
//        routeMap.remove(key);
//        if (line != null) {
//            line.setPoints(new ArrayList<ILatLng>());
//            line.remove();
//            line = null;
//        }
//    }
//
//    public void deleteAllRouteLine() {
//
//        try {
//            if (pathPintList != null) {
//                pathPintList.clear();
//            }
//            if (routeMap != null) {
//                for (String key : routeMap.keySet()) {
//                    IPolylineDelegate line = routeMap.get(key);
//                    if (line != null) {
//                        line.setPoints(new ArrayList<ILatLng>());
//                        line.remove();
//                        line = null;
//                    }
//                }
//            }
//            if (routeMap != null) {
//                routeMap.clear();
//            }
//        } catch (Exception e) {
//            deleteAllRouteLine();
//        }
//
//    }
//
//
//    public void drawNoflyZone() {
//        List<AreaLimit> areaLimitList = AreaLimitManager.getInstance().getAreaLimitList();
//        if (areaLimitList == null) {
//            return;
//        }
//        for (IOverlayDelegate polygonDelegate : noflyZoneList) {
//            polygonDelegate.remove();
//        }
//        noflyZoneList.clear();
//        for (AreaLimit areaLimit : areaLimitList) {
//            if (areaLimit.coordinates != null && areaLimit.coordinates.size() > 2) {
//                IPolygonOptions polygonOptions = new IPolygonOptions();
//                polygonOptions.fillColor(Color.parseColor("#55FF4646"));
//                polygonOptions.strokeWidth(1);
//                polygonOptions.zIndex(Z_INDEX_AREA_LIMIT);
//                polygonOptions.strokeColor(Color.parseColor("#FF4646"));
//                for (AreaLimit.Coordinate coordinate : areaLimit.coordinates) {
//                    BasePoint basePoint = new BasePoint();
//                    basePoint.initPointer(coordinate.y, coordinate.x, BasePoint.GEO_TYPE_WGS);
//                    polygonOptions.add(basePoint.getLatLngForMap());
//                }
//                IPolygonDelegate polygonDelegate = aMap.addPolygon(polygonOptions);
//                noflyZoneList.add(polygonDelegate);
//            }
////            if(areaLimit.type==0&&areaLimit.src!=null){
////                ICircleOptions circleOptions = new ICircleOptions();
////                circleOptions.radius((float) areaLimit.src.radius1);
////                circleOptions.strokeColor(Color.parseColor("#FF4646"));
////                circleOptions.fillColor(Color.parseColor("#55FF4646"));
////                ILatLng aIatLng = new ILatLng(areaLimit.src.B4[1],areaLimit.src.B4[0]);
////                MercatorPoint mercatorPointA = MercatorProjection.toMercatorPoint(aIatLng);
////                Point a = new Point(mercatorPointA.x,mercatorPointA.y);
////                ILatLng bIatLng = new ILatLng(areaLimit.src.C4[1],areaLimit.src.C4[0]);
////                MercatorPoint mercatorPointB = MercatorProjection.toMercatorPoint(bIatLng);
////                Point b = new Point(mercatorPointB.x,mercatorPointB.y);
////                List<ILatLng> latLngList = new ArrayList<>();
////                latLngList.add(aIatLng);
////                latLngList.add(bIatLng);
////                Point center = getCircleCenter(a,b,MercatorProjection.getMercatorDistance(latLngList,areaLimit.src.radius1));
////                ILatLng latLng = MercatorProjection.toLatLng(new MercatorPoint(center.x,center.y));
////                circleOptions.center(latLng);
////                aMap.addCircle(circleOptions);
////
////                IPolylineOptions polylineOptions = new IPolylineOptions();
////                polylineOptions.add(aIatLng);
////                polylineOptions.add(bIatLng);
////                polylineOptions.color(Color.parseColor("#FF4646"));
////                aMap.addPolyline(polylineOptions);
////
////            }
//        }
//
//    }
//
//
//    public void drawNoflyWhiteListZone() {
//        TXGWhiteListZone areaLimitWhiteList = AreaLimitManager.getInstance().getAreaLimitWhiteList();
//        if (areaLimitWhiteList == null) {
//            return;
//        }
//        if (!TXGSdkManagerApollo.getInstance().hasConnected()) {
//            clearNoflyWhitelistPoints();
//            return;
//        }
//        clearNoflyWhitelistPoints();
//        for (TXGWhiteListZone.WhiteListZonePolygon whiteListZonePolygon : areaLimitWhiteList.getWhiteListZonePolygonList()) {
//            if (whiteListZonePolygon.pointList != null && whiteListZonePolygon.pointList.size() > 2) {
//                IPolygonOptions polygonOptions = new IPolygonOptions();
//                polygonOptions.fillColor(Color.parseColor("#5500FF00"));
//                polygonOptions.strokeWidth(1);
//                polygonOptions.zIndex(Z_INDEX_AREA_LIMIT);
//                polygonOptions.strokeColor(Color.parseColor("#FF00FF00"));
//                for (TXGGeoPoint coordinate : whiteListZonePolygon.pointList) {
//                    BasePoint basePoint = new BasePoint();
//                    basePoint.initPointer(coordinate.getLat(), coordinate.getLon(), BasePoint.GEO_TYPE_WGS);
//                    polygonOptions.add(basePoint.getLatLngForMap());
//                }
//                IPolygonDelegate polygonDelegate = aMap.addPolygon(polygonOptions);
//                noflyWhiteZoneList.add(polygonDelegate);
//            }
//        }
//        for (TXGWhiteListZone.WhiteListZoneCircle whiteListZoneCircle : areaLimitWhiteList.getWhiteListCircleList()) {
//            if (whiteListZoneCircle.center != null) {
//                ICircleOptions circleOptions = new ICircleOptions();
//                circleOptions.fillColor(Color.parseColor("#5500FF00"));
//                circleOptions.strokeWidth(1);
//                circleOptions.zIndex(Z_INDEX_AREA_LIMIT);
//                circleOptions.strokeColor(Color.parseColor("#FF00FF00"));
//                BasePoint basePoint = new BasePoint();
//                basePoint.initPointer(whiteListZoneCircle.center.getLat(), whiteListZoneCircle.center.getLon(), BasePoint.GEO_TYPE_WGS);
//                circleOptions.center(basePoint.getLatLngForMap());
//                circleOptions.radius(whiteListZoneCircle.radius);
//                ICircleDelegate iCircleDelegate = aMap.addCircle(circleOptions);
//                noflyWhiteZoneList.add(iCircleDelegate);
//            }
//        }
//    }
//
//    public void clearNoflyWhitelistPoints() {
//        for (IOverlayDelegate polygonDelegate : noflyWhiteZoneList) {
//            polygonDelegate.remove();
//        }
//        noflyWhiteZoneList.clear();
//    }
//
//    private Point getCircleCenter(Point a, Point b, double radius) {
//        double x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y, R = radius;
//        double c1 = (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1) / (2 * (x2 - x1));
//        double c2 = (y2 - y1) / (x2 - x1);
//        double A = (c2 * c2 + 1);
//        double B = (2 * x1 * c2 - 2 * c1 * c2 - 2 * y1);
//        double C = x1 * x1 - 2 * x1 * c1 + c1 * c1 + y1 * y1 - R * R;
//        double y = (-B + Math.sqrt(B * B - 4 * A * C)) / (2 * A);
//        double x = c1 - c2 * y;
//        return new Point(x, y);
//
//    }
//
//    public void removePlane() {
//        if (planeMarker != null) {
//            planeMarker.remove();
//            planeMarker = null;
//        }
//    }
//
//    public void setDrawPathLine(boolean drawPathLine) {
//        isDrawPathLine = drawPathLine;
//        if (!drawPathLine) {
//            clearPathLine();
//        }
//    }
//
//    public void moveToPlane() {
//        if (planeMarker != null) {
//            moveToPoint(planeMarker.getPosition(), 18);
//        }
//    }
//
//    public void moveToPlane(float zoom) {
//        if (planeMarker != null) {
//            moveToPoint(planeMarker.getPosition(), zoom);
//        }
//    }
//
//
//    public void setShowPlane(boolean b) {
//        isShowPlane = b;
//    }
//
//    public void hidePlane() {
//        removePlane();
//        isShowPlane = false;
//    }
//
//
//    public void removePerson() {
//        if (personMarker != null) {
//            personMarker.remove();
//            personMarker = null;
//        }
//        destroyLocation();
//    }
//
//    public void moveToPerson() {
//        if (externalGpsMarker != null) {
//            moveToExternalGps();
//        } else if (personMarker != null) {
//            ILatLng latLng = personMarker.getPosition();
//            moveToPoint(latLng, 18);
//        } else {
//            startLocation(true);
//        }
//    }
//
//    public ILatLng getPersonLocation() {
//        if (externalGpsMarker != null) {
//            return externalGpsMarker.getPosition();
//        } else if (personMarker != null) {
//            return personMarker.getPosition();
//        } else if (personLatLng != null) {
//            return personLatLng;
//        } else {
//            startLocation(true);
//        }
//
//        return null;
//    }
//
//    public void moveToPerson2() {
//        if (null == personMarker && null != currentLocation) {
//            BasePoint point = new BasePoint();
//            point.initPointer(currentLocation.getLatitude(), currentLocation.getLongitude(), BasePoint.GEO_TYPE_WGS);
//            showPerson(point);
//        } else if (personMarker != null) {
//            ILatLng latLng = personMarker.getPosition();
//            moveToPoint(latLng, 18);
//        } else {
//            startLocation(true);
//        }
//    }
//
//    public void moveToPoint(final ILatLng latLng, final float zoomLevel) {
//
//        if (aMap == null) {
//            return;
//        }
//        mMapView.getSelfView().post(new Runnable() {
//            @Override
//            public void run() {
//                aMap.moveCamera(
//                        ICameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//            }
//        });
//    }
//
//    public void moveRTKPoint() {
//        if (aMap == null) {
//            return;
//        }
//
//        if (externalGpsMarker == null) {
//            return;
//        }
//
//        mMapView.getSelfView().post(new Runnable() {
//            @Override
//            public void run() {
//                aMap.moveCamera(
//                        ICameraUpdateFactory.newLatLngZoom(externalGpsMarker.getPosition(), 18));
//            }
//        });
//    }
//
//    public void rotateMap(final ILatLng centerPoint, final float angle, final boolean anim) {
//        if (aMap == null) {
//            return;
//        }
//        mMapView.getSelfView().post(new Runnable() {
//            @Override
//            public void run() {
//                if (anim) {
//                    aMap.animateCamera(
//                            ICameraUpdateFactory.rotateChange(centerPoint, angle));
//                } else {
//                    aMap.moveCamera(
//                            ICameraUpdateFactory.rotateChange(centerPoint, angle));
//                }
//
//            }
//        });
//    }
//
//    public void moveToPoint(final ILatLng latLng) {
//        if (aMap == null) {
//            return;
//        }
//        mMapView.getSelfView().post(new Runnable() {
//            @Override
//            public void run() {
//                aMap.moveCamera(ICameraUpdateFactory.newLatLngZoom(latLng,
//                        (float) aMap.getCameraPosition().zoom));
//            }
//        });
//
//    }
//
//    public void moveToPointList(final List<ILatLng> latLngList, final int padding) {
//        if (aMap == null || latLngList == null || latLngList.size() == 0) {
//            return;
//        }
//        ILatLngBounds.Builder builder = new ILatLngBounds.Builder();
//        ICameraUpdate iCameraUpdate = ICameraUpdateFactory.newLatLngBounds(builder.includes(latLngList).build(), padding);
//        aMap.moveCamera(iCameraUpdate);
//    }
//
//    public void moveToPointList(final List<ILatLng> latLngList, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
//        if (aMap == null || latLngList == null || latLngList.size() == 0) {
//            return;
//        }
//        ILatLngBounds.Builder builder = new ILatLngBounds.Builder();
//        ICameraUpdate iCameraUpdate = ICameraUpdateFactory.newLatLngBounds(builder.includes(latLngList).build(),
//                paddingLeft, paddingTop, paddingRight, paddingBottom);
//        aMap.moveCamera(iCameraUpdate);
//    }
//
//    public void startLocation(boolean mode) {
//        /*
//        mLocationClient = new AMapLocationClient(getContext());
//        mLocationClient.setLocationListener(this);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(false);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        if (!mLocationClient.isStarted()) {
//            mLocationClient.startLocation();
//        }
//        */
//        onStart();
//        focusPerson(mode);
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                //可在其中解析amapLocation获取相应内容。
//                BasePoint point = new BasePoint();
//                point.initPointer(aMapLocation.getLatitude(), aMapLocation.getLongitude(),
//                        BasePoint.GEO_TYPE_GCJ);
//                showPerson(point);
//                if (isFirstLocation && isFocusToLocation()) {
//                    moveToPoint(point.getLatLngForMap(), 16);
//                }
//                isFirstLocation = false;
//            } else {
//                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
////                LogUtils.e("AmapError", "location Error, ErrCode:"
////                        + aMapLocation.getErrorCode() + ", errInfo:"
////                        + aMapLocation.getErrorInfo());
//            }
//        }
//    }
//
//    protected boolean isFocusToLocation() {
//        return isFocusToLocation;
//    }
//
//    public void setFocusToLocation(boolean focusToLocation) {
//        isFocusToLocation = focusToLocation;
//    }
//
//    public void moveToFocus() {
//    }
//
//    public void destroyLocation() {
//        /*
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//            mLocationClient = null;
//        }
//        */
//        onStop();
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * 初始化函数
//     */
//    public boolean onStart() {
//        locationManager = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(false);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);
//        criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);
//
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (provider != null && 0 == LocationManager.GPS_PROVIDER.compareTo(provider)) {
//            gpsProvider = locationManager.getProvider(provider);
//            networkProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
//        } else if (provider != null && 0 == LocationManager.NETWORK_PROVIDER.compareTo(provider)) {
//            networkProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
//        }
//
//        //未打开任何一种定位方式
//        if (null == gpsProvider && null == networkProvider) {
//            return false;
//        }
//
//        //启动定位
//        if (ActivityCompat.checkSelfPermission(mMapView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(mMapView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return false;
//        }
//        if (null != networkProvider && networkListener != null) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0.1f, networkListener);
//        }
//
//        if (null != gpsProvider && gpsListener != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.1f, gpsListener);
//        }
//
//        //开启百度定位
//        if (locationService == null) {
//            locationService = new LocationService(getContext().getApplicationContext());
//        }
//        locationService.registerListener(mBaiduListener);
//        locationService.start();// 定位SDK
//        //  Log.d("catfish","开启百度定位");
//        return true;
//    }
//
//    /**
//     * 停止定位
//     */
//    public void onStop() {
//        try {
//            if (null != locationManager && null != gpsProvider && gpsListener != null) {
//                locationManager.removeUpdates(gpsListener);
//                gpsListener = null;
//            }
//
//            if (null != locationManager && null != gpsProvider && networkListener != null) {
//                locationManager.removeUpdates(networkListener);
//                networkListener = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
//        if (currentBestLocation == null) {
//            return true;
//        }
//
//        long timeDelta = location.getTime() - currentBestLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
//        boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
//        boolean isNewer = timeDelta > 0;
//
//        if (isSignificantlyNewer) {
//            return true;
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                currentBestLocation.getProvider());
//
//        if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//
//        return provider1.equals(provider2);
//    }
//
//    public void cleanFlightMarker(String key) {
//        for (String s : areaMarkerMap.keySet()) {
//            if (key.equals(s)) {
//                areaMarkerMap.get(s).remove();
//            }
//
//
//        }
//        areaMarkerMap.remove(key);
//    }
//
//    /**
//     * 位置监听器
//     */
//    public class AppLocationListener implements LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//
//            if (locationService.isStart()) {
//                locationService.stop();
//            }
//            currentLocation = location;
//            BasePoint point = new BasePoint();
//            point.initPointer(location.getLatitude(), location.getLongitude(), BasePoint.GEO_TYPE_WGS);
//            showPerson(point);
//            if (isFirstLocation) {
//                //移动人坐标至中心
//                if (focusPerson && isFocusToLocation) {
//                    moveToPoint(point.getLatLngForMap(), 18);
//                }
//                isFirstLocation = false;
//            }
//
//            EventBus.getDefault().post(currentLocation);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }
//
//
//
//
//
//
//    /*****
//     *
//     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
//     *
//     */
//    private BDAbstractLocationListener mBaiduListener = new BDAbstractLocationListener() {
//
//        /**
//         * 定位请求回调函数
//         * @param location 定位结果
//         */
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // TODO Auto-generated method stub
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//            //    Log.d("catfishgps", "百度" + location.getAddrStr() + "：" + location.getLongitude() + ":" + location.getLatitude());
//                BasePoint point = new BasePoint();
//                //31.8271934：118.7739121
//
//                point.initPointer(location.getLatitude(), location.getLongitude(), BasePoint.GEO_TYPE_GCJ);
//                showPerson(point);
//                if (isFirstLocation) {
//                    //移动人坐标至中心
//                    if (focusPerson && isFocusToLocation) {
//                        moveToPoint(point.getLatLngForMap(), 18);
//                    }
//                    isFirstLocation = false;
//                }
//            }
//
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//            super.onConnectHotSpotMessage(s, i);
//        }
//
//        /**
//         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
//         * @param locType 当前定位类型
//         * @param diagnosticType 诊断类型（1~9）
//         * @param diagnosticMessage 具体的诊断信息释义
//         */
//        @Override
//        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
//            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
//            int tag = 2;
//            StringBuffer sb = new StringBuffer(256);
//            sb.append(AppContext.getResString(R.string.diagnose_result));
//            if (locType == BDLocation.TypeNetWorkLocation) {
//                if (diagnosticType == 1) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_1));
//                    sb.append("\n" + diagnosticMessage);
//                } else if (diagnosticType == 2) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_2));
//                    sb.append("\n" + diagnosticMessage);
//                }
//            } else if (locType == BDLocation.TypeOffLineLocationFail) {
//                if (diagnosticType == 3) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_3));
//                    sb.append("\n" + diagnosticMessage);
//                }
//            } else if (locType == BDLocation.TypeCriteriaException) {
//                if (diagnosticType == 4) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_4));
//                    sb.append("\n" + diagnosticMessage);
//                } else if (diagnosticType == 5) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_5));
//                    sb.append(diagnosticMessage);
//                } else if (diagnosticType == 6) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_6));
//                    sb.append("\n" + diagnosticMessage);
//                } else if (diagnosticType == 7) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_7));
//                    sb.append("\n" + diagnosticMessage);
//                } else if (diagnosticType == 9) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_9));
//                    sb.append("\n" + diagnosticMessage);
//                }
//            } else if (locType == BDLocation.TypeServerError) {
//                if (diagnosticType == 8) {
//                    sb.append(AppContext.getResString(R.string.diagnose_result_8));
//                    sb.append("\n" + diagnosticMessage);
//                }
//            }
//            //logMsg(sb.toString(), tag);
//        }
//    };
//
//
//
//
//
//
//    /**
//     * 是否聚焦本人位置
//     *
//     * @param status
//     */
//    public void focusPerson(boolean status) {
//        focusPerson = status;
//    }
//
//    public void destory() {
//        onStop();
//        locationManager = null;
//        if (null != compassView) {
//            compassView.setOnClickListener(null);
//            compassView = null;
//        }
//    }
//}
