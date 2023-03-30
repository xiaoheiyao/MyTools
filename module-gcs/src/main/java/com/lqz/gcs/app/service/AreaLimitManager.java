//package com.lqz.gcs.app.service;
//
//import com.elvishew.xlog.XLog.Log;
//import com.google.gson.Gson;
//import com.topxgun.agservice.gcs.app.api.ApiFactory;
//import com.topxgun.agservice.gcs.app.event.EFenceGotEvent;
//import com.topxgun.agservice.gcs.app.model.AreaLimit;
//import com.topxgun.agservice.gcs.app.model.AreaLimitRequest;
//import com.topxgun.agservice.gcs.app.model.AreaLimitResult;
//import com.topxgun.agservice.gcs.app.model.AreaLimitWhiteList;
//import com.topxgun.agservice.gcs.app.model.AreaLimitWhiteListRequest;
//import com.topxgun.agservice.gcs.app.model.AreaLimitWhiteListResult;
//import com.topxgun.agservice.gcs.app.model.NoFlyWhiteListZoneGetEvent;
//import com.topxgun.agservice.gcs.app.model.NoFlyZoneGetEvent;
//import com.topxgun.agservice.gcs.app.util.CommonUtil;
//import com.topxgun.commonsdk.AppContext;
//import com.topxgun.commonsdk.connection.TXGSdkManagerApollo;
//import com.topxgun.commonsdk.connection.event.ClientConnectionFail;
//import com.topxgun.commonsdk.connection.event.ClientConnectionSuccess;
//import com.topxgun.commonsdk.http.ApiBaseResult;
//import com.topxgun.commonsdk.utils.ToastContext;
//import com.topxgun.open.api.base.AircraftConnection;
//import com.topxgun.open.api.base.ApiCallback;
//import com.topxgun.open.api.base.BaseResult;
//import com.topxgun.open.api.base.IFlightController;
//import com.topxgun.open.api.base.IFlyZoneManager;
//import com.topxgun.open.api.model.TXGResultCode;
//import com.topxgun.protocol.model.TXGGeoFence;
//import com.topxgun.protocol.model.TXGGeoPoint;
//import com.topxgun.protocol.model.TXGNoflyZone;
//import com.topxgun.protocol.model.TXGWhiteListZone;
//
//import org.simple.eventbus.EventBus;
//import org.simple.eventbus.Subscriber;
//import org.simple.eventbus.ThreadMode;
//
//import java.util.List;
//
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import retrofit2.HttpException;
//
///**
// * Created by TOPXGUN on 2017/7/17.
// */
//
//public class AreaLimitManager {
//    public final String TAG = AreaLimitManager.class.getSimpleName();
//    private static AreaLimitManager instance;
//
//    private boolean isFetching;
//
//    private boolean isWhiteListFetching;
//
//    private List<AreaLimit> areaLimitList;
//
//    private TXGWhiteListZone whiteList;
//
//    //电子围栏信息
//    private TXGGeoFence eFenceInfo;
//
//    private AreaLimitManager() {
//    }
//
//    public static AreaLimitManager getInstance() {
//        if (instance == null) {
//            instance = new AreaLimitManager();
//        }
//        return instance;
//    }
//
//    public void onStart() {
//        EventBus.getDefault().register(this);
//    }
//
//
//    public List<AreaLimit> getAreaLimitList() {
//        return areaLimitList;
//    }
//
//    public TXGWhiteListZone getAreaLimitWhiteList() {
//        return whiteList;
//    }
//
//    private void getAreaLimitFromServer(AreaLimitRequest areaLimitRequest) {
//        isFetching = true;
//        ApiFactory.getInstance().getAgriApi().getAreaLimit(areaLimitRequest)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(new Observer<ApiBaseResult<AreaLimitResult>>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        isFetching = false;
//                        Log.d(TAG, "NoflyServer:" + e != null ? "" : e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ApiBaseResult<AreaLimitResult> apiBaseResult) {
//                        if (apiBaseResult != null && apiBaseResult.data != null) {
//                            isFetching = false;
//                            areaLimitList = apiBaseResult.data.data;
//                            Log.d(TAG, "NoflyServer:" + new Gson().toJson(areaLimitList));
//                            EventBus.getDefault().post(new NoFlyZoneGetEvent());
//                            uploadAreaLimitToFcc();
//                        }
//                    }
//                });
//    }
//
//    public void uploadAreaLimitToFcc() {
//        if (areaLimitList != null && TXGSdkManagerApollo.getInstance().hasConnected()) {
//            TXGNoflyZone txgNoflyZone = new TXGNoflyZone();
//            for (AreaLimit areaLimit : areaLimitList) {
//                if (areaLimit.startDate == areaLimit.endDate) {
//                    areaLimit.startDate = 0;
//                    areaLimit.endDate = 0;
//                }
//                if (areaLimit.type == 0) {
//                    //机场禁飞区
//                    if (null != areaLimit.src) {
//                        TXGNoflyZone.NoFlyZoneAirport noFlyZoneAirport = new TXGNoflyZone.NoFlyZoneAirport();
//                        double[] a1 = areaLimit.src.A1;
//                        TXGGeoPoint a1Point = new TXGGeoPoint(a1[1], a1[0]);
//                        noFlyZoneAirport.A1 = a1Point;
//                        double[] a2 = areaLimit.src.A2;
//                        TXGGeoPoint a2Point = new TXGGeoPoint(a2[1], a2[0]);
//                        noFlyZoneAirport.A2 = a2Point;
//                        double[] a3 = areaLimit.src.A3;
//                        TXGGeoPoint a3Point = new TXGGeoPoint(a3[1], a3[0]);
//                        noFlyZoneAirport.A3 = a3Point;
//                        double[] a4 = areaLimit.src.A4;
//                        TXGGeoPoint a4Point = new TXGGeoPoint(a4[1], a4[0]);
//                        noFlyZoneAirport.A4 = a4Point;
//
//                        double[] b1 = areaLimit.src.B1;
//                        TXGGeoPoint b1Point = new TXGGeoPoint(b1[1], b1[0]);
//                        noFlyZoneAirport.B1 = b1Point;
//                        double[] b2 = areaLimit.src.B2;
//                        TXGGeoPoint b2Point = new TXGGeoPoint(b2[1], b2[0]);
//                        noFlyZoneAirport.B2 = b2Point;
//                        double[] b3 = areaLimit.src.B3;
//                        TXGGeoPoint b3Point = new TXGGeoPoint(b3[1], b3[0]);
//                        noFlyZoneAirport.B3 = b3Point;
//                        double[] b4 = areaLimit.src.B4;
//                        TXGGeoPoint b4Point = new TXGGeoPoint(b4[1], b4[0]);
//                        noFlyZoneAirport.B4 = b4Point;
//
//                        double[] c1 = areaLimit.src.C1;
//                        TXGGeoPoint c1Point = new TXGGeoPoint(c1[1], c1[0]);
//                        noFlyZoneAirport.C1 = c1Point;
//                        double[] c2 = areaLimit.src.C2;
//                        TXGGeoPoint c2Point = new TXGGeoPoint(c2[1], c2[0]);
//                        noFlyZoneAirport.C2 = c2Point;
//                        double[] c3 = areaLimit.src.C3;
//                        TXGGeoPoint c3Point = new TXGGeoPoint(c3[1], c3[0]);
//                        noFlyZoneAirport.C3 = c3Point;
//                        double[] c4 = areaLimit.src.C4;
//                        TXGGeoPoint c4Point = new TXGGeoPoint(c4[1], c4[0]);
//                        noFlyZoneAirport.C4 = c4Point;
//
//                        noFlyZoneAirport.radius1 = (float) areaLimit.src.radius1;
//                        noFlyZoneAirport.radius2 = (float) areaLimit.src.radius2;
//                        noFlyZoneAirport.radius3 = (float) areaLimit.src.radius3;
//                        noFlyZoneAirport.radius4 = (float) areaLimit.src.radius4;
//                        noFlyZoneAirport.startTime = areaLimit.startDate;
//                        noFlyZoneAirport.endTime = areaLimit.endDate;
//                        noFlyZoneAirport.id = areaLimit.id;
//                        txgNoflyZone.getNoflyZoneAirportList().add(noFlyZoneAirport);
//                    }
//                } else if (areaLimit.type == 3) {
//                    //多边形禁飞区
//                    TXGNoflyZone.NoflyZonePolygon noflyZonePolygon = new TXGNoflyZone.NoflyZonePolygon();
//                    for (double[] coor : areaLimit.poly.coordinates) {
//                        TXGGeoPoint geoPoint = new TXGGeoPoint(coor[1], coor[0]);
//                        noflyZonePolygon.pointList.add(geoPoint);
//                    }
//                    noflyZonePolygon.startTime = areaLimit.startDate;
//                    noflyZonePolygon.endTime = areaLimit.endDate;
//                    noflyZonePolygon.id = areaLimit.id;
//                    txgNoflyZone.getNoflyZonePolygonList().add(noflyZonePolygon);
//                } else if (areaLimit.type == 1) {
//                    //圆形
//                    TXGNoflyZone.NoflyZoneCircle noflyZoneCircle = new TXGNoflyZone.NoflyZoneCircle();
//                    noflyZoneCircle.radius = (float) (areaLimit.circle.radius * 1000);
//                    noflyZoneCircle.center = new TXGGeoPoint(areaLimit.circle.center[1], areaLimit.circle.center[0]);
//                    noflyZoneCircle.startTime = areaLimit.startDate;
//                    noflyZoneCircle.endTime = areaLimit.endDate;
//                    noflyZoneCircle.id = areaLimit.id;
//                    txgNoflyZone.getNoflyZoneCircleList().add(noflyZoneCircle);
//                } else if (areaLimit.type == 2) {
//                    //扇形
//                    TXGNoflyZone.NoflyZoneSector noflyZoneSector = new TXGNoflyZone.NoflyZoneSector();
//                    noflyZoneSector.startAngle = areaLimit.sector.bearing1;
//                    noflyZoneSector.endAngle = areaLimit.sector.bearing2;
//                    noflyZoneSector.bottomRadius = (float) (areaLimit.sector.radius * 1000);
//                    noflyZoneSector.topRadius = (float) (areaLimit.sector.radius * 1000);
//                    noflyZoneSector.bottomCenterPoint = new TXGGeoPoint(areaLimit.sector.center[1], areaLimit.sector.center[0]);
//                    noflyZoneSector.startTime = areaLimit.startDate;
//                    noflyZoneSector.endTime = areaLimit.endDate;
//                    noflyZoneSector.id = areaLimit.id;
//                    txgNoflyZone.getNoflyZoneSectorList().add(noflyZoneSector);
//                }
//            }
//            TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager().uploadNoflyZone(txgNoflyZone, new com.topxgun.open.api.base.ApiCallback() {
//                @Override
//                public void onResult(BaseResult result) {
//                    Log.d(TAG, "NoflyFCU：上传禁飞区成功");
//                }
//            });
//        }
//
//
//    }
//
//
//    private void getAreaLimitWhiteListFromServer(AreaLimitWhiteListRequest whiteListRequest) {
//        ApiFactory.getInstance().getAgriApi().getAreaLimitWhiteList(whiteListRequest)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(new Observer<ApiBaseResult<AreaLimitWhiteListResult>>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "WhiteListServer:" + e != null ? "" : e.getMessage());
//                        if (e instanceof HttpException && e != null && ((HttpException) e).code() == 404) {
//                            whiteList = new TXGWhiteListZone();
//                        }
//                        getAreaLimitWhiteListFromFcc();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ApiBaseResult<AreaLimitWhiteListResult> apiBaseResult) {
//                        if (apiBaseResult != null && apiBaseResult.data != null) {
//                            Log.d(TAG, "WhiteListServer:" + new Gson().toJson(apiBaseResult.data));
//                            uploadAreaLimitWhiteListToFcc(apiBaseResult.data.data);
//                        }
//                    }
//                });
//    }
//
//    public void uploadAreaLimitWhiteListToFcc(List<AreaLimitWhiteList> serverWhiteList) {
//        if (serverWhiteList != null && TXGSdkManagerApollo.getInstance().hasConnected()) {
//            TXGWhiteListZone whiteListZone = new TXGWhiteListZone();
//            for (AreaLimitWhiteList whiteListItem : serverWhiteList) {
//                if (whiteListItem.startDate == whiteListItem.endDate) {
//                    whiteListItem.startDate = 0;
//                    whiteListItem.endDate = 0;
//                }
//                if (whiteListItem.type == 3) {
//                    //多边形禁飞区
//                    TXGWhiteListZone.WhiteListZonePolygon whiteListZonePolygon = new TXGWhiteListZone.WhiteListZonePolygon();
//                    for (double[] coor : whiteListItem.poly.coordinates) {
//                        TXGGeoPoint geoPoint = new TXGGeoPoint(coor[1], coor[0]);
//                        whiteListZonePolygon.pointList.add(geoPoint);
//                    }
//                    whiteListZonePolygon.startTime = whiteListItem.startDate;
//                    whiteListZonePolygon.endTime = whiteListItem.endDate;
//                    whiteListZonePolygon.id = whiteListItem.id;
//                    whiteListZone.getWhiteListZonePolygonList().add(whiteListZonePolygon);
//                } else if (whiteListItem.type == 1) {
//                    //圆形
//                    TXGWhiteListZone.WhiteListZoneCircle whiteListZoneCircle = new TXGWhiteListZone.WhiteListZoneCircle();
//                    whiteListZoneCircle.radius = (float) (whiteListItem.circle.radius * 1000);
//                    whiteListZoneCircle.center = new TXGGeoPoint(whiteListItem.circle.center[1], whiteListItem.circle.center[0]);
//                    whiteListZoneCircle.startTime = whiteListItem.startDate;
//                    whiteListZoneCircle.endTime = whiteListItem.endDate;
//                    whiteListZoneCircle.id = whiteListItem.id;
//                    whiteListZone.getWhiteListCircleList().add(whiteListZoneCircle);
//                }
//            }
//            TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager().uploadWhiteListZone(whiteListZone, new com.topxgun.open.api.base.ApiCallback() {
//                @Override
//                public void onResult(BaseResult result) {
//                    Log.d(TAG, "WhiteListFCU：上传禁飞区白名单成功");
//                    //获取飞机白名单并绘制
//                    getAreaLimitWhiteListFromFcc();
//
//                }
//            });
//        }
//
//
//    }
//
//
//    public void getAreaLimitWhiteListFromFcc() {
//        if (!TXGSdkManagerApollo.getInstance().hasConnected()) {
//            return;
//        }
//
//        if (TXGSdkManagerApollo.getInstance().getConnection() == null || TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager() == null) {
//            return;
//        }
//
//        TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager().getWhiteListZone(new com.topxgun.open.api.base.ApiCallback<TXGWhiteListZone>() {
//            @Override
//            public void onResult(BaseResult<TXGWhiteListZone> result) {
//                isWhiteListFetching = false;
//                if (result.getData() != null) {
//                    whiteList = result.getData();
//                    EventBus.getDefault().post(new NoFlyWhiteListZoneGetEvent());
//                }else{
//                    Log.d("WhiteListFCU","获取白名单失败："+result.getMessage());
//                    whiteList = new TXGWhiteListZone();
//                }
//
//            }
//        });
//    }
//
//    /**
//     * 连接飞控失败回调
//     *
//     * @param event 连接飞控消息
//     */
//    @Subscriber(mode = ThreadMode.MAIN)
//    public void onEventMainThread(ClientConnectionFail event) {
//        isFetching = false;
//        isWhiteListFetching = false;
//        whiteList = null;
//        areaLimitList = null;
//        eFenceInfo = null;
//    }
//
//    /**
//     * 连接飞控失败回调
//     *
//     * @param event 连接飞控消息
//     */
//    @Subscriber(mode = ThreadMode.MAIN)
//    public void onEventMainThread(ClientConnectionSuccess event) {
//        getEfenceFromFcc();
//    }
//
//
//    /**
//     * @param flightState
//     */
//    @Subscriber(mode = ThreadMode.MAIN)
//    public void onEventMainThread(IFlightController.FlightState flightState) {
//        Log.d(TAG, "AreaLimit:nofly" + isFetching + "----" + new Gson().toJson(areaLimitList));
//        Log.d(TAG, "AreaLimit:whitelist" + isWhiteListFetching + "----" + new Gson().toJson(whiteList));
//        if (flightState.getDroneLocation().getLatitude() != 0 && flightState.getDroneLocation().getLongitude() != 0 && CommonUtil.isNetworkConnected(AppContext.getInstance())) {
//            if (!isFetching && areaLimitList == null) {
//                Log.d(TAG, "AreaLimitNofly start:");
//                Log.d(TAG, "AreaLimitWhiteList start:");
//                AreaLimitRequest areaLimitRequest = new AreaLimitRequest();
//                areaLimitRequest.center = new double[]{flightState.getDroneLocation().getLongitude(), flightState.getDroneLocation().getLatitude()};
//                areaLimitRequest.radius = 30;
//                getAreaLimitFromServer(areaLimitRequest);
//            }
//
//            if (!isWhiteListFetching && whiteList == null) {
//                if (TXGSdkManagerApollo.getInstance().hasConnected()) {
//                    isWhiteListFetching = true;
//                    AircraftConnection connection = TXGSdkManagerApollo.getInstance().getConnection();
//                    connection.getFlightController().getUniqueId(new ApiCallback<String>() {
//                        @Override
//                        public void onResult(BaseResult<String> result) {
//                            if (result.getCode() == TXGResultCode.TXG_CODE_SUCCESS) {
//                                String boomId = result.getData();
//                                AreaLimitWhiteListRequest whiteListRequest = new AreaLimitWhiteListRequest();
//                                whiteListRequest.lon = flightState.getDroneLocation().getLongitude();
//                                whiteListRequest.lat = flightState.getDroneLocation().getLatitude();
//                                whiteListRequest.radius = 30;
//                                whiteListRequest.boomId = boomId;
//                                getAreaLimitWhiteListFromServer(whiteListRequest);
//                            } else {
//                                isWhiteListFetching = false;
//                            }
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 获取电子围栏信息
//     *
//     * @return
//     */
//    public TXGGeoFence getEfenceInfo() {
//        return eFenceInfo;
//    }
//
//    public void setEfenceInfo(TXGGeoFence eFenceInfo) {
//        this.eFenceInfo = eFenceInfo;
//    }
//
//    public void setEfenceToFcc(TXGGeoFence eFenceInfo) {
//        if (TXGSdkManagerApollo.getInstance().hasConnected()) {
//            IFlyZoneManager flyZoneManager = TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager();
//            flyZoneManager.uploadGeoFence(eFenceInfo, new ApiCallback() {
//                @Override
//                public void onResult(BaseResult result) {
//                    ToastContext.getInstance().toastShort(result.getMessage());
//                }
//            });
//        }
//    }
//
//
//    public void getEfenceFromFcc() {
//        if (TXGSdkManagerApollo.getInstance().hasConnected()) {
//            IFlyZoneManager flyZoneManager = TXGSdkManagerApollo.getInstance().getConnection().getFlyZoneManager();
//            flyZoneManager.getGeoFence(new ApiCallback<TXGGeoFence>() {
//                @Override
//                public void onResult(BaseResult<TXGGeoFence> result) {
//                    if (result.getData() != null) {
//                        setEfenceInfo(result.getData());
//                        EventBus.getDefault().post(new EFenceGotEvent());
//                    }
//
//                }
//            });
//        }
//    }
//}
