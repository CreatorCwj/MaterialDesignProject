package com.widget.refreshloadview;

/**
 * Created by cwj on 16/7/22.
 * Header统一接口
 */
public interface HeaderController {

    /**
     * 重置状态处理方法
     */
    void onResetState();

    /**
     * 刷新时处理方法
     */
    void onRefreshState();

    /**
     * 下拉刷新处理方法
     */
    void onPullToRefreshState();

    /**
     * 释放刷新处理方法
     */
    void onReleaseToRefreshState();
}
