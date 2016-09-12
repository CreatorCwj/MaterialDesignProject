package com.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/30.
 * 支持Header和Footer的RecyclerView
 */
public class HeaderFooterRecyclerView extends RecyclerView {

    private List<View> headerViews;
    private List<View> footerViews;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public HeaderFooterRecyclerView(Context context) {
        this(context, null);
    }

    public HeaderFooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderFooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initClickListener();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private void initClickListener() {
        addOnItemTouchListener(new ItemClickListener());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {//置空adapter
            super.setAdapter(null);
        } else {//一律使用HeaderFooterAdapter包装,类型统一
            super.setAdapter(new HeaderFooterWrapperAdapter(headerViews, footerViews, adapter));
        }
    }

    public void addHeaderView(View headerView) {
        if (headerViews == null) {
            headerViews = new ArrayList<>();
        }
        headerViews.add(headerView);
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderFooterWrapperAdapter) {
            ((HeaderFooterWrapperAdapter) adapter).addHeaderView(headerView);
        }
    }

    public void addFooterView(View footerView) {
        if (footerViews == null) {
            footerViews = new ArrayList<>();
        }
        footerViews.add(footerView);
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderFooterWrapperAdapter) {
            ((HeaderFooterWrapperAdapter) adapter).addFooterView(footerView);
        }
    }

    public void removeHeaderView(View headerView) {
        if (headerViews != null && !headerViews.isEmpty()) {
            headerViews.remove(headerView);
            Adapter adapter = getAdapter();
            if (adapter instanceof HeaderFooterWrapperAdapter) {
                ((HeaderFooterWrapperAdapter) adapter).removeHeaderView(headerView);
            }
        }
    }

    public void removeFooterView(View footerView) {
        if (footerViews != null && !footerViews.isEmpty()) {
            footerViews.remove(footerView);
            Adapter adapter = getAdapter();
            if (adapter instanceof HeaderFooterWrapperAdapter) {
                ((HeaderFooterWrapperAdapter) adapter).removeFooterView(footerView);
            }
        }
    }

    public int getHeaderCounts() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderFooterWrapperAdapter) {
            return ((HeaderFooterWrapperAdapter) adapter).getHeaderCounts();
        }
        return 0;
    }

    public int getFooterCounts() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderFooterWrapperAdapter) {
            return ((HeaderFooterWrapperAdapter) adapter).getFooterCounts();
        }
        return 0;
    }

    public int getCount() {
        Adapter adapter = getAdapter();
        return adapter == null ? 0 : adapter.getItemCount();
    }

    private class ItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

        private final GestureDetectorCompat gestureDetector;

        public ItemClickListener() {
            gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View view = findChildViewUnder(e.getX(), e.getY());
                    if (view != null && onItemClickListener != null) {
                        int pos = getValidPosition(getChildLayoutPosition(view));
                        if (pos >= 0) {
                            onItemClickListener.onItemClick(view, pos);
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View view = findChildViewUnder(e.getX(), e.getY());
                    if (view != null && onItemLongClickListener != null) {
                        int pos = getValidPosition(getChildLayoutPosition(view));
                        if (pos >= 0) {
                            onItemLongClickListener.onItemLongClick(view, pos);
                        }
                    }
                }

                private int getValidPosition(int pos) {
                    if (pos < 0) {
                        return -1;
                    }
                    Adapter adapter = getAdapter();
                    if (adapter instanceof HeaderFooterWrapperAdapter) {
                        HeaderFooterWrapperAdapter wrapperAdapter = (HeaderFooterWrapperAdapter) adapter;
                        if (wrapperAdapter.isHeaderView(pos) || wrapperAdapter.isFooterView(pos)) {
                            return -1;
                        }
                        return pos - getHeaderCounts();
                    }
                    return -1;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetector.onTouchEvent(e);
            return false;
        }
    }

}
