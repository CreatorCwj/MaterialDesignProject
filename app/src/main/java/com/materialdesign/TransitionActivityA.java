package com.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_transition)
public class TransitionActivityA extends RoboActivity {

    @InjectView(R.id.imageView1)
    private ImageView imageView;

    @InjectView(R.id.button1)
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.原始方法
//                startActivity(new Intent(getApplicationContext(), TransitionActivityB.class));
//                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);//原始方法,在start和finish后调用即可
                //2.makeCustomAnimation自定义动画(通过ActivityOptionsCompat实现)
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(TransitionActivityA.this, R.anim.translate_in, R.anim.translate_out);
                //3.某个view放大缩小过度
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(imageView, imageView.getWidth() / 2, imageView.getHeight() / 2, 0, 0);
                //4.bitmap在某个view范围内放大过度
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(imageView, BitmapFactory.decodeResource(getResources(), R.drawable.arrow), 0, 0);
                //5.单组对象过度
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivityA.this, imageView, "image");
                /*Pair<View,String> p = Pair.create(imageView,"image");
                List<Object> list = new ArrayList<Object>();
                list.add(p1);
                Map<Object,Object> map = new HashMap<Object, Object>();
                map.put("","");*/
                //6.多组对象过度
//                Pair<View, String> p1 = Pair.create((View)imageView, "image");
//                Pair<View, String> p2 = Pair.create((View)button, "button");
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivityA.this, p1, p2);
//                ActivityCompat.startActivity(TransitionActivityA.this, new Intent(TransitionActivityA.this, TransitionActivityB.class), activityOptionsCompat.toBundle());
                startActivity(new Intent(TransitionActivityA.this, TransitionActivityB.class), activityOptionsCompat.toBundle());
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.finishAfterTransition(TransitionActivityA.this);
                finishAfterTransition();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
    }
}
