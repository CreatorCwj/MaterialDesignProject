package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.materialdesign.R;


/**
 * Created by cwj on 16/7/28.
 * 刷新加载控件
 * 1.添加空view(可设置xml,也可代码多次改变)
 * 2.需要用到子view方法,通过getXxxView拿到后自行调用即可,这里只提供通用方法
 * 3.todo:整套view支持xml布局,代码生成的方式并不完善
 */
public class RefreshLoadScrollView extends FrameLayout implements IRefreshLoadView {

    private RefreshScrollView refreshScrollView;
    private ViewStub viewStub;
    private View emptyView;

    private int emptyViewId = -1;

    private OnEmptyViewListener onEmptyViewListener;

    public RefreshLoadScrollView(Context context) {
        this(context, null);
    }

    public RefreshLoadScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            emptyViewId = typedArray.getResourceId(R.styleable.RefreshLoadView_emptyView, -1);
            typedArray.recycle();
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        //先添加emptyView,防止emptyView有背景时显示在刷新控件前面,下拉效果看不到
        if (emptyViewId != -1) {
            viewStub = new ViewStub(context, emptyViewId);
            addView(viewStub);
        }
        //添加刷新加载控件
        refreshScrollView = new RefreshScrollView(context, attrs);
        addView(refreshScrollView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 获取刷新加载view
     */
    public RefreshScrollView getRefreshScrollView() {
        return refreshScrollView;
    }

    /**
     * 获取加载view
     */
    public LoadScrollView getLoadScrollView() {
        return refreshScrollView.getLoadScrollView();
    }

    /**
     * 获取空view(还没有inflate要inflate)
     */
    public View getEmptyView() {
        if (emptyView == null && viewStub != null) {
            emptyView = viewStub.inflate();
            emptyView.setVisibility(GONE);//先隐藏view
        }
        return emptyView;
    }

    /**
     * 手动设置空view,可多次设置
     */
    public void setEmptyView(View emptyView) {
        //移除之前的
        if (this.emptyView != null && this.emptyView.getParent() != null && this.emptyView.getParent() == this) {
            removeView(this.emptyView);
        }
        //放入新的emptyView(先从父view移除)
        this.emptyView = emptyView;
        if (emptyView.getParent() != null && (emptyView.getParent() instanceof ViewGroup)) {
            ((ViewGroup) emptyView.getParent()).removeView(emptyView);
        }
        addView(emptyView, 0);//添加到最下面,防止有背景时使得刷新控件无效果
        this.emptyView.setVisibility(GONE);//先隐藏view
    }

    @Override
    public void setOnEmptyViewListener(OnEmptyViewListener onEmptyViewListener) {
        this.onEmptyViewListener = onEmptyViewListener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        refreshScrollView.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        refreshScrollView.setOnLoadListener(onLoadListener);
    }

    @Override
    public boolean isLoading() {
        return refreshScrollView.isLoading();
    }

    @Override
    public boolean isRefreshing() {
        return refreshScrollView.isRefreshing();
    }

    @Override
    public int getPageNo() {
        return refreshScrollView.getPageNo();
    }

    @Override
    public int getPageSize() {
        return refreshScrollView.getPageSize();
    }

    @Override
    public void refresh() {
        refreshScrollView.refresh();
    }

    /**
     * 默认调用此方法结束刷新加载(不需要考虑展示空view以及加载功能的时候)
     */
    @Override
    public void refreshLoadComplete() {
        refreshLoadComplete(false, false);
    }

    /**
     * 刷新加载结束,并通知当前无数据
     */
    public void completeWithDataEmpty() {
        refreshLoadComplete(true, false);
    }

    /**
     * 刷新加载结束,并通知当无更多数据
     */
    public void completeWithNoMoreData() {
        refreshLoadComplete(false, true);
    }

    /**
     * 针对与ScrollView刷新加载的结束方法,可以通知数据的状态(需要手动通知是因为无法像ListView有adapter那样知道数据的状态)
     *
     * @param isDataEmpty  数据是否为空
     * @param isNoMoreData 数据是否没有更多
     */
    private void refreshLoadComplete(boolean isDataEmpty, boolean isNoMoreData) {
        refreshScrollView.refreshLoadComplete();//结束刷新加载
        getLoadScrollView().hideFooter(isDataEmpty);//空数据隐藏footer
        setEmptyState(isDataEmpty ? VISIBLE : GONE);//空数据展示空view
        if (isDataEmpty || isNoMoreData) {
            getLoadScrollView().stopLoadMore();//关闭加载功能
        } else {
            getLoadScrollView().restoreLoadMore();//恢复加载功能
        }
    }

    /**
     * 改变emptyView的显示状态,并相应的通知改变刷新控件的滑动和加载控件的内容显示状态
     */
    private void setEmptyState(int visibleState) {
        View emptyView = getEmptyView();
        if (emptyView != null && emptyView.getVisibility() != visibleState) {
            emptyView.setVisibility(visibleState);
            refreshScrollView.setShowEmptyView(visibleState == VISIBLE);//通知刷新控件处理空数据
            getLoadScrollView().hideContent(visibleState == VISIBLE);//隐藏内容
            if (onEmptyViewListener != null) {
                if (visibleState == View.VISIBLE) {
                    onEmptyViewListener.onEmptyViewVisible(emptyView);
                } else if (visibleState == View.GONE) {
                    onEmptyViewListener.onEmptyViewGone(emptyView);
                }
            }
        }
    }

    /**
     * 默认的刷新加载的失败方法(不需要考虑展示空view以及加载功能的时候)
     */
    @Override
    public void refreshLoadError() {
        refreshLoadError(false, false);
    }

    /**
     * 刷新加载错误上报,并通知当前无数据
     */
    public void errorWithDataEmpty() {
        refreshLoadError(true, false);
    }

    /**
     * 刷新加载错误上报,并通知当无更多数据
     */
    public void errorWithNoMoreData() {
        refreshLoadError(false, true);
    }

    /**
     * 针对与ScrollView刷新加载的失败方法,可以通知数据的状态(因为在失败的请求后可能已经改变数据状态,此时应该通知view)
     *
     * @param isDataEmpty  数据是否为空
     * @param isNoMoreData 数据是否没有更多
     */
    private void refreshLoadError(boolean isDataEmpty, boolean isNoMoreData) {
        refreshScrollView.refreshLoadError();//分页回滚等操作
        refreshLoadComplete(isDataEmpty, isNoMoreData);//数据状态的更新处理
    }

}
