package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by cwj on 16/7/22.
 * Header
 */
public class HeaderLayout extends BaseHeaderFooterLayout implements HeaderController {

    public HeaderLayout(Context context) {
        super(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {

    }

    @Override
    public void onResetState() {
        if (imageView != null) {
            imageView.setVisibility(GONE);
        }
        if (textView != null) {
            textView.setText("重置");
        }
    }

    @Override
    public void onRefreshState() {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
        }
        if (textView != null) {
            textView.setText("正在刷新...");
        }
    }

    @Override
    public void onPullToRefreshState() {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
        }
        if (textView != null) {
            textView.setText("下拉刷新");
        }
    }

    @Override
    public void onReleaseToRefreshState() {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
        }
        if (textView != null) {
            textView.setText("释放刷新");
        }
    }
}
