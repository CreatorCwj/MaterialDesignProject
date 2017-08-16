package com.widget.pinned2;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.materialdesign.R;

/**
 * Created by cwj on 16/11/23.
 * 普通滑动组件的吸顶容器
 */
public class ScrollPinnedLayout extends BasePinnedLayout<IPinnedScroll> {

    private static final int NO_ID = -1;

    private int anchorId = NO_ID;//anchorView的id(某一子view的id)

    private View anchorView;

    public ScrollPinnedLayout(Context context) {
        this(context, null);
    }

    public ScrollPinnedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPinnedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollPinnedLayout);
            anchorId = typedArray.getResourceId(R.styleable.ScrollPinnedLayout_anchorId, NO_ID);
            typedArray.recycle();
        }
    }

    /**
     * 获取anchorView
     */
    public View getAnchorView() {
        return anchorView;
    }

    /**
     * 设置anchorId指定anchorView
     */
    public void setAnchorId(int anchorId) {
        if (this.anchorId == anchorId) {
            return;
        }
        this.anchorId = anchorId;
        findAnchorView();
    }

    /**
     * 设置anchorView
     */
    public void setAnchorView(View anchorView) {
        if (this.anchorView == anchorView) {
            return;
        }
        this.anchorView = anchorView;
        refreshHeaderState();
    }

    private void findAnchorView() {
        if (anchorId != NO_ID) {
            anchorView = findViewById(anchorId);
        } else {
            anchorView = null;
        }
        refreshHeaderState();
    }

    @Override
    protected boolean shouldPinned(@NonNull IPinnedScroll pinnedView) {
        return pinnedView.shouldPinned(anchorView);
    }

    @Override
    protected boolean anchorValid() {
        return anchorView != null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //xml加载完后才可以获取到相应的view
        findAnchorView();
    }

}
