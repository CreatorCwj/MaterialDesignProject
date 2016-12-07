package com.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.ThroughTouchAdapter;
import com.utils.Utils;

public class ThroughTouchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_through_touch);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ThroughTouchAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToast(getApplicationContext(), "俺被点击了:" + position);
            }
        });

        TextView textView = (TextView) findViewById(R.id.titleTv);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.showToast(getApplicationContext(), "俺作为标题栏居然被点击了");
//            }
//        });
    }
}
