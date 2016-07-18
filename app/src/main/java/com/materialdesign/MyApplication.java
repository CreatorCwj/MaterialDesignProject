package com.materialdesign;

import android.app.Application;

/**
 * Created by cwj on 16/7/18.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new MyLifeCycle());
    }
}
