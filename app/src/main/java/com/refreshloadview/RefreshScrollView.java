package com.refreshloadview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;


/**
 * Created by cwj on 16/7/27.
 * 刷新加载ScrollView
 * 1.刷新和加载不同时进行,避免数据混乱
 * 2.支持首次进入刷新
 * 3.封装分页逻辑
 */
public class RefreshScrollView extends RefreshBaseView<LoadScrollView> implements SwipeRefreshLayout.OnRefreshListener, LoadScrollView.OnLoadListener, IRefreshLoadView {

    private boolean showEmptyView;//是否展示了空view

    public RefreshScrollView(Context context) {
        this(context, null);
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected LoadScrollView createLoadView(Context context, AttributeSet attrs) {
        return new LoadScrollView(context, attrs);
    }

    void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }

    @Override
    public boolean canChildScrollUp() {
        //展示空view情况下直接可以下拉刷新
        return !showEmptyView && super.canChildScrollUp();
    }

}
