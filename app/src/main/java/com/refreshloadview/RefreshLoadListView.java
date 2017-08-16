package com.refreshloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;


/**
 * Created by cwj on 16/7/28.
 * 刷新加载ListView控件
 * 1.添加空view(可设置xml,也可代码多次改变)
 * 2.需要用到listview方法,通过getLoadListView拿到后自行调用即可,这里只提供通用方法
 * 3.todo:整套view支持xml布局,代码生成的方式并不完善
 */
public class RefreshLoadListView extends RefreshLoadBaseView<RefreshListView> implements LoadListView.OnDataChangeListener {

    public RefreshLoadListView(Context context) {
        this(context, null);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getLoadListView().addOnDataChangeListener(this);
    }

    @Override
    protected RefreshListView createRefreshView(Context context, AttributeSet attrs) {
        return new RefreshListView(context, attrs);
    }

    /**
     * 获取加载view
     */
    public LoadListView getLoadListView() {
        return refreshView.getLoadView();
    }

    /**
     * 手动设置空view,可多次设置
     */
    @Override
    public void setEmptyView(View emptyView) {
        super.setEmptyView(emptyView);
        //刷新一下,因为可能emptyView已经更换但是还没有展示
        ListAdapter adapter = getLoadListView().getAdapter();
        if (adapter != null && (adapter instanceof BaseAdapter)) {
            ((BaseAdapter) adapter).notifyDataSetChanged();
        }
    }

    @Override
    public void onDataChange(ListAdapter adapter, int dataCount) {
        View emptyView = getEmptyView();
        if (dataCount == 0) {//没有数据则显示空view(有的话)
            if (emptyView != null && emptyView.getVisibility() != VISIBLE) {
                emptyView.setVisibility(VISIBLE);
                if (onEmptyViewListener != null) {
                    onEmptyViewListener.onEmptyViewVisible(emptyView);
                }
            }
        } else if (emptyView != null && emptyView.getVisibility() != GONE) {//隐藏空view
            emptyView.setVisibility(GONE);
            if (onEmptyViewListener != null) {
                onEmptyViewListener.onEmptyViewGone(emptyView);
            }
        }
    }

    /**
     * 设置adapter
     */
    public void setAdapter(ListAdapter adapter) {
        refreshView.setAdapter(adapter);
    }
}
