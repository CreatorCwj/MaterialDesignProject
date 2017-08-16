package com.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.materialdesign.R;

/**
 * Created by cwj on 16/8/1.
 * 刷新加载控件基类,提供emptyView
 */
public abstract class RefreshLoadBaseView<T extends RefreshBaseView> extends FrameLayout implements IRefreshLoadView {

    protected T refreshView;
    private ViewStub viewStub;
    private View emptyView;

    private int emptyViewId = -1;

    protected OnEmptyViewListener onEmptyViewListener;

    public interface OnEmptyViewListener {

        void onEmptyViewVisible(@NonNull View emptyView);

        void onEmptyViewGone(@NonNull View emptyView);
    }

    public RefreshLoadBaseView(Context context) {
        this(context, null);
    }

    public RefreshLoadBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLoadBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
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
        refreshView = createRefreshView(context, attrs);
        if (refreshView == null) {
            throw new IllegalArgumentException("refreshView can not be null");
        }
        addView(refreshView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 创建refreshView
     */
    protected abstract T createRefreshView(Context context, AttributeSet attrs);

    /**
     * 获取refreshView
     */
    public T getRefreshView() {
        return refreshView;
    }

    public void setOnEmptyViewListener(OnEmptyViewListener onEmptyViewListener) {
        this.onEmptyViewListener = onEmptyViewListener;
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
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        refreshView.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        refreshView.setOnLoadListener(onLoadListener);
    }

    @Override
    public boolean isLoading() {
        return refreshView.isLoading();
    }

    @Override
    public boolean isRefreshing() {
        return refreshView.isRefreshing();
    }

    @Override
    public int getPageNo() {
        return refreshView.getPageNo();
    }

    @Override
    public int getPageSize() {
        return refreshView.getPageSize();
    }

    @Override
    public void refresh() {
        refreshView.refresh();
    }

    @Override
    public void refreshLoadComplete() {
        refreshView.refreshLoadComplete();
    }

    @Override
    public void refreshLoadError() {
        refreshView.refreshLoadError();
    }
}
