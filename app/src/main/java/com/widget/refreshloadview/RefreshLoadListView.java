package com.widget.refreshloadview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by cwj on 16/7/21.
 * ListView可刷新加载控件
 */
public class RefreshLoadListView extends RefreshBaseAbsListView<LoadListView> {

    public RefreshLoadListView(Context context) {
        super(context);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLoadViewCreated(@NonNull LoadListView loadView) {

    }

    @Override
    protected LoadListView onCreateLoadView(Context context, AttributeSet attrs) {
        return new LoadListView(context, attrs);
    }

}
