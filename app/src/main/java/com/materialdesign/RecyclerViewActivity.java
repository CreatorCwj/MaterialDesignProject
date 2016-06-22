package com.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.adapter.NormalRecyclerAdapter;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView((R.layout.activity_recycler_view))
public class RecyclerViewActivity extends RoboActivity {

    @InjectView(R.id.recyclerView)
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));//线性
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));//gridView
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//瀑布流
        recyclerView.setAdapter(new NormalRecyclerAdapter(this));
    }

}
