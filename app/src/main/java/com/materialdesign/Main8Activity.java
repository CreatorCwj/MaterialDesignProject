package com.materialdesign;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.adapter.Main8RecyclerAdapter;

import java.util.ArrayList;

public class Main8Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Main8RecyclerAdapter adapter;

    private SwipeRefreshLayout swipe_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        swipe_view = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new Divider(Utils.dp2px(this, 15), Color.GRAY));

        adapter = new Main8RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.update(Main8RecyclerAdapter.BULLETIN, "I am bulletin");
        adapter.update(Main8RecyclerAdapter.DASHBOARD, new ArrayList<String>() {
            {
                add("item0");
                add("item1");
                add("item2");
                add("item3");
            }
        });
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.update(Main8RecyclerAdapter.BULLETIN, null);
            }
        }, 3000);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.update(Main8RecyclerAdapter.BULLETIN, "bulletin come back!!!");
            }
        }, 6000);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.update(Main8RecyclerAdapter.DASHBOARD, null);
            }
        }, 9000);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.update(Main8RecyclerAdapter.DASHBOARD, new ArrayList<String>() {
                    {
                        add("item4");
                        add("item5");
                        add("item6");
                        add("item7");
                    }
                });
            }
        }, 12000);
    }
}
