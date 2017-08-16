package com.materialdesign;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.utils.Utils;

import java.util.Random;

/**
 * Created by cwj on 17/4/5.
 */

public class TestService extends Service {

    private static final String TAG = TestService.class.getSimpleName();
    private static final String KEY_NAME = "name";

    public static Intent getIntent(Context context, String name) {
        Intent intent = new Intent(context, TestService.class);
        intent.putExtra(KEY_NAME, name);
        return intent;
    }

    public class TestBinder extends Binder {

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Utils.logger(TAG, "tid:", android.os.Process.myTid() + "");
            Utils.logger(TAG, "pid:", android.os.Process.myPid() + "");
            return super.onTransact(code, data, reply, flags);
        }

        public final TestService getService() {
            return TestService.this;
        }
    }

    private final IBinder iBinder = new TestBinder();
    private final Random random = new Random();

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.logger(TAG, "onCreate", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.logger(TAG, "onDestroy", "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.logger(TAG, "onStartCommand", intent.getStringExtra(KEY_NAME));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.logger(TAG, "onBind", intent.getStringExtra(KEY_NAME));
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.logger(TAG, "onUnbind", intent.getStringExtra(KEY_NAME));
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Utils.logger(TAG, "onRebind", intent.getStringExtra(KEY_NAME));
    }

    public final int getRandomInt() {
        return random.nextInt(100);
    }
}
