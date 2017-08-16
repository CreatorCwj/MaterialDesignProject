//package com.widget.pinned;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.database.DataSetObserver;
//import android.graphics.Canvas;
//import android.support.annotation.IntDef;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.HeaderViewListAdapter;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//
//import com.materialdesign.R;
//
///**
// * Created by cwj on 16/11/22.
// * 可吸顶的ListView
// */
//public class PinnedListView extends ListView {
//
//    @IntDef({HEADER_MODE_CUSTOM, HEADER_MODE_ADAPTER})
//    public @interface HeaderMode {
//    }
//
//    public static final int HEADER_MODE_CUSTOM = 0;//自定义view
//    public static final int HEADER_MODE_ADAPTER = 1;//通过adapter获取指定pos的itemView
//
//    private static final int INVALID_HEADER_VIEW_TYPE = -1;
//    private static final int NO_ID = -1;
//
//    private static final boolean DEFAULT_CAN_PINNED = true;
//    @HeaderMode
//    private static final int DEFAULT_HEADER_MODE = HEADER_MODE_CUSTOM;
//
//    private boolean canPinned = DEFAULT_CAN_PINNED;//是否有吸顶功能,默认开启
//    private int anchorPos = -1;//anchorView的item position
//    private int headerId = NO_ID;//headerView的id
//    @HeaderMode
//    private int headerMode = DEFAULT_HEADER_MODE;//获取header的模式,默认自定义
//
//    private View headerView;
//    private int currentHeaderViewType = INVALID_HEADER_VIEW_TYPE;//当前headerView的type(ADAPTER模式用到的，用于按类别复用headerView)
//
//    public PinnedListView(Context context, @HeaderMode int headerMode) {
//        this(context, null, 0, headerMode);
//    }
//
//    public PinnedListView(Context context) {
//        this(context, null);
//    }
//
//    public PinnedListView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public PinnedListView(Context context, AttributeSet attrs, int defStyleAttr) {
//        this(context, attrs, defStyleAttr, DEFAULT_HEADER_MODE);
//    }
//
//    private PinnedListView(Context context, AttributeSet attrs, int defStyleAttr, @HeaderMode int headerMode) {
//        super(context, attrs, defStyleAttr);
//        this.headerMode = headerMode;
//        initAttrs(context, attrs);
//        initHeaderView();
//    }
//
//    private void initAttrs(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinnedListView);
//            canPinned = typedArray.getBoolean(R.styleable.PinnedListView_canPinned, DEFAULT_CAN_PINNED);
//            anchorPos = typedArray.getInt(R.styleable.PinnedListView_anchorPos, -1);
//            headerId = typedArray.getResourceId(R.styleable.PinnedListView_headerView, NO_ID);
//            //noinspection WrongConstant
//            headerMode = typedArray.getInt(R.styleable.PinnedListView_headerMode, DEFAULT_HEADER_MODE);
//            typedArray.recycle();
//        }
//    }
//
//    private DataSetObserver observer = new DataSetObserver() {
//        @Override
//        public void onChanged() {
//            if (headerMode == HEADER_MODE_ADAPTER) {//重新获取Header
//                inflateHeaderViewByAdapter();
//            }
//        }
//    };
//
//    @Override
//    public void setAdapter(ListAdapter adapter) {
//        ListAdapter oldAdapter = getAdapter();
//        if (oldAdapter != null) {
//            oldAdapter.unregisterDataSetObserver(observer);
//        }
//        super.setAdapter(adapter);
//        if (adapter != null) {
//            adapter.registerDataSetObserver(observer);
//        }
//        if (headerMode == HEADER_MODE_ADAPTER) {//重新获取Header
//            resetHeaderViewType();
//            inflateHeaderViewByAdapter();
//        }
//    }
//
//    /**
//     * 获取anchorPos
//     */
//    public int getAnchorPos() {
//        return anchorPos;
//    }
//
//    /**
//     * 获取headerView
//     */
//    public View getHeaderView() {
//        return headerView;
//    }
//
//    /**
//     * 是否开启吸顶功能
//     */
//    public boolean isCanPinned() {
//        return canPinned;
//    }
//
//    /**
//     * 是否开启吸顶功能
//     */
//    public void setCanPinned(boolean canPinned) {
//        if (this.canPinned == canPinned) {
//            return;
//        }
//        this.canPinned = canPinned;
//        resetHeaderView();
//    }
//
//    /**
//     * 设置headerId,mode必须是HEADER_MODE_CUSTOM
//     */
//    public void setHeaderId(int headerId) {
//        if (headerMode != HEADER_MODE_CUSTOM || this.headerId == headerId) {//无需做更新
//            return;
//        }
//        this.headerId = headerId;
//        inflateHeaderViewById();
//    }
//
//    /**
//     * 设置headerView,mode必须是HEADER_MODE_CUSTOM
//     */
//    public void setHeaderView(View headerView) {
//        if (headerMode != HEADER_MODE_CUSTOM || this.headerView == headerView) {//无需做更新
//            return;
//        }
//        this.headerView = headerView;
//        resetHeaderView();
//    }
//
//    /**
//     * anchorPos
//     * 1.mode是ADAPTER时，这个pos应该指的是相对于dataList中的pos(因为通常是某一dataItem作为anchor)
//     * 2.mode是CUSTOM时，这个pos应该指的是相对于整个ListView的item的pos(因为通常是某一header作为anchor)
//     */
//    public void setAnchorPos(int anchorPos) {
//        if (this.anchorPos == anchorPos) {
//            return;
//        }
//        this.anchorPos = anchorPos;
//        //如果是mode的话需要更新headerView,因为headerView是根据anchorPos拿的
//        if (headerMode == HEADER_MODE_ADAPTER) {
//            inflateHeaderViewByAdapter();
//        } else {
//            resetHeaderView();
//        }
//    }
//
//    private void inflateHeaderViewById() {
//        if (headerId != NO_ID) {
//            headerView = LayoutInflater.from(getContext()).inflate(headerId, this, false);
//        } else {
//            headerView = null;
//        }
//        resetHeaderView();
//    }
//
//    //需要考虑按viewType进行header复用
//    private void inflateHeaderViewByAdapter() {
//        //此时anchorPos为相对与数据集合的pos(不可以直接拿Header的View作为吸顶的headerView)
//        ListAdapter adapter = getAdapter();
//        if (adapter != null && anchorPos >= 0 &&
//                (adapter.getCount() - getHeaderViewsCount() - getFooterViewsCount()) > anchorPos) {
//            ListAdapter listAdapter = (adapter instanceof HeaderViewListAdapter) ? ((HeaderViewListAdapter) adapter).getWrappedAdapter() : adapter;
//            BaseAdapter baseAdapter = (listAdapter instanceof BaseAdapter) ? (BaseAdapter) listAdapter : null;
//            if (baseAdapter == null) {
//                headerView = null;
//                resetHeaderViewType();
//            } else {
//                //根据type复用
//                int headerViewType = baseAdapter.getItemViewType(anchorPos);
//                if (currentHeaderViewType == INVALID_HEADER_VIEW_TYPE || headerViewType != currentHeaderViewType) {
//                    headerView = baseAdapter.getView(anchorPos, null, this);
//                    currentHeaderViewType = headerViewType;
//                } else {//可以复用
//                    headerView = baseAdapter.getView(anchorPos, headerView, this);
//                }
//            }
//        } else {//当前无anchorPos这个itemView,header为null
//            headerView = null;
//            resetHeaderViewType();
//        }
//        resetHeaderView();
//    }
//
//    private void resetHeaderViewType() {
//        currentHeaderViewType = INVALID_HEADER_VIEW_TYPE;
//    }
//
//    private void initHeaderView() {
//        //by mode
//        switch (headerMode) {
//            case HEADER_MODE_CUSTOM://通过headerId加载
//                inflateHeaderViewById();
//                break;
//            case HEADER_MODE_ADAPTER://通过adapter获取
//                inflateHeaderViewByAdapter();
//                break;
//        }
//    }
//
//    private void resetHeaderView() {
//        //1.重置为GONE
//        if (headerView != null) {
//            headerView.setVisibility(GONE);
//        }
//        //2.刷新状态(不用刷新，在一次布局后会调用到scroll的listener，在那里会进行刷新状态，这里刷新是不准的，因为新的数据可能导致views发生改变)
////        refreshHeaderState();
//        //3.重新测量、布局、绘制header
//        requestLayout();
//        invalidate();
//    }
//
//    private void refreshHeaderState() {
//        if (!canPinned || headerView == null || anchorPos < 0) {
//            return;
//        }
//        int firstPosition = getFirstVisiblePosition();
//        if (headerMode == HEADER_MODE_ADAPTER) {
//            firstPosition -= getHeaderViewsCount();
//        }
//        //这里不能直接用firstPos与anchorPos比较，因为divider会被算作firstItem的一部分，即可能anchor并未置顶时已经到达anchorPos(因为上一个item下方的divider)
//        if (firstPosition == anchorPos) {//当前第一项为anchor
//            View anchorView = getChildCount() > 0 ? getChildAt(0) : null;
//            if (anchorView != null) {
//                int anchorTop = anchorView.getTop();
//                int baseline = getPaddingTop();
//                if (anchorTop <= baseline) {//anchor到达顶部,header可见
//                    headerView.setVisibility(VISIBLE);
//                    return;
//                }
//            }
//            headerView.setVisibility(GONE);
//        } else if (firstPosition > anchorPos) {//anchor之后项都会显示header
//            headerView.setVisibility(VISIBLE);
//        } else {
//            headerView.setVisibility(GONE);
//        }
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (!canPinned || headerView == null) {
//            return;
//        }
//        int width = Math.max(0, getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
//        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);//占满LV子view可用宽度全部空间
//        int heightSpec;
//        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
//        if (layoutParams != null && layoutParams.height > 0) {
//            heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
//        } else {
//            heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        }
//        headerView.measure(widthSpec, heightSpec);
//        headerView.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + headerView.getMeasuredWidth(), getPaddingTop() + headerView.getMeasuredHeight());
//    }
//
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        if (!canPinned || headerView == null || headerView.getVisibility() != VISIBLE) {
//            return;
//        }
//        int saveCount = canvas.save();
//        canvas.translate(headerView.getLeft(), headerView.getTop());
//        canvas.clipRect(getScrollX(), getScrollY(), getScrollX() + headerView.getMeasuredWidth(), getScrollY() + headerView.getMeasuredHeight());
//        headerView.draw(canvas);
//        canvas.restoreToCount(saveCount);
//    }
//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//        refreshHeaderState();//该方法每次加载完views都会调用,改变header的状态,在紧接着的draw方法时会绘制
//    }
//}
