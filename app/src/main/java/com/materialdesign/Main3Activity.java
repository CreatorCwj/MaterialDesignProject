package com.materialdesign;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.adapter.ExpAdapter;

import java.util.Arrays;

public class Main3Activity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableLv);
        ExpAdapter adapter = new ExpAdapter(Arrays.asList("g1", "g2"), Arrays.asList(Arrays.asList("g1c1", "g1c2"), Arrays.asList("g2c1", "g2c2")));
        expandableListView.setAdapter(adapter);
        expandableListView.setDivider(new ColorDrawable(Color.YELLOW));
//        ColorDrawable colorDrawable = new ColorDrawable(Color.GREEN);
//        colorDrawable.setBounds(0, 0, 6, 6);
        Drawable drawable = getResources().getDrawable(R.drawable.arrow);
        expandableListView.setChildDivider(drawable);
        expandableListView.setDividerHeight(6);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildIndicator(null);
        expandableListView.setHeaderDividersEnabled(false);
        expandableListView.setFooterDividersEnabled(false);
        expandableListView.setOnGroupClickListener(adapter);
    }

}
