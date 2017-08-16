package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by cwj on 16/7/21.
 * ListView加载控件
 */
public class LoadListView extends LoadBaseAbsListView<LoadListView.InnerListView> {

    public LoadListView(Context context) {
        super(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void handleAttrs(@NonNull TypedArray typedArray) {
        //解析属性
    }

    @Override
    protected void onLoadViewCreated(@NonNull InnerListView loadView) {

    }

    @Override
    protected FooterLayout getFooter() {
        if (loadView != null) {
            return loadView.getFooter();
        }
        return null;
    }

    @Override
    protected InnerListView onCreateLoadView(Context context, AttributeSet attrs) {
        return new InnerListView(context, attrs);
    }

    @Override
    protected boolean shouldLoad() {
        if (loadView == null) {
            return false;
        }
        //未加载状态下,滚动到最后一条数据时可以进行刷新
        return !isLoading() && loadView.getCount() > 0 &&
                loadView.getLastVisiblePosition() >= loadView.getCount() - 1;
    }

    class InnerListView extends ListView {

        private FooterLayout footerView;

        public InnerListView(Context context) {
            this(context, null);
        }

        public InnerListView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initFooterView(context, attrs);
        }

        private void initFooterView(Context context, AttributeSet attrs) {
            footerView = new FooterLayout(context, attrs);
            addFooterView(footerView);
        }

        private FooterLayout getFooter() {
            return footerView;
        }
    }
}
