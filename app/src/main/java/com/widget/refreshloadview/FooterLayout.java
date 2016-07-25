package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by cwj on 16/7/22.
 * Footer
 */
public class FooterLayout extends BaseHeaderFooterLayout implements FooterController {

    public FooterLayout(Context context) {
        this(context, null);
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {
        //解析属性
    }

    @Override
    public void onIdleState() {
        if (imageView != null) {
            imageView.setVisibility(GONE);
        }
        if (textView != null) {
            textView.setText("等待加载");
        }
    }

    @Override
    public void onLoadingState() {
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
        }
        if (textView != null) {
            textView.setText("加载中...");
        }
    }

}
