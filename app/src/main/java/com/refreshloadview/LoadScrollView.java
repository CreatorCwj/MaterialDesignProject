package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.materialdesign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/7/27.
 * 可加载的ScrollView
 * 1.可设置是否需要加载
 * 2.可配置背景色
 */
public class LoadScrollView extends ScrollView implements ILoadView {

    private static final int HANDLE_DELAY = 50;

    private int currentY = Integer.MIN_VALUE;

    private int scrollState = OnScrollStateChangedListener.IDLE;

    private LinearLayout containerView;
    private FooterLayout footerLayout;

    private boolean isLoading;

    private boolean originCanLoadMore = false;
    private boolean canLoadMore = false;//默认不可加载
    private int loadViewBackground;//背景颜色
    private int contentViewId = -1;

    private List<OnScrollStateChangedListener> onScrollStateChangedListeners;
    private OnLoadListener onLoadListener;

    public interface OnScrollStateChangedListener {

        /**
         * 空闲
         */
        int IDLE = 0;

        /**
         * 触摸滚动
         */
        int TOUCH_SCROLL = 1;

        /**
         * 自由滑动
         */
        int FLING = 2;

        void onScrollStateChanged(int scrollState);
    }

    public LoadScrollView(Context context) {
        this(context, null);
    }

    public LoadScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, null, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            originCanLoadMore = canLoadMore = typedArray.getBoolean(R.styleable.RefreshLoadView_canLoadMore, false);
            loadViewBackground = typedArray.getColor(R.styleable.RefreshLoadView_loadViewBackgroundColor, Color.TRANSPARENT);
            contentViewId = typedArray.getResourceId(R.styleable.RefreshLoadView_contentView, -1);
            typedArray.recycle();
        }
    }

    private void initListener() {
        addOnScrollStateChangedListener(onScrollStateChangedListener);
    }

    private void initView(Context context, AttributeSet attrs) {
        //先添加LinearLayout的child
        containerView = new LinearLayout(context);
        containerView.setOrientation(LinearLayout.VERTICAL);
        addView(containerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //添加内容
        if (contentViewId != -1) {
            LayoutInflater.from(context).inflate(contentViewId, containerView, true);
        }
        //初始可加载的话,添加footer到container的最后一个
        if (originCanLoadMore) {
            footerLayout = new FooterLayout(context, attrs);
            containerView.addView(footerLayout);
            footerLayout.onResetState();
        }
        //设置属性
        containerView.setBackgroundColor(loadViewBackground);
    }

    /**
     * 添加滚动状态监听器
     */
    public void addOnScrollStateChangedListener(OnScrollStateChangedListener onScrollStateChangedListener) {
        if (onScrollStateChangedListeners == null) {
            onScrollStateChangedListeners = new ArrayList<>();
        }
        onScrollStateChangedListeners.add(onScrollStateChangedListener);
    }

    private OnScrollStateChangedListener onScrollStateChangedListener = new OnScrollStateChangedListener() {
        @Override
        public void onScrollStateChanged(int scrollState) {
            switch (scrollState) {
                case IDLE://空闲时,如果可以加载,调用加载更多
                    if (shouldLoad()) {
                        startLoad();
                    }
                    break;
            }
        }
    };

    /**
     * 设置加载监听器
     */
    @Override
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 改变footer是否显示
     */
    void hideFooter(boolean hide) {
        if (footerLayout != null && hide) {
            footerLayout.setVisibility(GONE);
        } else if (footerLayout != null) {
            footerLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * 改变containerView的显示
     */
    void hideContent(boolean hide) {
        if (hide && containerView.getVisibility() != GONE) {
            containerView.setVisibility(GONE);
        } else if (!hide && containerView.getVisibility() != VISIBLE) {
            scrollTo(0, 0);//回到顶部
            containerView.setVisibility(VISIBLE);
        }
    }

    /**
     * 无法继续加载
     */
    @Override
    public void stopLoadMore() {
        if (canLoadMore) {
            loadComplete();//结束加载
            canLoadMore = false;//置为不可加载
            footerLayout.onLoadStop();
        }
    }

    /**
     * 恢复可加载状态(初始设置可加载的话)
     */
    @Override
    public void restoreLoadMore() {
        if (originCanLoadMore && !canLoadMore) {//初始时可加载,当前不可加载(从不可加载变过来)
            canLoadMore = true;//置为可加载
            footerLayout.onResetState();
        }
    }

    /**
     * 是否处于加载状态
     */
    @Override
    public boolean isLoading() {
        return isLoading;
    }

    private boolean shouldLoad() {
        if (!canLoadMore || isLoading() || footerLayout == null) {
            return false;
        }
        //滚动到足以显示footer
        int shouldScrollY = containerView.getHeight() - getHeight() - footerLayout.getHeight();
        return shouldScrollY <= 0 || getScrollY() >= shouldScrollY;
    }

    /**
     * 加载完成
     */
    @Override
    public void loadComplete() {
        if (isLoading()) {
            isLoading = false;
            footerLayout.onResetState();
        }
    }

    @Override
    public void backToTop() {
        scrollTo(0, 0);
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

    private Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            if (getScrollY() == currentY) {
                if (scrollState != OnScrollStateChangedListener.IDLE && onScrollStateChangedListeners != null) {//改变时才调用
                    for (OnScrollStateChangedListener listener : onScrollStateChangedListeners) {
                        listener.onScrollStateChanged(OnScrollStateChangedListener.IDLE);
                    }
                }
                scrollState = OnScrollStateChangedListener.IDLE;
                removeCallbacks(this);
            } else {
                if (scrollState != OnScrollStateChangedListener.FLING && onScrollStateChangedListeners != null) {//改变时才调用
                    for (OnScrollStateChangedListener listener : onScrollStateChangedListeners) {
                        listener.onScrollStateChanged(OnScrollStateChangedListener.FLING);
                    }
                }
                scrollState = OnScrollStateChangedListener.FLING;
                currentY = getScrollY();
                postDelayed(this, HANDLE_DELAY);
            }
        }
    };

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (scrollState != OnScrollStateChangedListener.TOUCH_SCROLL && onScrollStateChangedListeners != null) {//改变时才调用
                    for (OnScrollStateChangedListener listener : onScrollStateChangedListeners) {
                        listener.onScrollStateChanged(OnScrollStateChangedListener.TOUCH_SCROLL);
                    }
                }
                this.scrollState = OnScrollStateChangedListener.TOUCH_SCROLL;
                currentY = getScrollY();
                removeCallbacks(scrollRunnable);
                break;
            case MotionEvent.ACTION_UP:
                post(scrollRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

}
