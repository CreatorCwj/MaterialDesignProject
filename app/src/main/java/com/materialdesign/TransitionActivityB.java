package com.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_transition_activity_b)
public class TransitionActivityB extends RoboActivity {

    @InjectView(R.id.imageView2)
    private ImageView imageView;

    @InjectView(R.id.button2)
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivityB.this, imageView, "image");
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(TransitionActivityB.this, R.anim.translate_in, R.anim.translate_out);
//                ActivityCompat.startActivity(TransitionActivityB.this, new Intent(TransitionActivityB.this, TransitionActivityA.class), activityOptionsCompat.toBundle());
                startActivity(new Intent(TransitionActivityB.this, TransitionActivityA.class), activityOptionsCompat.toBundle());
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.finishAfterTransition(TransitionActivityB.this);
                finishAfterTransition();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();
    }

}
