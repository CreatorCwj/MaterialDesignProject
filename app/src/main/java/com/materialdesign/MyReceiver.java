package com.materialdesign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.utils.Utils;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Utils.showToast(context,"onReceive");
    }
}
