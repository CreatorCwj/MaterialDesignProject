package com.widget.pinned2;

import android.support.annotation.NonNull;

/**
 * Created by cwj on 16/11/24.
 * 吸顶控件接口父类
 * 每个实现pinned的子类,需要通过OnPinnedScrollListener,将自己的滚动事件回调给吸顶容器
 */
public interface BaseIPinned {

    interface OnPinnedScrollListener {
        void onPinnedScroll();
    }

    void setIPinnedScrollListener(@NonNull OnPinnedScrollListener onPinnedScrollListener);
}
