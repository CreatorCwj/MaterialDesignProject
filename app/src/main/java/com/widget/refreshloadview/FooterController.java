package com.widget.refreshloadview;

/**
 * Created by cwj on 16/7/21.
 * Footer的统一接口
 */
public interface FooterController {

    /**
     * 空闲时处理方法
     */
    void onResetState();

    /**
     * 加载时处理方法
     */
    void onLoadingState();

    /**
     * 无法加载更多
     */
    void onLoadStop();

}
