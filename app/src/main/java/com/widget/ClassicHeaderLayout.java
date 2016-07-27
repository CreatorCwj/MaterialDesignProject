package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.materialdesign.R;
import com.widget.refreshloadview.HeaderLayout;

/**
 * Created by cwj on 16/7/26.
 */
public class ClassicHeaderLayout extends HeaderLayout {

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;

    private Animation mRotateAnimation, mResetRotateAnimation;

    public ClassicHeaderLayout(Context context) {
        this(context, null);
    }

    public ClassicHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(150);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(new LinearInterpolator());
        mResetRotateAnimation.setDuration(150);
        mResetRotateAnimation.setFillAfter(true);
    }

    @Override
    protected void onViewCreated() {
        imageView = (ImageView) findViewById(R.id.classic_header_item_imageView);
        textView = (TextView) findViewById(R.id.classic_header_item_textView);
        progressBar = (ProgressBar) findViewById(R.id.classic_header_item_progressBar);
    }

    @Override
    protected int getContentViewId() {
        return R.id.classic_header_content;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.classic_header_layout;
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {

    }

    @Override
    public void onResetState() {
        imageView.clearAnimation();
        imageView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        textView.setText("刷新结束");
    }

    @Override
    public void onRefreshState() {
        imageView.clearAnimation();
        imageView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        textView.setText("正在刷新...");
    }

    @Override
    public void onPullToRefreshState() {
        progressBar.setVisibility(GONE);
        textView.setText("下拉刷新");
        imageView.setVisibility(VISIBLE);
        if (mRotateAnimation == imageView.getAnimation()) {
            imageView.startAnimation(mResetRotateAnimation);
        }
    }

    @Override
    public void onReleaseToRefreshState() {
        progressBar.setVisibility(GONE);
        textView.setText("释放立即刷新");
        imageView.setVisibility(VISIBLE);
        imageView.startAnimation(mRotateAnimation);
    }

    @Override
    public void onScalePull(float scale) {

    }
}
