package com.materialdesign;

import android.os.Bundle;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView((R.layout.activity_test_coordinator_layout))
public class TestCoordinatorLayout extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_coordinator_layout);
    }

}
