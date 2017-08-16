package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.widget.refreshloadview.HeaderLayout;

/**
 * Created by cwj on 16/7/26.
 */
public class AnimationHeaderLayout extends HeaderLayout {

    public AnimationHeaderLayout(Context context) {
        super(context);
    }

    public AnimationHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {

    }

    @Override
    protected void onViewCreated() {

    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onResetState() {

    }

    @Override
    public void onRefreshState() {

    }

    @Override
    public void onPullToRefreshState() {

    }

    @Override
    public void onReleaseToRefreshState() {

    }

    @Override
    public void onScalePull(float scale) {

    }
}
