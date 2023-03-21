package com.lqz.mytools.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.lqz.commonsdk.application.ApplicationObserver;
import com.lqz.commonsdk.application.CommonApplication;
import com.lqz.commonsdk.application.IApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * author : LQZ
 * e-mail : qzli@topxgun.com
 * date   : 2023/2/10 10:02
 * desc   :
 */
public class MyApplication extends Application {

    private List<IApplication> applicationList = new ArrayList<>();

    //该方法比onCreate执行的更早
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationList.add(new CommonApplication(this));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //将CommonSDK的生命周期绑定进来
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver(this));
        setOnCreate();
    }

    private void setOnCreate() {
        for (IApplication app : applicationList) {
            app.onCreate();
        }
    }

    /**
     * 注意：这个接口只有在模拟器上才会触发，在真机上是不会触发的
     * 可以参考：https://blog.csdn.net/weixin_29081101/article/details/117576701
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        setOnTerminate();
    }

    private void setOnTerminate() {
        for (IApplication app : applicationList) {
            app.onTerminate();
        }
    }
}
