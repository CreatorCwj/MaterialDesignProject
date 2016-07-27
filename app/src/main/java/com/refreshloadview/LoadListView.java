package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.materialdesign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/7/27.
 * 可加载的ListView
 * 1.可设置是否需要加载
 * 2.无数据不显示footer
 * 3.可配置背景色和divider样式
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    private FooterLayout footerLayout;

    private boolean isLoading;

    private boolean originCanLoadMore = true;
    private boolean canLoadMore = true;//默认可加载
    private int loadViewBackground;//背景颜色
    private int loadViewDividerColor;//分割线颜色
    private int loadViewDividerHeight;//分割线高度

    private OnScrollListener onScrollListener;
    private OnLoadListener onLoadListener;
    private List<OnDataChangeListener> onDataChangeListeners;

    interface OnLoadListener {
        void onLoad();
    }

    public interface OnDataChangeListener {
        void onDataChange(ListAdapter adapter, int dataCount);
    }

    public LoadListView(Context context) {
        this(context, null);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, null, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            originCanLoadMore = canLoadMore = typedArray.getBoolean(R.styleable.RefreshLoadView_canLoadMore, true);
            loadViewBackground = typedArray.getColor(R.styleable.RefreshLoadView_loadViewBackgroundColor, Color.TRANSPARENT);
            loadViewDividerColor = typedArray.getColor(R.styleable.RefreshLoadView_loadViewDividerColor, Color.TRANSPARENT);
            loadViewDividerHeight = typedArray.getDimensionPixelSize(R.styleable.RefreshLoadView_loadViewDividerHeight, 0);
            typedArray.recycle();
        }
    }

    private void initListener() {
        setOnScrollListener(this);
    }

    /**
     * 重写设置滚动监听,保证不覆盖自身所需监听器
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l == this) {//当前view的注册
            super.setOnScrollListener(l);
        } else {
            onScrollListener = l;//自定义的listener
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        //初始可加载的话,添加footer
        if (originCanLoadMore) {
            footerLayout = new FooterLayout(context, attrs);
            addFooterView(footerLayout, null, false);
            footerLayout.onResetState();
        }
        //设置属性
        setSelector(android.R.color.transparent);
        setBackgroundColor(loadViewBackground);
        setDivider(new ColorDrawable(loadViewDividerColor));
        setDividerHeight(loadViewDividerHeight);
    }

    /**
     * 设置加载监听器
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 添加数据改变监听器
     */
    public void addOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        if (onDataChangeListeners == null) {
            onDataChangeListeners = new ArrayList<>();
        }
        onDataChangeListeners.add(onDataChangeListener);
    }

    /**
     * 重写设置adapter,每次设置会向adapter注册数据改变监听,且会取消之前adapter注册的监听
     * 每次设置后要调用一次监听,保证UI根据数据来正确显示
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (getAdapter() != null) {//取消前一个adapter的注册
            getAdapter().unregisterDataSetObserver(dataSetObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerDataSetObserver(dataSetObserver);
            if (adapter instanceof BaseAdapter) {
                ((BaseAdapter) adapter).notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (getAdapter() != null) {//离开window时取消注册
            getAdapter().unregisterDataSetObserver(dataSetObserver);
        }
        super.onDetachedFromWindow();
    }

    //数据改变监听
    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (getAdapter() != null) {
                int dataCount = getAdapter().getCount() - getHeaderViewsCount() - getFooterViewsCount();//刨除header和footer的数据数量
                if (dataCount > 0 && footerLayout != null && footerLayout.getVisibility() != VISIBLE) {//有数据显示底部
                    footerLayout.setVisibility(VISIBLE);
                } else if (dataCount == 0 && footerLayout != null && footerLayout.getVisibility() != GONE) {//无数据隐藏底部
                    footerLayout.setVisibility(GONE);
                }
                //notify
                if (onDataChangeListeners != null) {
                    for (OnDataChangeListener listener : onDataChangeListeners) {
                        listener.onDataChange(getAdapter(), dataCount);
                    }
                }
            }
        }
    };

    /**
     * 无法继续加载
     */
    void stopLoadMore() {
        if (canLoadMore) {
            loadComplete();//结束加载
            canLoadMore = false;//置为不可加载
            footerLayout.onLoadStop();
        }
    }

    /**
     * 恢复可加载状态(初始设置可加载的话)
     */
    void restoreLoadMore() {
        if (originCanLoadMore && !canLoadMore) {//初始时可加载,当前不可加载(从不可加载变过来)
            canLoadMore = true;//置为可加载
            footerLayout.onResetState();
        }
    }

    /**
     * 是否处于加载状态
     */
    public boolean isLoading() {
        return isLoading;
    }

    private boolean shouldLoad() {
        if (!canLoadMore || isLoading() || getAdapter() == null) {
            return false;
        }
        int footerPos = getAdapter().getCount() - 1;//footer的pos
        int dataCount = getAdapter().getCount() - getFooterViewsCount() - getHeaderViewsCount();//数据总数刨除所有的header和footer
        return dataCount > 0 && getLastVisiblePosition() >= footerPos;//有数据且滚动到最底部时可以刷新
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE://空闲时,如果可以加载,调用加载更多
                if (shouldLoad()) {
                    startLoad();
                }
                break;
        }
        if (onScrollListener != null) {//回调自定义的listener
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * 加载完成
     */
    void loadComplete() {
        if (isLoading()) {
            isLoading = false;
            footerLayout.onResetState();
        }
    }

    private void startLoad() {
        if (!isLoading()) {//加载时不再加载
            isLoading = true;
            footerLayout.onLoadingState();
            //notify listener
            if (onLoadListener != null) {
                onLoadListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {//回调自定义的listener
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
