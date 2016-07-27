package com.widget.refreshloadview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by cwj on 16/7/21.
 * 刷新控件基类
 */
public abstract class RefreshBaseView<T extends LoadBaseView> extends LinearLayout {

    private enum RefreshState {
        RESET,//空闲
        PULL_TO_REFRESH,//下拉过程中
        RELEASE_TO_REFRESH,//释放刷新
        REFRESHING//刷新中
    }

    private final float FRICTION = 2.0f;

    protected T loadView;
    private HeaderLayout headerView;

    private RefreshState refreshState = RefreshState.RESET;

    private float initialY;
    private float lastY;

    public RefreshBaseView(Context context) {
        this(context, null);
    }

    public RefreshBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //attrs
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        //创建加载控件
        loadView = onCreateLoadView(context, attrs);
        if (loadView == null) {
            return;
        }
        if (loadView.getParent() == null) {//未添加则添加view
            addView(loadView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        onLoadViewCreated(loadView);
        //添加header
        //todo 定制化
//        headerView = new HeaderLayout(context, attrs);
        addView(headerView, 0, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * loadView创建完成
     */
    protected abstract void onLoadViewCreated(@NonNull T loadView);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateViewSize();//尺寸变化时要重新设置大小和位置
    }

    private void updateViewSize() {
        int headerHeight = getMaxHeaderHeight();//header高度
        headerView.setVisibleHeight(headerHeight);//设置header高度
        setPadding(getPaddingLeft(), -headerHeight, getPaddingRight(), getPaddingBottom());//设置初始位置
    }

    private int getMaxHeaderHeight() {
        return (int) (Math.round(getHeight() / FRICTION) * 1.2f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY(ev);//初始化起点
                return false;
            case MotionEvent.ACTION_MOVE:
                lastY = ev.getY();//记录最新位置
                if (isReadyForPull()) {//可以下拉
                    if (refreshState == RefreshState.RESET) {//当前没有在拖拽状态
                        if (lastY > initialY) {//向下滑动
                            initY(ev);
                            return true;
                        }
                    }
                    if (refreshState == RefreshState.REFRESHING) {
                        initY(ev);
                        return true;
                    }
                }
                return false;//不可以下拉则继续交由子类处理
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetY();//重置位置
                return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY(event);//初始化起点
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInvalidY()) {//赋值起始点
                    initY(event);
                } else {//计算移动距离并处理
                    lastY = event.getY();
                    handlePull();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回滚至原位(需要的话)
                handleUp();
                resetY();//重置位置
                break;
        }
        return true;
    }

    private void handleUp() {
        switch (refreshState) {
            case PULL_TO_REFRESH:
                setState(RefreshState.RESET);
                break;
            case RELEASE_TO_REFRESH:
                setState(RefreshState.REFRESHING);
                break;
            case REFRESHING:
                if (getScrollY() < -headerView.getContentHeight()) {
                    scrollTo(0, -headerView.getContentHeight());
                }
                break;
        }
    }

    private void handlePull() {
//        int scrollValue = Math.round((initialY - lastY) / FRICTION);
        int scrollValue = Math.round(Math.min(initialY - lastY, 0) / FRICTION);//一共(从起始点到结束点)下拉的距离
        if (refreshState == RefreshState.REFRESHING) {
            scrollValue -= headerView.getContentHeight();
        }
        scrollTo(0, scrollValue);//移动到相应的位置
        //根据header高度判断是否改变状态
        if (refreshState == RefreshState.REFRESHING) {//刷新状态中,不再改变状态
            return;
        }
        int headerContentHeight = headerView.getContentHeight();
        if (refreshState != RefreshState.PULL_TO_REFRESH && Math.abs(scrollValue) <= headerContentHeight) {//下拉刷新状态
            setState(RefreshState.PULL_TO_REFRESH);
        } else if (refreshState != RefreshState.RELEASE_TO_REFRESH && Math.abs(scrollValue) > headerContentHeight) {//释放刷新状态
            setState(RefreshState.RELEASE_TO_REFRESH);
        }
    }

    private void setState(RefreshState refreshState) {
        this.refreshState = refreshState;
        if (headerView == null) {
            return;
        }
        switch (refreshState) {
            case RESET:
                headerView.onResetState();
                scrollTo(0, 0);
                break;
            case PULL_TO_REFRESH:
                headerView.onPullToRefreshState();
                break;
            case RELEASE_TO_REFRESH:
                headerView.onReleaseToRefreshState();
                break;
            case REFRESHING:
                headerView.onRefreshState();
                scrollTo(0, -headerView.getContentHeight());
                break;
        }
    }

    private void initY(MotionEvent ev) {
        initialY = lastY = ev.getY();
    }

    private void resetY() {
        initialY = lastY = -1;//初始化触摸点
    }

    private boolean isInvalidY() {
        return (initialY == lastY) && (initialY == -1);
    }

    protected abstract boolean isReadyForPull();

    /**
     * 获取加载控件
     */
    protected abstract T onCreateLoadView(Context context, AttributeSet attrs);

}
