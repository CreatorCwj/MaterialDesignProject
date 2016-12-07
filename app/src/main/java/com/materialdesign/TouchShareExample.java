package com.materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.utils.Utils;

public class TouchShareExample extends AppCompatActivity implements View.OnClickListener {

    private Button throughBtn;
    private Button vpBtn;
    private Button enableBtn;
    private Button scaleIvBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_share_example);
        throughBtn = (Button) findViewById(R.id.throughTouchBtn);
        vpBtn = (Button) findViewById(R.id.viewPagerBtn);
        enableBtn = (Button) findViewById(R.id.enableBtn);
        scaleIvBtn = (Button) findViewById(R.id.scaleIvBtn);

        throughBtn.setOnClickListener(this);
        vpBtn.setOnClickListener(this);
        enableBtn.setOnClickListener(this);
        scaleIvBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == throughBtn) {
            gotoActivity(ThroughTouchActivity.class);
        } else if (v == vpBtn) {
            gotoActivity(VPInterceptActivity.class);
        } else if (v == enableBtn) {
            Utils.showToast(getApplicationContext(), "I am clicked");
        } else if (v == scaleIvBtn) {
            gotoActivity(TouchActivity.class);
        }
    }

    private void gotoActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }
}
