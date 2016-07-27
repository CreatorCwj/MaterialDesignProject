package com.materialdesign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.refreshloadview.IRefreshLoadView;
import com.refreshloadview.RefreshLoadListView;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_test_list)
public class TestListActivity extends RoboActivity {

    @InjectView(R.id.error_btn)
    private Button errorBtn;

    @InjectView(R.id.test_btn)
    private Button testBtn;

    @InjectView(R.id.clear_btn)
    private Button clearBtn;

    @InjectView(R.id.load_btn)
    private Button loadBtn;

    @InjectView(R.id.btn)
    private Button btn;

    @InjectView(R.id.listview)
    private RefreshLoadListView listView;

    private ArrayAdapter<String> adapter;

    private MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.item_tv, getData(0));
        listView.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listView.isRefreshing()) {
//                    listView.refreshLoadComplete();
//                } else {
                listView.refresh();
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.refreshLoadComplete();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.getLoadListView().setSelectionFromTop(0, -20);
            }
        });
        errorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.refreshLoadError();
            }
        });

        listView.getRefreshListView().setOnRefreshListener(new IRefreshLoadView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(0, 2000);
                Utils.showToast(getApplicationContext(), "refresh pageNo: " + listView.getPageNo() + " pageSize: " + listView.getPageSize());
            }
        });
        listView.getRefreshListView().setOnLoadListener(new IRefreshLoadView.OnLoadListener() {
            @Override
            public void onLoad() {
                handler.sendEmptyMessageDelayed(1, 2000);
                Utils.showToast(getApplicationContext(), "load pageNo: " + listView.getPageNo() + " pageSize: " + listView.getPageSize());
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adapter.clear();
            }
            doLoadWork();
        }
    }

    private void doLoadWork() {
        adapter.addAll(getData(15));
        listView.refreshLoadComplete();
        Utils.showToast(getApplicationContext(), "complete");
    }

    private List<String> getData(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add("" + i);
        }
        return list;
    }

}
