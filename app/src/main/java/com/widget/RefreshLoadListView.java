package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

import com.materialdesign.R;
import com.widget.refreshloadview.FooterLayout;
import com.widget.refreshloadview.HeaderLayout;

/**
 * Created by cwj on 16/7/25.
 * 刷新加载ListView
 * 1.刷新和加载不同时进行,避免数据混乱
 * 2.支持刷新时候不能滑动
 * 3.支持首次进入刷新
 * 4.todo:封装分页逻辑,请求逻辑
 * 5.todo:可配置header和footer
 * 6.todo:不够一页时footer隐藏,上拉会出现(非全部加载完的时候)
 */
public class RefreshLoadListView extends ListView implements AbsListView.OnScrollListener, OnRefreshListener, OnLoadListener {

    private static final float FRICTION = 2.0f;//系数
    private static final int SCROLL_DURATION = 225;//滚动时间

    private static final int RESET = 0x1;//重置状态(初始,刷新完成)
    private static final int PULL_TO_REFRESH = 0x2;//下拉刷新
    private static final int RELEASE_TO_REFRESH = 0x3;//释放刷新
    private static final int REFRESHING = 0x4;//刷新中

    private int state = RESET;
    private boolean isLoading;

    private FooterLayout footerLayout;
    private HeaderLayout headerLayout;

    private Scroller scroller;
    private float lastY;

    private boolean canRefresh = true;//默认可刷新
    private boolean autoRefresh = true;//默认可自动刷新
    private boolean canScrollWhenRefresh = true;//默认刷新时可以移动
    private boolean canLoadMore = true;//默认可加载
    private HeaderLayout.HeaderStyle headerMode;//header样式

    private OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;

    public RefreshLoadListView(Context context) {
        this(context, null);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initListener() {
        setOnScrollListener(this);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            canRefresh = typedArray.getBoolean(R.styleable.RefreshLoadView_canRefresh, true);
            autoRefresh = typedArray.getBoolean(R.styleable.RefreshLoadView_autoRefresh, true);
            canScrollWhenRefresh = typedArray.getBoolean(R.styleable.RefreshLoadView_canScrollWhenRefresh, true);
            canLoadMore = typedArray.getBoolean(R.styleable.RefreshLoadView_canLoadMore, true);
            headerMode = HeaderLayout.HeaderStyle.getStyle(typedArray.getInt(R.styleable.RefreshLoadView_headerMode, HeaderLayout.HeaderStyle.CLASSIC.getId()));
            typedArray.recycle();
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        //添加header
        if (canRefresh) {
            headerLayout = headerMode.getHeaderLayout(context, attrs);
            addHeaderView(headerLayout, null, false);
            headerLayout.setVisibleHeight(0);//初始隐藏
            headerLayout.onResetState();
        }
        //添加footer
        if (canLoadMore) {
            footerLayout = new FooterLayout(context, attrs);
            addFooterView(footerLayout, null, false);
            footerLayout.onResetState();
        }
        //是否首次刷新
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (canRefresh && autoRefresh) {
                    refresh();
                }
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (isRefreshing() && !canScrollWhenRefresh) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();//记录初始位置
                break;
            case MotionEvent.ACTION_MOVE:
                if (!scroller.isFinished()) {//移动时要关闭之前可能还存在的滚动
                    scroller.forceFinished(true);
                }
                float deltaY = ev.getY() - lastY;//移动距离
                if (canDrag(deltaY)) {
                    handlePull(deltaY / FRICTION);//根据比例算出移动距离,进行处理
                }
                lastY = ev.getY();//记录最新位置
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                lastY = -1; //重置Y
                handleUp();//处理释放动作
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean canDrag(float deltaY) {
        //没有header不可拖拽
        return headerLayout != null && getFirstVisiblePosition() == 0 && ((headerLayout.getVisibleHeight() > headerLayout.getContentHeight())//多余偏移量时可以移动
                || (isRefreshing() && (headerLayout.getTop() == 0) && (deltaY > 0))//当刷新时,header置顶时可以往下拉
                || ((headerLayout.getVisibleHeight() > 0) && !isRefreshing())//不刷新时的拖动
                || ((headerLayout.getVisibleHeight() == 0) && (deltaY > 0)));
    }

    private void handleUp() {
        //改变状态
        if (state == RELEASE_TO_REFRESH && !isLoading()) {//释放刷新变为刷新状态(加载时不刷新)
            setState(REFRESHING);
        }
        //重置header
        resetHeaderHeight();
    }

    private void resetHeaderHeight() {
        if (headerLayout == null) {
            return;
        }
        int height = headerLayout.getVisibleHeight();
        if (height == 0) {//已经为0则不用重置
            return;
        }
        if (isRefreshing() && height <= headerLayout.getContentHeight()) {//刷新状态且没有超过下拉刷新的高度时不用重置
            return;
        }
        if (!isLoading() && height > headerLayout.getContentHeight()) {//超过下拉刷新高度时,要回到下拉刷新的高度(准备刷新);正在加载的话则回到高度为0
            scrollHeader(true);
        } else {//否则回到0
            scrollHeader(false);
        }
    }

    private void scrollHeader(boolean backToRefresh) {
        if (headerLayout == null) {
            return;
        }
        int height = headerLayout.getVisibleHeight();
        if (backToRefresh) {//回到刷新状态
            scroller.startScroll(0, height, 0, headerLayout.getContentHeight() - height, SCROLL_DURATION);
        } else {//回到高度为0状态
            scroller.startScroll(0, height, 0, -height, SCROLL_DURATION);
        }
        invalidate();//触发滚动
    }

    private void handlePull(float delta) {
        if (headerLayout == null) {
            return;
        }
        int newHeight = (int) delta + headerLayout.getVisibleHeight();//新高度
        if (isRefreshing() && newHeight < headerLayout.getContentHeight()) {//刷新时header高度为一定的
            newHeight = headerLayout.getContentHeight();
        }
        headerLayout.setVisibleHeight(newHeight);//改变高度
        setSelection(0); //置顶
        //改变状态
        if (isRefreshing()) {//刷新状态不作处理
            return;
        }
        //通知scalePull方法
        float scale = (float) headerLayout.getVisibleHeight() / headerLayout.getContentHeight();
        headerLayout.onScalePull(scale);
        if (state != RESET && headerLayout.getVisibleHeight() == 0) {//重置状态
            setState(RESET);
        } else if (state != PULL_TO_REFRESH && headerLayout.getVisibleHeight() <= headerLayout.getContentHeight()) {//变为下拉刷新
            setState(PULL_TO_REFRESH);
        } else if (state != RELEASE_TO_REFRESH && headerLayout.getVisibleHeight() > headerLayout.getContentHeight()) {//变为释放刷新
            setState(RELEASE_TO_REFRESH);
        }
    }

    private void setState(int state) {
        this.state = state;
        if (headerLayout == null) {
            return;
        }
        switch (state) {
            case RESET:
                headerLayout.onResetState();
                break;
            case PULL_TO_REFRESH:
                headerLayout.onPullToRefreshState();
                break;
            case RELEASE_TO_REFRESH:
                headerLayout.onReleaseToRefreshState();
                break;
            case REFRESHING:
                headerLayout.onRefreshState();
                this.onRefresh();
                break;
        }
    }

    /**
     * 手动调用刷新
     */
    public void refresh() {
        if (!canRefresh || isRefreshing() || isLoading()) {//不能刷新或者正在刷新或者正在加载
            return;
        }
        setSelection(0);//置顶
        setState(REFRESHING);
        scrollHeader(true);
    }

    /**
     * 刷新结束
     */
    public void refreshComplete() {
        if (!isRefreshing()) {
            return;
        }
        setState(RESET);
        scrollHeader(false);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {//正在计算中
            if (headerLayout != null) {
                headerLayout.setVisibleHeight(scroller.getCurrY());//改变为当前高度
            }
            if (scroller.getCurrY() == 0 && scroller.getCurrY() == scroller.getFinalY()) {//变为重置状态
                setState(RESET);
            }
            postInvalidate();//继续计算滚动
        }
        super.computeScroll();
    }

    public boolean isRefreshing() {
        return state == REFRESHING;
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                if (canLoadMore()) {//空闲时,如果可以加载,调用加载更多
                    startLoad();
                }
                break;
        }
    }

    /**
     * 外部调用结束加载
     */
    public void loadComplete() {
        if (isLoading()) {
            isLoading = false;
            if (footerLayout != null) {
                footerLayout.onResetState();
            }
        }
    }

    private void startLoad() {
        isLoading = true;
        if (footerLayout != null) {
            footerLayout.onLoadingState();
        }
        this.onLoad();
    }

    private boolean canLoadMore() {
        if (!canLoadMore || isRefreshing() || isLoading() || getAdapter() == null) {
            return false;
        }
        int footerPos = getAdapter().getCount() - 1;//footer的pos
        int dataCount = getAdapter().getCount() - getFooterViewsCount() - getHeaderViewsCount();//数据总数刨除所有的header和footer
        return dataCount > 0 && getLastVisiblePosition() >= footerPos;//无数据不刷新
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //do nothing
    }

    @Override
    public void onLoad() {
        //分页等处理
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    @Override
    public void onRefresh() {
        //分页重置等处理
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }
}
