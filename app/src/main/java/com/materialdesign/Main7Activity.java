package com.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.utils.Utils;

public class Main7Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        ImageView imageView = (ImageView) findViewById(R.id.iv3);
        if (imageView == null) {
            return;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = v.getLayoutParams();
                if (params != null) {
                    params.width = Utils.dp2px(getApplicationContext(), 150);
                    params.height = Utils.dp2px(getApplicationContext(), 150);
                    v.setLayoutParams(params);
                }
            }
        });
    }
}
