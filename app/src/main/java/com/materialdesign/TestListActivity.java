package com.materialdesign;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.utils.Utils;
import com.widget.refreshloadview.LoadViewController;
import com.widget.refreshloadview.RefreshLoadListView;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_test_list)
public class TestListActivity extends RoboActivity {

    @InjectView(R.id.listview)
    private RefreshLoadListView listView;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.item_tv, getData());
        listView.setAdapter(adapter);
//        listView.setOnLoadListener(new LoadViewController.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                Utils.showToast(getApplicationContext(), "loading");
//            }
//        });
//        listView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                listView.loadComplete();
//            }
//        }, 10000);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        return list;
    }

}
