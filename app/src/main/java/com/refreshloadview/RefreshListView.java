package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ListAdapter;

import com.materialdesign.R;


/**
 * Created by cwj on 16/7/27.
 * 刷新加载ListView
 * 1.刷新和加载不同时进行,避免数据混乱
 * 2.支持首次进入刷新
 * 3.封装分页逻辑
 */
public class RefreshListView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, LoadListView.OnLoadListener, LoadListView.OnDataChangeListener, IRefreshLoadView {

    private LoadListView loadListView;

    private boolean canRefresh = true;//默认可刷新
    private boolean autoRefresh = true;//默认首次自动刷新

    private Page page;

    private IRefreshLoadView.OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, null);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initListener() {
        setOnRefreshListener(this);
        loadListView.setOnLoadListener(this);
        loadListView.addOnDataChangeListener(this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            canRefresh = typedArray.getBoolean(R.styleable.RefreshLoadView_canRefresh, true);
            autoRefresh = typedArray.getBoolean(R.styleable.RefreshLoadView_autoRefresh, true);
            int firstPageNo = typedArray.getInteger(R.styleable.RefreshLoadView_firstPageNo, Page.DEFAULT_FIRST_PAGE_NO);
            int pageSize = typedArray.getInteger(R.styleable.RefreshLoadView_pageSize, Page.DEFAULT_PAGE_SIZE);
            page = new Page(firstPageNo, pageSize);
            typedArray.recycle();
        } else {
            page = new Page();
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        //style
        this.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //canRefresh
        setEnabled(canRefresh);
        //添加loadView
        loadListView = new LoadListView(context, attrs);
        addView(loadListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //是否需要首次加载
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (autoRefresh) {
                    refresh();
                }
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 获取加载view
     */
    public LoadListView getLoadListView() {
        return loadListView;
    }

    @Override
    public void setOnEmptyViewListener(OnEmptyViewListener onEmptyViewListener) {
        //do nothing
    }

    @Override
    public void setOnRefreshListener(IRefreshLoadView.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void setOnLoadListener(IRefreshLoadView.OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public boolean isLoading() {
        return loadListView.isLoading();
    }

    @Override
    public int getPageNo() {
        return page.getPageNo();
    }

    @Override
    public int getPageSize() {
        return page.getPageSize();
    }

    @Override
    public void refresh() {
        if (isEnabled()) {//可以刷新时调用刷新(可重复刷新)
            setRefreshing(true);
            onRefresh();
        }
    }

    private void loadComplete() {
        loadListView.loadComplete();
    }

    private void refreshComplete() {
        if (isRefreshing()) {
            setRefreshing(false);
        }
    }

    @Override
    public void refreshLoadComplete() {
        refreshComplete();
        loadComplete();
    }

    @Override
    public void refreshLoadError() {
        page.prePage();//分页回滚
        refreshLoadComplete();//结束刷新加载
    }

    /**
     * 设置adapter
     */
    public void setAdapter(ListAdapter adapter) {
        loadListView.setAdapter(adapter);
    }

    @Override
    public void onDataChange(ListAdapter adapter, int dataCount) {
        int maxDataCount = (page.getPageNo() - page.getFirstPageNo() + 1) * page.getPageSize();
        if (dataCount < maxDataCount) {//小于当前页数应该有的数据数量,说明没有更多
            loadListView.stopLoadMore();//不能继续加载更多
        } else {
            loadListView.restoreLoadMore();//否则恢复加载(需要的话)
        }
    }

    @Override
    public void onRefresh() {
        if (loadListView.isLoading()) {//加载时可刷新(先要取消掉加载状态,为了保证数据准确性,最好外部控制网络请求为单一的)
            loadListView.loadComplete();
        }
        //置顶,重置page,回调刷新接口
        loadListView.smoothScrollToPosition(0);
        page.resetPage();
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    @Override
    public void onLoad() {
        if (isRefreshing()) {//刷新时不加载
            loadListView.loadComplete();
            return;
        }
        //page+1,回调加载接口
        page.nextPage();
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }
}
