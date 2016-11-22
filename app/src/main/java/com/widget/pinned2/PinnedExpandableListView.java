package com.widget.pinned2;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

/**
 * Created by cwj on 16/11/24.
 * 吸顶的ExpandableListView
 */
public class PinnedExpandableListView extends ExpandableListView implements IPinnedGroupList {

    private static final int INVALID_GROUP_POSITION = -1;

    private View headerView;

    private int currentGroupPosition = INVALID_GROUP_POSITION;//当前headerView所处groupPosition

    public PinnedExpandableListView(Context context) {
        this(context, null);
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        updateHeaderView();
    }

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            updateHeaderView();
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
        updateHeaderView();
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
        //do nothing
        return null;
    }

    @Override
    public View getHeaderViewByGroup(int[] offset) {
        return headerView;
    }

    @Override
    public int getGroupPosition() {
        return currentGroupPosition;
    }

    //更新headerView
    private void updateHeaderView() {
        //获取最新groupPosition
        int newGroupPosition = getNewGroupPosition();
        //正常处理
        if (newGroupPosition != currentGroupPosition) {
            currentGroupPosition = newGroupPosition;
            //获取新的headerView
            buildHeaderViewByAdapter();
        }
        //计算offset
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
        //注:这里需要处理一种情况,在没有ExpandableListView没有header的情况下,第一个group置顶时,headerView会显示出来,要取消掉这种情况
        if (headerView != null && getHeaderViewsCount() == 0 && newGroupPosition == 0 && getFirstVisiblePosition() == 0 && firstGroupToTop()) {
            if (headerView.getParent() == null) {//还没有加入View树,需要设置监听,计算高度设置初始margin
                headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setInitialMargin();
                    }
                });
            } else {//直接设置margin
                setInitialMargin();
            }
        }
    }

    private void setInitialMargin() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) headerView.getLayoutParams();
        params.topMargin = -headerView.getMeasuredHeight();
        headerView.setLayoutParams(params);
    }

    private boolean firstGroupToTop() {
        View firstGroupChild = getChildCount() > 0 ? getChildAt(0) : null;
        return firstGroupChild != null && firstGroupChild.getTop() >= 0;
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
            headerView = null;
            return;
        }
        ExpandableListAdapter adapter = getExpandableListAdapter();
        if (adapter == null || currentGroupPosition < 0 || currentGroupPosition >= adapter.getGroupCount()) {
            headerView = null;
            return;
        }
        //ExpandableListView没有viewType,所以GroupView都是一种,直接复用即可
        headerView = adapter.getGroupView(currentGroupPosition, isGroupExpanded(currentGroupPosition), headerView, this);
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
                View nextGroupView = getChildAt(i - firstPosition);
                //根据其top判断header的offset
                if (nextGroupView != null) {
                    int top = nextGroupView.getTop();
                    int headerBottom = headerView.getMeasuredHeight();
                    return top < headerBottom ? headerBottom - top : 0;
                }
            }
        }
        return 0;
    }

}
