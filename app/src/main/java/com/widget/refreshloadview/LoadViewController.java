package com.widget.refreshloadview;

import android.view.View;

/**
 * Created by cwj on 16/7/21.
 * 加载控件的通用接口
 */
public interface LoadViewController{

    interface OnLoadListener {
        void onLoad();
    }
}
