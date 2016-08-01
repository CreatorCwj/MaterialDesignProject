package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.materialdesign.R;

/**
 * Created by cwj on 16/8/1.
 * 刷新view基类
 */
public abstract class RefreshBaseView<T extends ILoadView> extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, ILoadView.OnLoadListener, IRefreshLoadView {

    protected T loadView;

    private boolean canRefresh = true;//默认可刷新
    private boolean autoRefresh = true;//默认首次自动刷新

    protected Page page;

    private IRefreshLoadView.OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;

    public RefreshBaseView(Context context) {
        this(context, null);
    }

    public RefreshBaseView(Context context, AttributeSet attrs) {
        super(context, null);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
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
        loadView = createLoadView(context, attrs);
        if (!(loadView instanceof View)) {
            throw new IllegalArgumentException("loadView must be a view");
        }
        addView((View) loadView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
     * 创建加载view
     */
    protected abstract T createLoadView(Context context, AttributeSet attrs);

    /**
     * 获取loadView
     */
    public T getLoadView() {
        return loadView;
    }

    private void initListener() {
        setOnRefreshListener(this);
        loadView.setOnLoadListener(this);
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
        return loadView.isLoading();
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

    @Override
    public void refreshLoadComplete() {
        refreshComplete();
        loadComplete();
    }

    private void loadComplete() {
        loadView.loadComplete();
    }

    private void refreshComplete() {
        if (isRefreshing()) {
            setRefreshing(false);
        }
    }

    @Override
    public void refreshLoadError() {
        page.prePage();//分页回滚
        refreshLoadComplete();//结束刷新加载
    }

    @Override
    public void onLoad() {
        if (isRefreshing()) {//刷新时不加载
            loadView.loadComplete();
            return;
        }
        //page+1,回调加载接口
        page.nextPage();
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    @Override
    public void onRefresh() {
        if (loadView.isLoading()) {//加载时可刷新(先要取消掉加载状态,为了保证数据准确性,最好外部控制网络请求为单一的)
            loadView.loadComplete();
        }
        //置顶,重置page,回调刷新接口
        loadView.backToTop();
        page.resetPage();
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }
}
