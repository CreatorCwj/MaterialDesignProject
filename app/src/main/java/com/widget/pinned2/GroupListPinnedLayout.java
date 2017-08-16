package com.widget.pinned2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cwj on 16/11/24.
 * 分组列表组件吸顶容器
 */
public class GroupListPinnedLayout extends BaseListPinnedLayout<IPinnedGroupList> {

    private int[] offset = new int[1];//放置header偏移量的数组,大小为1

    public GroupListPinnedLayout(Context context) {
        this(context, null);
    }

    public GroupListPinnedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupListPinnedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getHeaderViewByList(@NonNull IPinnedGroupList pinnedView) {
        return pinnedView.getHeaderViewByGroup();
    }

    @Override
    protected int getHeaderViewPosition(@NonNull IPinnedGroupList pinnedView) {
        return pinnedView.getGroupPosition();
    }

    @Override
    protected boolean shouldPinned(@NonNull IPinnedGroupList pinnedView) {
        if (headerMode == HEADER_MODE_CUSTOM) {
            return pinnedView.shouldPinned(anchorPos, false);
        } else if (headerMode == HEADER_MODE_ITEM) {
            return pinnedView.shouldPinnedByGroup();
        }
        return false;
    }

    @Override
    protected boolean anchorValid() {
        return headerMode == HEADER_MODE_ITEM || anchorPos >= 0;//item模式时与anchor无关
    }
}
