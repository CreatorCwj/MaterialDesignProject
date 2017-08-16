package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.materialdesign.R;

/**
 * Created by cwj on 16/7/21.
 * 加载控件基类
 */
public abstract class LoadBaseView<T extends View> extends FrameLayout {

    private enum LoadState {
        LOADING,//加载中
        IDLE//未加载
    }

    /**
     * 加载监听
     */
    public interface OnLoadListener {
        void onLoad();
    }

    protected T loadView;
    private FooterLayout footerView;

    private LoadState loadState = LoadState.IDLE;
    private OnLoadListener onLoadListener;

    public LoadBaseView(Context context) {
        this(context, null);
    }

    public LoadBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //初始化属性,交由子类处理,此类回收
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshListView);
            handleAttrs(typedArray);
            typedArray.recycle();
        }
    }

    /**
     * 处理属性
     */
    protected abstract void handleAttrs(@NonNull TypedArray typedArray);

    private void initView(Context context, AttributeSet attrs) {
        //创建view
        loadView = onCreateLoadView(context, attrs);
        if (loadView == null) {
            return;
        }
        if (loadView.getParent() == null) {//未添加view则添加view
            addView(loadView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        onLoadViewCreated(loadView);
        //获取footer
        footerView = getFooter();
    }

    /**
     * loadView创建完成
     */
    protected abstract void onLoadViewCreated(@NonNull T loadView);

    protected void startLoad() {
        setState(LoadState.LOADING);
        if (onLoadListener != null) {//回调监听
            onLoadListener.onLoad();
        }
    }

    /**
     * 加载结束方法
     */
    public void loadComplete() {
        setState(LoadState.IDLE);
    }

    /**
     * 是否正在加载
     */
    public boolean isLoading() {
        return loadState == LoadState.LOADING;
    }

    private void setState(LoadState loadState) {
        this.loadState = loadState;//更新状态
        if (footerView == null) {
            return;
        }
        //更新footer
        switch (loadState) {
            case IDLE:
                footerView.onResetState();
                break;
            case LOADING:
                footerView.onLoadingState();
                break;
        }
    }

    /**
     * 设置加载监听
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 拿到footer
     */
    protected abstract FooterLayout getFooter();

    /**
     * 创建加载的view
     */
    protected abstract T onCreateLoadView(Context context, AttributeSet attrs);

    /**
     * 获取loadView
     */
    public T getLoadView() {
        return loadView;
    }

}
