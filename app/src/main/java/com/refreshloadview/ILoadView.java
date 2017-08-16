package com.refreshloadview;

/**
 * Created by cwj on 16/8/1.
 */
public interface ILoadView {

    interface OnLoadListener {
        void onLoad();
    }

    /**
     * 设置加载监听器
     */
    void setOnLoadListener(OnLoadListener onLoadListener);

    /**
     * 无法继续加载
     */
    void stopLoadMore();

    /**
     * 恢复可加载状态(初始设置可加载的话)
     */
    void restoreLoadMore();

    /**
     * 是否处于加载状态
     */
    boolean isLoading();

    /**
     * 加载完成
     */
    void loadComplete();

    /**
     * 回顶的方法
     */
    void backToTop();
}
