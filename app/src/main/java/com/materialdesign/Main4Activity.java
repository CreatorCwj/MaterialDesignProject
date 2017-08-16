package com.materialdesign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adapter.RecyclerAdapter;
import com.utils.Utils;
import com.widget.recyclerview.HeaderFooterRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {

    private HeaderFooterRecyclerView recyclerView;

    private TextView header1, header2, header3;
    private TextView footer1, footer2;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btn = (Button) findViewById(R.id.btn);
        recyclerView = (HeaderFooterRecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.VERTICAL);
//        GridLayoutManager manager = new GridLayoutManager(this,3);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

//        recyclerView.setAdapter(new RecyclerAdapter(getData()));

        header1 = getHeaderFooter("Header1");
        header2 = getHeaderFooter("Header2");
        header3 = getHeaderFooter("Header3");
        footer1 = getHeaderFooter("Footer1");
        footer2 = getHeaderFooter("Footer2");
//        recyclerView.addHeaderView(header1);
//        recyclerView.addFooterView(footer1);
//        recyclerView.addHeaderView(header2);
//        recyclerView.addHeaderView(header3);
//        recyclerView.addFooterView(footer2);766525

//        recyclerView.removeHeaderView(header1);
//        recyclerView.removeFooterView(footer2);

        final RecyclerAdapter adapter = new RecyclerAdapter(getData());

        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
//                adapter.addDataToPosition("addData", 2);
                adapter.notifyDataSetChanged();
//                recyclerView.addHeaderView(header1);
//                recyclerView.removeHeaderView(header2);
//                v.requestLayout();
            }
        });

//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.removeHeaderView(header2);
////                recyclerView.removeFooterView(footer1);
//
////                recyclerView.removeHeaderView(header2);
//                recyclerView.addHeaderView(header1);
////                recyclerView.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        recyclerView.addHeaderView(header2);
////                    }
////                },2000);
//
////                recyclerView.addFooterView(footer2);
//                Utils.showToast(Main4Activity.this, "" + recyclerView.getCount());
//            }
//        }, 5000);
        Utils.showToast(this, "" + recyclerView.getCount());
        recyclerView.setOnItemClickListener(new HeaderFooterRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.showToast(getApplicationContext(), "click:" + position);
            }
        });
        recyclerView.setOnItemLongClickListener(new HeaderFooterRecyclerView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Utils.showToast(getApplicationContext(), "long click:" + position);
            }
        });
//        startActivityForResult(new Intent(Main4Activity.this, FocusActivity.class), 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private TextView getHeaderFooter(String value) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setText(value);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("" + i);
        }
        return list;
    }
}
