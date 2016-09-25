package com.zcj.zhbj;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        Log.i("onCreate: ","应用被创建了");
    }
}
