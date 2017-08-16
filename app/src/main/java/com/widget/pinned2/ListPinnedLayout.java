package com.widget.pinned2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cwj on 16/11/23.
 * 列表组件的吸顶容器
 */
public class ListPinnedLayout extends BaseListPinnedLayout<IPinnedList> {

    public ListPinnedLayout(Context context) {
        this(context, null);
    }

    public ListPinnedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPinnedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean shouldPinned(@NonNull IPinnedList pinnedView) {
        return pinnedView.shouldPinned(anchorPos, headerMode == HEADER_MODE_ITEM);
    }

    @Override
    protected View getHeaderViewByList(@NonNull IPinnedList pinnedView) {
        if (anchorPos >= 0) {
            return pinnedView.getHeaderViewByAnchor(anchorPos);
        }
        return null;
    }

    @Override
    protected int getHeaderViewPosition(@NonNull IPinnedList pinnedView) {
        return anchorPos;
    }

    @Override
    protected boolean anchorValid() {
        return anchorPos >= 0;
    }

}
