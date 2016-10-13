package com.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.LVAdapter;

import java.util.ArrayList;
import java.util.List;

public class Main5Activity extends AppCompatActivity {

    private ListView listview;
    private Button btn;
    private Button btn2;
    private LVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        adapter = new LVAdapter(getData());
        listview = (ListView) findViewById(R.id.listview);
        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);
        listview.addHeaderView(new TextView(this));
        listview.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listview.getVisibility() == View.VISIBLE) {
                    listview.setVisibility(View.GONE);
                } else {
                    listview.setVisibility(View.VISIBLE);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(getData());
            }
        });
//        listview.setAdapter(new LVAdapter(getData()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("" + i);
        }
        return list;
    }
}
