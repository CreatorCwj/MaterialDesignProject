package com.materialdesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.utils.Utils;
import com.widget.DoubleProgressView;
import com.widget.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_test)
public class TestActivity extends RoboActivity implements View.OnClickListener {

    @InjectView(R.id.progressView)
    private View progressView;

    @InjectView(R.id.progressTop)
    private ImageView progressTop;

    @InjectView(R.id.iv)
    private ImageView iv;

    @InjectView(R.id.container)
    private LinearLayout container;

    @InjectView(R.id.animIv)
    private ImageView animIv;

    @InjectView(R.id.roundBar)
    private RoundProgressBar roundBar;

    @InjectView(R.id.mpv)
    private DoubleProgressView mpv;

    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LifeCycle:", "onCreate");
        if (savedInstanceState != null && savedInstanceState.containsKey("outState")) {
            String outState = savedInstanceState.getString("outState");
            Utils.showToast(this, outState);
        }
        iv.setOnClickListener(this);
        testGlobalLayout();
        testAnim();
        setRoundBar();

        mpv.postDelayed(new Runnable() {
            @Override
            public void run() {
                mpv.setData(36, 64);
            }
        }, 2000);
//        startActivity();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
        return b;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LifeCycle:", "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("LifeCycle:", "onRestoreInstanceState");
        if (savedInstanceState.containsKey("outState")) {
            String outState = savedInstanceState.getString("outState");
            Utils.showToast(this, "onRestoreInstanceState " + outState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LifeCycle:", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LifeCycle:", "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("LifeCycle:", "onSaveInstanceState");
        outState.putString("outState", "I am killed by accident!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LifeCycle:", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LifeCycle:", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LifeCycle:", "onRestart");
    }

    private void startActivity() {
//        startActivity(new Intent(this, PhotoViewActivity.class));
        startActivity(new Intent(this, CircleFloatingBtnActivity.class));
    }

    private void setRoundBar() {
        roundBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    ViewGroup.LayoutParams params = roundBar.getLayoutParams();
                    params.width += 100;
                    roundBar.setLayoutParams(params);
                    roundBar.invalidate();
                } else roundBar.requestLayout();
                flag = !flag;
            }
        });
    }

    private void testAnim() {
        AnimationDrawable animationDrawable = (AnimationDrawable) animIv.getBackground();
        if (animationDrawable != null)
            animationDrawable.start();
    }

    private void testGlobalLayout() {
        //整个View树内有变化都会发送调用,所以处理完后要remove掉
        iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Utils.showToast(getApplicationContext(), "imageview");
            }
        });
    }

    @Override
    public void onClick(View v) {
        mpv.setData(0, 64);

//        progressTop.setVisibility(View.INVISIBLE);
        List<Animator> animators = new ArrayList<>();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = progressView.getLayoutParams();
                if (params != null) {
                    params.width = width;
                    progressView.setLayoutParams(params);
                }
            }
        });
        animators.add(valueAnimator);

        //第二个参数是属性名,必须确保被更新对象有setter和getter方法(进行动画实时处理)
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(progressTop, "translationX", 0, 1000);
        animators.add(objectAnimator);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(progressTop, "rotation", 0, 360);
        animators.add(objectAnimator2);

        AnimatorSet animatorSet = new AnimatorSet();//也可用各自的ofPropertyValueHolder方法来控制多个动画同步异步执行
        animatorSet.playTogether(animators);//同步执行动画
        animatorSet.playSequentially(animators);//顺序执行动画
        animatorSet.play(valueAnimator).with(objectAnimator);//同步执行两个动画
        animatorSet.play(objectAnimator2).after(valueAnimator).after(2000);//在某个动画后执行一个动画,after为play的动画执行前延迟时间,不能在AnimatorSet里设置duration,否则会覆盖(duration可在给动画的Animator对象设置)
        animatorSet.setDuration(1000);
        animatorSet.start();

    }
}
