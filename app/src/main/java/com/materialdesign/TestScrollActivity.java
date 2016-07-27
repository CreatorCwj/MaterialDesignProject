package com.materialdesign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.refreshloadview.IRefreshLoadView;
import com.refreshloadview.RefreshLoadScrollView;
import com.utils.Utils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_test_scroll)
public class TestScrollActivity extends RoboActivity implements IRefreshLoadView.OnRefreshListener, IRefreshLoadView.OnLoadListener, IRefreshLoadView.OnEmptyViewListener {

    @InjectView(R.id.refreshBtn)
    private Button refreshBtn;

    @InjectView(R.id.loadBtn)
    private Button loadBtn;

    @InjectView(R.id.rlScrollView)
    private RefreshLoadScrollView rlScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlScrollView.setOnRefreshListener(this);
//        rlScrollView.setOnEmptyViewListener(this);
        rlScrollView.setOnLoadListener(this);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlScrollView.refresh();
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlScrollView.errorWithDataEmpty();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                rlScrollView.refreshLoadComplete();
            }
        }
    };

    @Override
    public void onEmptyViewVisible(@NonNull View emptyView) {
        Utils.showToast(getApplicationContext(), "emptyVisible");
    }

    @Override
    public void onEmptyViewGone(@NonNull View emptyView) {
        Utils.showToast(getApplicationContext(), "emptyGone");
    }

    @Override
    public void onLoad() {
        Utils.showToast(getApplicationContext(), "load");
    }

    @Override
    public void onRefresh() {
        Utils.showToast(getApplicationContext(), "refresh");
    }
}
