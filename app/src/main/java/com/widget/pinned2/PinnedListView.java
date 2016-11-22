package com.widget.pinned2;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by cwj on 16/11/24.
 * 吸顶的ListView
 */
public class PinnedListView extends ListView implements IPinnedList {

    private static final int INVALID_HEADER_VIEW_TYPE = -1;
    private static final int INITIAL_ANCHOR_POS = -1;

    private int currentAnchorPos = INITIAL_ANCHOR_POS;

    private View headerView;
    private int currentHeaderViewType = INVALID_HEADER_VIEW_TYPE;//当前headerView的type(ITEM模式用到的，用于按类别复用headerView)

    public PinnedListView(Context context) {
        super(context);
    }

    public PinnedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            buildHeaderViewByAdapter();//重新获取Header
        }
    };

    @Override
    public void setAdapter(ListAdapter adapter) {
        ListAdapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
        }
        resetHeaderViewType();//换了adapter,要重置viewType(新的adapter可能有一样的viewType)
        buildHeaderViewByAdapter();//重新获取Header
    }

    @Override
    public boolean shouldPinned(int anchorPos, boolean relativeDataPos) {
        int firstPosition = getFirstVisiblePosition();
        if (relativeDataPos) {//是否是相对于data集合的pos(不包括ListView的header)
            firstPosition -= getHeaderViewsCount();
        }
        //这里不能直接用firstPos与anchorPos比较，因为divider会被算作firstItem的一部分，即可能anchor并未置顶时已经到达anchorPos(因为上一个item下方的divider)
        if (firstPosition == anchorPos) {//当前第一项为anchor
            View anchorView = getChildCount() > 0 ? getChildAt(0) : null;
            if (anchorView != null) {
                int anchorTop = anchorView.getTop();
                int baseline = getPaddingTop();
                return anchorTop <= baseline;
            }
            return false;
        } else { //anchor之后项都会显示header
            return firstPosition > anchorPos;
        }
    }

    @Override
    public View getHeaderViewByAnchor(int anchorPos) {
        //首次获取或者改变了anchorPos,需要重新获取headerView
        if (currentAnchorPos == INITIAL_ANCHOR_POS || currentAnchorPos != anchorPos) {
            currentAnchorPos = anchorPos;
            buildHeaderViewByAdapter();
        }
        return headerView;
    }

    private void buildHeaderViewByAdapter() {
        //此时anchorPos默认为相对与数据集合的pos(不可以直接拿Header的View作为吸顶的headerView)
        ListAdapter adapter = getAdapter();
        if (adapter != null && currentAnchorPos != INITIAL_ANCHOR_POS && currentAnchorPos >= 0 &&
                (adapter.getCount() - getHeaderViewsCount() - getFooterViewsCount()) > currentAnchorPos) {
            ListAdapter listAdapter = (adapter instanceof HeaderViewListAdapter) ? ((HeaderViewListAdapter) adapter).getWrappedAdapter() : adapter;
            BaseAdapter baseAdapter = (listAdapter instanceof BaseAdapter) ? (BaseAdapter) listAdapter : null;
            if (baseAdapter == null) {
                headerView = null;
                resetHeaderViewType();
            } else {
                //根据type复用
                int headerViewType = baseAdapter.getItemViewType(currentAnchorPos);
                if (currentHeaderViewType == INVALID_HEADER_VIEW_TYPE || headerViewType != currentHeaderViewType) {
                    headerView = baseAdapter.getView(currentAnchorPos, null, this);
                    currentHeaderViewType = headerViewType;
                } else {//可以复用
                    headerView = baseAdapter.getView(currentAnchorPos, headerView, this);
                }
            }
        } else {//当前无anchorPos这个itemView,header为null
            headerView = null;
            resetHeaderViewType();
        }
    }

    private void resetHeaderViewType() {
        currentHeaderViewType = INVALID_HEADER_VIEW_TYPE;
    }
}
