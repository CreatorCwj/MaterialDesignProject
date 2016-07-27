package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.materialdesign.R;
import com.widget.refreshloadview.HeaderLayout;

/**
 * Created by cwj on 16/7/26.
 */
public class RotateHeaderLayout extends HeaderLayout {

    private ImageView imageView;
    private TextView textView;

    private Animation mRotateAnimation;
    private Matrix mHeaderImageMatrix;

    private float mRotationPivotX, mRotationPivotY;

    public RotateHeaderLayout(Context context) {
        this(context, null);
    }

    public RotateHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //用矩阵来实时旋转imageView
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        mHeaderImageMatrix = new Matrix();
        imageView.setImageMatrix(mHeaderImageMatrix);
        if (null != imageView.getDrawable()) {
            mRotationPivotX = Math.round(imageView.getDrawable().getIntrinsicWidth() / 2f);
            mRotationPivotY = Math.round(imageView.getDrawable().getIntrinsicHeight() / 2f);
        }
        //无限旋转动画
        mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setDuration(1200);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {
        //属性
    }

    @Override
    protected void onViewCreated() {
        imageView = (ImageView) findViewById(R.id.rotate_header_item_imageView);
        textView = (TextView) findViewById(R.id.rotate_header_item_textView);
    }

    @Override
    protected int getContentViewId() {
        return R.id.rotate_header_content;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.rotate_header_layout;
    }

    @Override
    public void onResetState() {
        textView.setText("刷新结束");
        imageView.clearAnimation();
        mHeaderImageMatrix.reset();
        imageView.setImageMatrix(mHeaderImageMatrix);
    }

    @Override
    public void onRefreshState() {
        textView.setText("正在刷新...");
        imageView.startAnimation(mRotateAnimation);
    }

    @Override
    public void onPullToRefreshState() {
        textView.setText("下拉刷新");
    }

    @Override
    public void onReleaseToRefreshState() {
        textView.setText("释放立即刷新");
    }

    @Override
    public void onScalePull(float scale) {
        float angle = 360 * scale;
        mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
        imageView.setImageMatrix(mHeaderImageMatrix);
    }
}
