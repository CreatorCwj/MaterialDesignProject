package com.widget.pinned2;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.materialdesign.R;

/**
 * Created by cwj on 16/11/23.
 * 列表组件的吸顶容器基类
 */
public abstract class BaseListPinnedLayout<T extends IPinnedList> extends BasePinnedLayout<T> {

    public interface OnListPinnedViewClickListener {
        void onListPinnedViewClick(@NonNull View headerView, int position);
    }

    @IntDef({HEADER_MODE_CUSTOM, HEADER_MODE_ITEM})
    public @interface HeaderMode {
    }

    /**
     * 自定义HeaderView
     */
    public static final int HEADER_MODE_CUSTOM = 0;

    /**
     * 该模式下anchorPos为列表控件item的pos,headerView直接取用相应item的view(各个子类可能不一样)
     * 这里pos只数据集合中的pos(不包括列表控件的header,因为其header的view不能直接获取使用)
     */
    public static final int HEADER_MODE_ITEM = 1;

    @HeaderMode
    private static final int DEFAULT_HEADER_MODE = HEADER_MODE_CUSTOM;

    @HeaderMode
    protected int headerMode = DEFAULT_HEADER_MODE;//获取header的模式,默认自定义

    protected int anchorPos = -1;//anchorView的item position

    private OnListPinnedViewClickListener onListPinnedViewClickListener;

    public BaseListPinnedLayout(Context context) {
        this(context, null);
    }

    public BaseListPinnedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseListPinnedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseListPinnedLayout);
            anchorPos = typedArray.getInt(R.styleable.BaseListPinnedLayout_anchorPos, -1);
            //noinspection WrongConstant
            headerMode = typedArray.getInt(R.styleable.BaseListPinnedLayout_headerMode, DEFAULT_HEADER_MODE);
            typedArray.recycle();
        }
    }

    /**
     * 设置headerView的点击监听,mode为ITEM时使用
     * CUSTOM的mode时使用{@link #setOnPinnedViewClickListener(OnClickListener)}
     */
    public void setOnListPinnedViewClickListener(OnListPinnedViewClickListener onListPinnedViewClickListener) {
        this.onListPinnedViewClickListener = onListPinnedViewClickListener;
    }

    /**
     * 获取headerMode
     */
    @HeaderMode
    public int getHeaderMode() {
        return headerMode;
    }

    /**
     * 获取anchorPos
     */
    public int getAnchorPos() {
        return anchorPos;
    }

    /**
     * 设置anchorPos
     */
    public void setAnchorPos(int anchorPos) {
        if (this.anchorPos == anchorPos) {
            return;
        }
        this.anchorPos = anchorPos;
        if (headerMode == HEADER_MODE_CUSTOM) {
            refreshHeaderState();
        } else if (headerMode == HEADER_MODE_ITEM) {
            inflateHeaderViewByList();
        }
    }

    /**
     * 从列表组件中找到header
     */
    protected abstract View getHeaderViewByList(@NonNull T pinnedView);

    /**
     * 获取headerView的position
     */
    protected abstract int getHeaderViewPosition(@NonNull T pinnedView);

    private void inflateHeaderViewByList() {
        View newHeaderView = null;
        if (pinnedView != null) {
            newHeaderView = getHeaderViewByList(pinnedView);
        }
        if (newHeaderView != null) {//根据列表组件产生的headerView要实时进行点击监听的更新
            newHeaderView.setOnClickListener(onClickListener);
        }
        resetHeaderView(newHeaderView);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onListPinnedViewClickListener != null) {
                onListPinnedViewClickListener.onListPinnedViewClick(v, getHeaderViewPosition(pinnedView));
            }
        }
    };

    @Override
    public void setHeaderId(int headerId) {
        if (headerMode == HEADER_MODE_CUSTOM) {
            super.setHeaderId(headerId);
        }
    }

    @Override
    public void setHeaderView(View headerView) {
        if (headerMode == HEADER_MODE_CUSTOM) {
            super.setHeaderView(headerView);
        }
    }

    @Override
    protected void refreshOnScrollChanged() {
        if (headerMode == HEADER_MODE_ITEM) {
            inflateHeaderViewByList();
        }
        super.refreshOnScrollChanged();
    }

    @Override
    protected void inflateHeaderView() {
        if (headerMode == HEADER_MODE_CUSTOM) {
            super.inflateHeaderView();
        } else if (headerMode == HEADER_MODE_ITEM) {
            resetHeaderView(null);
        }
    }

}
