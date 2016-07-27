package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.materialdesign.R;


/**
 * Created by cwj on 16/7/27.
 * 刷新加载ScrollView
 * 1.刷新和加载不同时进行,避免数据混乱
 * 2.支持首次进入刷新
 * 3.封装分页逻辑
 */
public class RefreshScrollView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, LoadScrollView.OnLoadListener, IRefreshLoadView {

    private LoadScrollView loadScrollView;

    private boolean canRefresh = true;//默认可刷新
    private boolean autoRefresh = true;//默认首次自动刷新

    private Page page;

    private IRefreshLoadView.OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;

    private boolean showEmptyView;//是否展示了空view

    public RefreshScrollView(Context context) {
        this(context, null);
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, null);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initListener() {
        setOnRefreshListener(this);
        loadScrollView.setOnLoadListener(this);
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
        loadScrollView = new LoadScrollView(context, attrs);
        addView(loadScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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

    void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }

    @Override
    public boolean canChildScrollUp() {
        //展示空view情况下直接可以下拉刷新
        return !showEmptyView && super.canChildScrollUp();
    }

    /**
     * 获取加载view
     */
    public LoadScrollView getLoadScrollView() {
        return loadScrollView;
    }

    @Override
    public void setOnEmptyViewListener(OnEmptyViewListener onEmptyViewListener) {
        // do nothing
    }

    @Override
    public void setOnRefreshListener(IRefreshLoadView.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public boolean isLoading() {
        return loadScrollView.isLoading();
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
        loadScrollView.loadComplete();
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

    @Override
    public void onRefresh() {
        if (loadScrollView.isLoading()) {//加载时可刷新(先要取消掉加载状态,为了保证数据准确性,最好外部控制网络请求为单一的)
            loadScrollView.loadComplete();
        }
        loadScrollView.restoreLoadMore();//恢复加载功能(需要的话)
        //置顶,重置page,回调刷新接口
        loadScrollView.scrollTo(0, 0);
        page.resetPage();
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    @Override
    public void onLoad() {
        if (isRefreshing()) {//刷新时不加载
            loadScrollView.loadComplete();
            return;
        }
        //page+1,回调加载接口
        page.nextPage();
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }
}
