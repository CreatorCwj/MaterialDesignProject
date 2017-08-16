package com.refreshloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by cwj on 16/7/28.
 * 刷新加载ScrollView控件
 * 1.添加空view(可设置xml,也可代码多次改变)
 * 2.需要用到scrollView方法,通过getLoadScrollView拿到后自行调用即可,这里只提供通用方法
 * 3.todo:整套view支持xml布局,代码生成的方式并不完善
 */
public class RefreshLoadScrollView extends RefreshLoadBaseView<RefreshScrollView> {

    public RefreshLoadScrollView(Context context) {
        this(context, null);
    }

    public RefreshLoadScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RefreshScrollView createRefreshView(Context context, AttributeSet attrs) {
        return new RefreshScrollView(context, attrs);
    }

    /**
     * 获取加载view
     */
    public LoadScrollView getLoadScrollView() {
        return refreshView.getLoadView();
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
        refreshView.refreshLoadComplete();//结束刷新加载
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
            refreshView.setShowEmptyView(visibleState == VISIBLE);//通知刷新控件处理空数据
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
        refreshView.refreshLoadError();//分页回滚等操作
        refreshLoadComplete(isDataEmpty, isNoMoreData);//数据状态的更新处理
    }

}
