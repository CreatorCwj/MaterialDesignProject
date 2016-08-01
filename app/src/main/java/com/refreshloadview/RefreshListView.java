package com.refreshloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;


/**
 * Created by cwj on 16/7/27.
 * 刷新加载ListView
 * 1.刷新和加载不同时进行,避免数据混乱
 * 2.支持首次进入刷新
 * 3.封装分页逻辑
 */
public class RefreshListView extends RefreshBaseView<LoadListView> implements LoadListView.OnDataChangeListener {

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView.addOnDataChangeListener(this);
    }

    @Override
    protected LoadListView createLoadView(Context context, AttributeSet attrs) {
        return new LoadListView(context, attrs);
    }

    /**
     * 设置adapter
     */
    public void setAdapter(ListAdapter adapter) {
        loadView.setAdapter(adapter);
    }

    @Override
    public void onDataChange(ListAdapter adapter, int dataCount) {
        int maxDataCount = (page.getPageNo() - page.getFirstPageNo() + 1) * page.getPageSize();
        if (dataCount < maxDataCount) {//小于当前页数应该有的数据数量,说明没有更多
            loadView.stopLoadMore();//不能继续加载更多
        } else {
            loadView.restoreLoadMore();//否则恢复加载(需要的话)
        }
    }

}
