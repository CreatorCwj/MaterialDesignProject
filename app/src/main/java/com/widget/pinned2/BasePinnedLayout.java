package com.widget.pinned2;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.materialdesign.R;

/**
 * Created by cwj on 16/11/24.
 * 吸顶容器基类
 */
public abstract class BasePinnedLayout<T extends BaseIPinned> extends FrameLayout {

    private static final int NO_ID = -1;

    private static final boolean DEFAULT_CAN_PINNED = true;

    private boolean canPinned = DEFAULT_CAN_PINNED;//是否有吸顶功能,默认开启
    private int headerId = NO_ID;//headerView的id

    private View headerView;

    protected T pinnedView;

    private BaseIPinned.OnPinnedScrollListener onPinnedScrollListener;

    public BasePinnedLayout(Context context) {
        this(context, null);
    }

    public BasePinnedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePinnedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BasePinnedLayout);
            canPinned = typedArray.getBoolean(R.styleable.BasePinnedLayout_canPinned, DEFAULT_CAN_PINNED);
            headerId = typedArray.getResourceId(R.styleable.BasePinnedLayout_headerView, NO_ID);
            typedArray.recycle();
        }
    }

    private void initListener() {
        onPinnedScrollListener = new BaseIPinned.OnPinnedScrollListener() {
            @Override
            public void onPinnedScroll() {
                refreshOnScrollChanged();
            }
        };
    }

    /**
     * 设置headerView的点击事件
     */
    public void setOnPinnedViewClickListener(OnClickListener onPinnedViewClickListener) {
        if (headerView != null) {
            headerView.setOnClickListener(onPinnedViewClickListener);
        }
    }

    /**
     * 是否开启了吸顶功能
     */
    public boolean isCanPinned() {
        return canPinned;
    }

    /**
     * 获取headerView
     */
    public View getHeaderView() {
        return headerView;
    }

    /**
     * 是否开启吸顶功能
     */
    public void setCanPinned(boolean canPinned) {
        if (this.canPinned == canPinned) {
            return;
        }
        this.canPinned = canPinned;
        refreshHeaderState();
    }

    /**
     * 设置headerId指定headerView
     */
    public void setHeaderId(int headerId) {
        if (this.headerId == headerId) {
            return;
        }
        this.headerId = headerId;
        inflateHeaderView();
    }

    /**
     * 设置headerView
     */
    public void setHeaderView(View headerView) {
        if (this.headerView == headerView) {
            return;
        }
        resetHeaderView(headerView);
    }

    /**
     * 是否到达吸顶状态,由子类自己实现
     */
    protected abstract boolean shouldPinned(@NonNull T pinnedView);

    /**
     * 由于anchor实现方式不一样,所以anchor有效的条件交由子类实现
     */
    protected abstract boolean anchorValid();

    /**
     * 滚动时的刷新方法,默认为直接刷新headerView状态
     */
    protected void refreshOnScrollChanged() {
        refreshHeaderState();
    }

    /**
     * 加载headerView的方法,默认为根据headerId加载View
     */
    protected void inflateHeaderView() {
        View newHeaderView = headerId != NO_ID ? LayoutInflater.from(getContext()).inflate(headerId, this, false) : null;
        resetHeaderView(newHeaderView);
    }

    /**
     * 重置headerView,移除原有的,添加新的,并刷新
     */
    final protected void resetHeaderView(View newHeaderView) {
        if (headerView == newHeaderView) {//没变不做处理
            return;
        }
        if (headerView != null) {//移除原有的
            removeView(headerView);
        }
        if (newHeaderView != null) {//添加新的
            addView(newHeaderView);
        }
        headerView = newHeaderView;
        refreshHeaderState();//刷新headerView状态
    }

    /**
     * 刷新headerView状态
     */
    final protected void refreshHeaderState() {
        if (headerView != null) {
            headerView.setVisibility(GONE);
        }
        if (!canPinned || headerView == null || pinnedView == null || !anchorValid()) {
            return;
        }
        if (shouldPinned(pinnedView)) {
            headerView.setVisibility(VISIBLE);
        } else {
            headerView.setVisibility(GONE);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //xml加载完后才可以获取到相应的view
        findPinnedView();
        registerScrollListenerToPinned();
        inflateHeaderView();
    }

    private void registerScrollListenerToPinned() {
        if (pinnedView != null) {
            pinnedView.setIPinnedScrollListener(onPinnedScrollListener);
        }
    }

    private void findPinnedView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof BaseIPinned) {
                try {
                    //noinspection unchecked
                    pinnedView = (T) view;
                } catch (Exception e) {
                    pinnedView = null;
                }
                return;
            }
        }
    }
}
