package com.widget.pinned;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.materialdesign.R;

/**
 * Created by cwj on 16/11/21.
 */
public class PinnedScrollView extends ScrollView {

    private int anchorId;
    private View anchorView;

    private int headerId;
    private View headerView;

    private int[] anchorLocation = new int[2];

    public PinnedScrollView(Context context) {
        this(context, null);
    }

    public PinnedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initScrollListener();
    }

    private void initScrollListener() {
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (anchorView == null || headerView == null) {
                    return;
                }
                getLocationInWindow(anchorLocation);
                int baseline = anchorLocation[1] + getPaddingTop();
                anchorView.getLocationInWindow(anchorLocation);
                int anchorTop = anchorLocation[1];

                if (anchorTop <= baseline) {
                    headerView.setVisibility(VISIBLE);
                } else {
                    headerView.setVisibility(GONE);
                }
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinnedScrollView);
            anchorId = typedArray.getResourceId(R.styleable.PinnedScrollView_anchor, -1);
            headerId = typedArray.getResourceId(R.styleable.PinnedScrollView_header, -1);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (headerView == null) {
            return;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.getMode(widthMeasureSpec));
        int heightSpec;
        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
        if (layoutParams != null && layoutParams.height > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
        } else {
            heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        headerView.measure(widthSpec, heightSpec);
        headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (headerView == null || headerView.getVisibility() != VISIBLE) {
            return;
        }
        int saveCount = canvas.save();
        canvas.translate(0, getScrollY());
        headerView.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (anchorId > 0) {
            anchorView = findViewById(anchorId);
        }
        if (headerId > 0) {
            headerView = LayoutInflater.from(getContext()).inflate(headerId, this, false);
            headerView.setVisibility(GONE);
        }
    }
}
