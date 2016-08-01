package com.refreshloadview;

/**
 * Created by cwj on 16/7/28.
 * 刷新加载view共有方法接口
 */
public interface IRefreshLoadView {

    interface OnRefreshListener {
        void onRefresh();
    }

    interface OnLoadListener {
        void onLoad();
    }

    /**
     * 设置刷新监听
     */
    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    /**
     * 设置加载监听
     */
    void setOnLoadListener(OnLoadListener onLoadListener);

    /**
     * 是否在加载
     */
    boolean isLoading();

    /**
     * 是否在刷新
     */
    boolean isRefreshing();

    /**
     * 得到当前页数
     */
    int getPageNo();

    /**
     * 得到每页大小
     */
    int getPageSize();

    /**
     * 手动调用刷新
     */
    void refresh();

    /**
     * 结束刷新加载
     */
    void refreshLoadComplete();

    /**
     * 上报错误,分页回滚,结束刷新加载
     */
    void refreshLoadError();
}
