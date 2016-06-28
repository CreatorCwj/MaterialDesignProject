package com.materialdesign;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_circle_floating_btn)
public class CircleFloatingBtnActivity extends RoboActivity {

    @InjectView(R.id.iv)
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setIv();
    }

    private void setIv() {
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 360);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat(View.ROTATION_X, 360);
        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat(View.SCALE_X, 0);
        PropertyValuesHolder holder4 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0);
        PropertyValuesHolder holder5 = PropertyValuesHolder.ofFloat(View.ALPHA, 0);
        PropertyValuesHolder holder6 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 150);
        PropertyValuesHolder holder7 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 150);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv, holder1, holder2, holder3, holder4, holder5, holder6, holder7);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private void init() {
        ImageView menuIcon = new ImageView(this);
        menuIcon.setImageResource(R.drawable.arrow);
        FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(this)
                .setContentView(menuIcon).build();

        ImageView subIcon1 = new ImageView(this);
        subIcon1.setImageResource(R.drawable.ic_contact);
        ImageView subIcon2 = new ImageView(this);
        subIcon2.setImageResource(R.drawable.arrow);
        ImageView subIcon3 = new ImageView(this);
        subIcon3.setImageResource(R.drawable.ic_contact);
        ImageView subIcon4 = new ImageView(this);
        subIcon4.setImageResource(R.drawable.arrow);
        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        FloatingActionMenu menu = new FloatingActionMenu.Builder(this)
                .addSubActionView(builder.setContentView(subIcon1).build())
                .addSubActionView(builder.setContentView(subIcon2).build())
                .addSubActionView(builder.setContentView(subIcon3).build())
                .addSubActionView(builder.setContentView(subIcon4).build())
                .attachTo(floatingActionButton)
                .build();
    }

}
