package com.materialdesign;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by cwj on 16/7/18.
 * 返回界面或跳转到界面时会先调用onActivityResumed方法,再调用onActivityStopped方法
 */
public class MyLifeCycle implements Application.ActivityLifecycleCallbacks {

    private int count = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (count <= 0) {//第一个activity进入界面
            System.out.println("应用到前台");
        }
        ++count;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        --count;
        if (count <= 0) {
            System.out.println("应用在后台");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
