package com.widget.refreshloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by cwj on 16/7/21.
 * AbsListView加载控件基类
 */
public abstract class LoadBaseAbsListView<T extends AbsListView> extends LoadBaseView<T> implements AbsListView.OnScrollListener {

    public LoadBaseAbsListView(Context context) {
        this(context, null);
    }

    public LoadBaseAbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadBaseAbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (loadView != null) {
            loadView.setOnScrollListener(this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                if (shouldLoad()) {//空闲时,如果可以加载,调用加载更多
                    startLoad();
                }
                break;
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (loadView != null) {
            loadView.setAdapter(adapter);
        }
    }

    /**
     * 判断是否应该加载
     */
    protected abstract boolean shouldLoad();

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
