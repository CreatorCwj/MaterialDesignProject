package com.widget.pinned2;

import android.view.View;

/**
 * Created by cwj on 16/11/23.
 * 列表组件的吸顶接口
 */
public interface IPinnedList extends BaseIPinned {

    /**
     * 是否到达吸顶状态
     *
     * @param relativeDataPos anchorPos是否是基于数据集合的pos
     */
    boolean shouldPinned(int anchorPos, boolean relativeDataPos);

    /**
     * 根据anchorPos获取headerView,在HEADER_MODE_LIST_ITEM模式下使用到
     * anchorPos是相对于数据集合的pos
     */
    View getHeaderViewByAnchor(int anchorPos);
}
