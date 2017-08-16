package com.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.adapter.LVAdapter;

import java.util.ArrayList;
import java.util.List;

public class InnerListViewActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_list_view);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new LVAdapter(getData()));
    }

    private List<String> getData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("newItem" + i);
        }
        return datas;
    }
}
