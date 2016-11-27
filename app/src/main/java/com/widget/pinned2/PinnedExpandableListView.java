package com.widget.pinned2;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

/**
 * Created by cwj on 16/11/24.
 * 吸顶的ExpandableListView
 */
public class PinnedExpandableListView extends ExpandableListView implements IPinnedGroupList {

    private static final int INVALID_GROUP_TYPE = -1;
    private static final int INVALID_GROUP_POSITION = -1;

    private View headerView;

    private int currentGroupPosition = INVALID_GROUP_POSITION;//当前headerView所处groupPosition
    private int currentGroupType = INVALID_GROUP_TYPE;

    private boolean isNotify;

    private OnPinnedScrollListener onPinnedScrollListener;

    public PinnedExpandableListView(Context context) {
        this(context, null);
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isNotify && onPinnedScrollListener != null) {//数据更新引起的,需要在layout结束后再次获取新的header
                    updateHeaderView(true);
                    onPinnedScrollListener.onPinnedScroll();
                }
                isNotify = false;
            }
        });
    }

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            isNotify = true;//数据更新了
            expand();
//            buildHeaderViewByAdapter();
        }
    };

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        ExpandableListAdapter oldAdapter = getExpandableListAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
        }
        expand();
        observer.onChanged();
    }

    private void expand() {
        //test
        int groupCount = getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandGroup(i);
        }
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
                return anchorTop < baseline;
            }
            return false;
        } else { //anchor之后项都会显示header
            return firstPosition > anchorPos;
        }
    }

    @Override
    public boolean shouldPinnedByGroup() {
        return !(getHeaderViewsCount() == 0 && currentGroupPosition == 0 && getFirstVisiblePosition() == 0 && firstGroupToTop());
    }

    private boolean firstGroupToTop() {
        View firstGroupChild = getChildCount() > 0 ? getChildAt(0) : null;
        return firstGroupChild != null && firstGroupChild.getTop() >= 0;
    }

    @Override
    public View getHeaderViewByAnchor(int anchorPos) {
        //do nothing
        return null;
    }

    @Override
    public View getHeaderViewByGroup() {
        updateHeaderView(false);
        return headerView;
    }

    @Override
    public int getGroupPosition() {
        return currentGroupPosition;
    }

    //更新headerView
    private void updateHeaderView(boolean forceBuildHeader) {
        //获取最新groupPosition
        int newGroupPosition = getNewGroupPosition();
        //正常处理
        if (((newGroupPosition != currentGroupPosition) && !isNotify) || forceBuildHeader) {//改变位置或者数据刷新都要重新获取header
            currentGroupPosition = newGroupPosition;
            //获取新的headerView
            buildHeaderViewByAdapter();
        }
        //计算offset,设置margin
        int offset = getOffset();
        if (headerView != null) {
            ViewGroup.LayoutParams params = headerView.getLayoutParams();
            if (!(params instanceof FrameLayout.LayoutParams)) {//转换成FrameLayout的LayoutParams,并设置topMargin
                FrameLayout.LayoutParams frameParams = params != null ? new FrameLayout.LayoutParams(params) : new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                frameParams.topMargin = -offset;
                headerView.setLayoutParams(frameParams);
            } else {//设置topMargin
                ((FrameLayout.LayoutParams) params).topMargin = -offset;
                headerView.setLayoutParams(params);
            }
        }
    }

    //获取最新的groupPosition
    private int getNewGroupPosition() {
        ExpandableListAdapter adapter = getExpandableListAdapter();
        if (adapter == null || adapter.getGroupCount() <= 0) {//无数据
            return INVALID_GROUP_POSITION;
        }
        long packedPosition = getExpandableListPosition(getFirstVisiblePosition());
        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);//groupPosition
        if (groupPosition < 0) {//没在某个group(header or footer)
            return INVALID_GROUP_POSITION;
        }
        return groupPosition;
    }

    //根据currentGroupPosition创建headerView
    private void buildHeaderViewByAdapter() {
        if (currentGroupPosition == INVALID_GROUP_POSITION) {//无headerView
            resetHeaderViewType();
            headerView = null;
            return;
        }
        ExpandableListAdapter adapter = getExpandableListAdapter();
        if (!(adapter instanceof BaseExpandableListAdapter) || currentGroupPosition < 0 || currentGroupPosition >= adapter.getGroupCount()) {
            resetHeaderViewType();
            headerView = null;
            return;
        }
        //根据groupType复用
        int newGroupType = ((BaseExpandableListAdapter) adapter).getGroupType(currentGroupPosition);
        if (currentGroupType == INVALID_GROUP_TYPE || currentGroupType != newGroupType) {//new header
            headerView = adapter.getGroupView(currentGroupPosition, isGroupExpanded(currentGroupPosition), null, this);
            currentGroupType = newGroupType;
        } else {//reuse
            headerView = adapter.getGroupView(currentGroupPosition, isGroupExpanded(currentGroupPosition), headerView, this);
        }
    }

    private void resetHeaderViewType() {
        currentGroupType = INVALID_GROUP_TYPE;
    }

    //计算headerView的offset
    private int getOffset() {
        if (headerView == null) {
            return 0;
        }
        int firstPosition = getFirstVisiblePosition();
        int lastPosition = getLastVisiblePosition();
        for (int i = firstPosition + 1; i <= lastPosition; i++) {
            //找到header下面的第一个group
            if (ExpandableListView.getPackedPositionType(getExpandableListPosition(i)) == PACKED_POSITION_TYPE_GROUP) {
                int childIndex = i - firstPosition;
                View nextGroupView = getChildCount() > childIndex ? getChildAt(i - firstPosition) : null;
                //根据其top判断header的offset
                if (nextGroupView != null) {
                    int top = nextGroupView.getTop();
                    if (headerView.getMeasuredHeight() <= 0) {
                        measureHeader();
                    }
                    int headerBottom = headerView.getMeasuredHeight();
                    return top < headerBottom ? headerBottom - top : 0;
                }
            }
        }
        return 0;
    }

    private void measureHeader() {
        if (headerView == null) {
            return;
        }
        ViewGroup.LayoutParams params = headerView.getLayoutParams();
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec;
        if (params != null && params.height > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        } else {
            heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        headerView.measure(widthSpec, heightSpec);
        headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
    }

    @Override
    public void setIPinnedScrollListener(@NonNull OnPinnedScrollListener onPinnedScrollListener) {
        this.onPinnedScrollListener = onPinnedScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onPinnedScrollListener != null) {
            onPinnedScrollListener.onPinnedScroll();
        }
    }
}
