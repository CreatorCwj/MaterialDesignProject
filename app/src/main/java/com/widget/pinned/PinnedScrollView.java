//package com.widget.pinned;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.ScrollView;
//
//import com.materialdesign.R;
//
///**
// * Created by cwj on 16/11/21.
// * 可吸顶的ScrollView
// */
//public class PinnedScrollView extends ScrollView {
//
//    private static final int NO_ID = -1;
//
//    private static final boolean DEFAULT_CAN_PINNED = true;
//
//    private boolean canPinned = DEFAULT_CAN_PINNED;//是否有吸顶功能,默认开启
//    private int anchorId = NO_ID;//anchorView的id(某一子view的id)
//    private int headerId = NO_ID;//headerView的id
//
//    private View anchorView;
//    private View headerView;
//
//    private int[] anchorLocation = new int[2];
//
//    public PinnedScrollView(Context context) {
//        this(context, null);
//    }
//
//    public PinnedScrollView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public PinnedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initAttrs(context, attrs);
//        initListener();
//    }
//
//    private void initListener() {
//        //首次加载后要刷新一次状态
//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                refreshHeaderState();
//                PinnedScrollView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
//    }
//
//    private void initAttrs(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinnedScrollView);
//            canPinned = typedArray.getBoolean(R.styleable.PinnedScrollView_canPinned, DEFAULT_CAN_PINNED);
//            anchorId = typedArray.getResourceId(R.styleable.PinnedScrollView_anchorId, NO_ID);
//            headerId = typedArray.getResourceId(R.styleable.PinnedScrollView_headerView, NO_ID);
//            typedArray.recycle();
//        }
//    }
//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {//滚动后会调用
//        super.onScrollChanged(l, t, oldl, oldt);
//        refreshHeaderState();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (!canPinned || headerView == null) {
//            return;
//        }
//        int width = Math.max(0, getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
//        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);//占满SV子view可用宽度全部空间
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
//        canvas.translate(headerView.getLeft(), getScrollY() + headerView.getTop());//SV使用mScrollY滚动
//        canvas.clipRect(getScrollX(), 0, getScrollX() + headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
//        headerView.draw(canvas);
//        canvas.restoreToCount(saveCount);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //1.判断header是否可以获取事件(canPinned、VISIBLE、inRect)
//        if (!canPinned || headerView == null || headerView.getVisibility() != VISIBLE || !isInRect(ev)) {
//            return super.dispatchTouchEvent(ev);
//        }
//        //2.在的话派发给headerView(MotionEvent要偏移)
//        int offsetX = getScrollX() - headerView.getLeft();
//        int offsetY = -headerView.getTop();
//        ev.offsetLocation(offsetX, offsetY);
//        boolean handleByHeader = headerView.dispatchTouchEvent(ev);
//        ev.offsetLocation(-offsetX, -offsetY);
//        //3.headerView处理了返回true
//        //4.headerView未处理返回super
//        return handleByHeader || super.dispatchTouchEvent(ev);
//    }
//
//    private boolean isInRect(MotionEvent ev) {
//        float relativeX = ev.getX() + getScrollX() - headerView.getLeft();
//        float relativeY = ev.getY() - headerView.getTop();
//        return relativeX >= 0 && relativeX < headerView.getWidth()
//                && relativeY >= 0 && relativeY < headerView.getHeight();
//    }
//
//    /**
//     * 获取HeaderView
//     */
//    public View getHeaderView() {
//        return headerView;
//    }
//
//    /**
//     * 获取AnchorView
//     */
//    public View getAnchorView() {
//        return anchorView;
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
//     * 设置是否可以吸顶
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
//     * 通过设置headerId来设置headerView
//     */
//    public void setHeaderId(int headerId) {
//        if (this.headerId == headerId) {
//            return;
//        }
//        this.headerId = headerId;
//        inflateHeaderView();
//    }
//
//    /**
//     * 设置headerView
//     */
//    public void setHeaderView(View headerView) {
//        if (this.headerView == headerView) {
//            return;
//        }
//        this.headerView = headerView;
//        resetHeaderView();
//    }
//
//    /**
//     * 通过设置anchorId来设置anchorView
//     */
//    public void setAnchorId(int anchorId) {
//        if (this.anchorId == anchorId) {
//            return;
//        }
//        this.anchorId = anchorId;
//        findAnchorView();
//    }
//
//    /**
//     * 设置anchorView(必须是某一个子view)
//     */
//    public void setAnchorView(View anchorView) {
//        if (this.anchorView == anchorView) {
//            return;
//        }
//        this.anchorView = anchorView;
//        resetHeaderView();
//    }
//
//    private void inflateHeaderView() {
//        if (headerId != NO_ID) {
//            headerView = LayoutInflater.from(getContext()).inflate(headerId, this, false);
//        } else {
//            headerView = null;
//        }
//        resetHeaderView();
//    }
//
//    private void findAnchorView() {
//        if (anchorId != NO_ID) {
//            anchorView = findViewById(anchorId);
//        } else {
//            anchorView = null;
//        }
//        resetHeaderView();
//    }
//
//    private void resetHeaderView() {
//        //1.重置为GONE
//        if (headerView != null) {
//            headerView.setVisibility(GONE);
//        }
//        //2.刷新当前显示状态
//        refreshHeaderState();
//        //3.需要重新测量、布局、绘制header
//        requestLayout();
//        invalidate();
//    }
//
//    private void refreshHeaderState() {
//        if (!canPinned || anchorView == null || headerView == null) {
//            return;
//        }
//        //计算各自的top
//        getLocationInWindow(anchorLocation);
//        int baseline = anchorLocation[1] + getPaddingTop();
//        anchorView.getLocationInWindow(anchorLocation);
//        int anchorTop = anchorLocation[1];
//        //根据当前top位置决定header是否显示
//        if (anchorTop <= baseline) {
//            headerView.setVisibility(VISIBLE);
//        } else {
//            headerView.setVisibility(GONE);
//        }
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        //xml加载完后才可以获取到相应的view
//        findAnchorView();
//        inflateHeaderView();
//    }
//}
