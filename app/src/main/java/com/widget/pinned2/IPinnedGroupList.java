package com.widget.pinned2;

import android.view.View;

/**
 * Created by cwj on 16/11/23.
 * 列表组件的吸顶接口
 */
public interface IPinnedGroupList extends IPinnedList {

    /**
     * 需要获取当前位置的headerView,以及其被下一个headerView推动的offset
     */
    View getHeaderViewByGroup();

    /**
     * 获取当前headerView的groupPosition
     */
    int getGroupPosition();

    /**
     * 是否吸顶(ITEM模式下)
     */
    boolean shouldPinnedByGroup();
}
