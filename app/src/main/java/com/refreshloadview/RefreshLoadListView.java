package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.materialdesign.R;


/**
 * Created by cwj on 16/7/28.
 * 刷新加载控件
 * 1.添加空view(可设置xml,也可代码多次改变)
 * 2.需要用到子view方法,通过getXxxView拿到后自行调用即可,这里只提供通用方法
 * 3.todo:整套view支持xml布局,代码生成的方式并不完善
 */
public class RefreshLoadListView extends FrameLayout implements LoadListView.OnDataChangeListener, IRefreshLoadView {

    private RefreshListView refreshListView;
    private ViewStub viewStub;
    private View emptyView;

    private int emptyViewId = -1;

    private OnEmptyViewListener onEmptyViewListener;

    public RefreshLoadListView(Context context) {
        this(context, null);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            emptyViewId = typedArray.getResourceId(R.styleable.RefreshLoadView_emptyView, -1);
            typedArray.recycle();
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        //先添加emptyView,防止emptyView有背景时显示在刷新控件前面,下拉效果看不到
        if (emptyViewId != -1) {
            viewStub = new ViewStub(context, emptyViewId);
            addView(viewStub);
        }
        //添加刷新加载控件
        refreshListView = new RefreshListView(context, attrs);
        addView(refreshListView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 获取刷新加载view
     */
    public RefreshListView getRefreshListView() {
        return refreshListView;
    }

    /**
     * 获取加载view
     */
    public LoadListView getLoadListView() {
        return refreshListView.getLoadListView();
    }

    /**
     * 获取空view(还没有inflate要inflate)
     */
    public View getEmptyView() {
        if (emptyView == null && viewStub != null) {
            emptyView = viewStub.inflate();
            emptyView.setVisibility(GONE);//先隐藏view
        }
        return emptyView;
    }

    /**
     * 手动设置空view,可多次设置
     */
    public void setEmptyView(View emptyView) {
        //移除之前的
        if (this.emptyView != null && this.emptyView.getParent() != null && this.emptyView.getParent() == this) {
            removeView(this.emptyView);
        }
        //放入新的emptyView(先从父view移除)
        this.emptyView = emptyView;
        if (emptyView.getParent() != null && (emptyView.getParent() instanceof ViewGroup)) {
            ((ViewGroup) emptyView.getParent()).removeView(emptyView);
        }
        addView(emptyView, 0);//添加到最下面,防止有背景时使得刷新控件无效果
        this.emptyView.setVisibility(GONE);//先隐藏view
        //刷新一下,因为可能emptyView已经更换但是还没有展示
        ListAdapter adapter = getLoadListView().getAdapter();
        if (adapter != null && (adapter instanceof BaseAdapter)) {
            ((BaseAdapter) adapter).notifyDataSetChanged();
        }
    }

    private void initListener() {
        getLoadListView().addOnDataChangeListener(this);
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

    @Override
    public void setOnEmptyViewListener(IRefreshLoadView.OnEmptyViewListener onEmptyViewListener) {
        this.onEmptyViewListener = onEmptyViewListener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        refreshListView.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        refreshListView.setOnLoadListener(onLoadListener);
    }

    @Override
    public boolean isLoading() {
        return refreshListView.isLoading();
    }

    @Override
    public boolean isRefreshing() {
        return refreshListView.isRefreshing();
    }

    @Override
    public int getPageNo() {
        return refreshListView.getPageNo();
    }

    @Override
    public int getPageSize() {
        return refreshListView.getPageSize();
    }

    @Override
    public void refresh() {
        refreshListView.refresh();
    }

    @Override
    public void refreshLoadComplete() {
        refreshListView.refreshLoadComplete();
    }

    @Override
    public void refreshLoadError() {
        refreshListView.refreshLoadError();
    }

    /**
     * 设置adapter
     */
    public void setAdapter(ListAdapter adapter) {
        refreshListView.setAdapter(adapter);
    }
}
