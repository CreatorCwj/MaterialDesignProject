package com.widget.pinned2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by cwj on 16/11/23.
 * 吸顶的ScrollView
 */
public class PinnedScrollView extends ScrollView implements IPinnedScroll {

    private int[] anchorLocation = new int[2];

    private OnPinnedScrollListener onPinnedScrollListener;

    public PinnedScrollView(Context context) {
        super(context);
    }

    public PinnedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean shouldPinned(@NonNull View anchorView) {
        //计算各自的top
        getLocationInWindow(anchorLocation);
        int baseline = anchorLocation[1] + getPaddingTop();
        anchorView.getLocationInWindow(anchorLocation);
        int anchorTop = anchorLocation[1];
        return anchorTop < baseline;
    }

    @Override
    public void setIPinnedScrollListener(@NonNull OnPinnedScrollListener onPinnedScrollListener) {
        this.onPinnedScrollListener = onPinnedScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onPinnedScrollListener != null) {
            onPinnedScrollListener.onPinnedScroll();
        }
    }
}
