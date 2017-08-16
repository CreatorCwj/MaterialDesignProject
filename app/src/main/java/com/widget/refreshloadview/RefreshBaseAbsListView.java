package com.widget.refreshloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by cwj on 16/7/21.
 * AbsListView刷新加载控件基类
 */
public abstract class RefreshBaseAbsListView<T extends LoadBaseAbsListView> extends RefreshBaseView<T> {

    public RefreshBaseAbsListView(Context context) {
        super(context);
    }

    public RefreshBaseAbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshBaseAbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean isReadyForPull() {
        AbsListView absListView = null;
        View view = loadView.getLoadView();
        if (view instanceof AbsListView) {//一定是AbsListView的子类
            absListView = (AbsListView) view;
        }
        if (absListView == null) {
            return false;
        }
        ListAdapter adapter = absListView.getAdapter();
        if (adapter == null || adapter.isEmpty()) {//无数据,可下拉刷新
            return true;
        }
        if (absListView.getFirstVisiblePosition() <= 1) {
            View firstChild = absListView.getChildAt(0);
            if (firstChild != null) {//在AbsListView最顶部可下拉刷新
                return firstChild.getTop() >= absListView.getTop();
            }
        }
        return false;
    }

    public void setAdapter(ListAdapter adapter) {
        loadView.setAdapter(adapter);
    }
}
