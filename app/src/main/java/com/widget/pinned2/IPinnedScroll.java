package com.widget.pinned2;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by cwj on 16/11/23.
 * 普通滑动组件的吸顶接口
 */
public interface IPinnedScroll extends BaseIPinned {

    /**
     * 是否到达吸顶状态
     */
    boolean shouldPinned(@NonNull View anchorView);
}
