package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import com.materialdesign.R;

/**
 * Created by cwj on 17/5/5.
 * 流式布局,根据剩余宽度自动换行排版children
 */

public class FlowLayout extends ViewGroup {

    @IntDef({TOP, CENTER_VERTICAL, BOTTOM})
    public @interface ChildGravity {
    }

    public static final int TOP = 0;
    public static final int CENTER_VERTICAL = 1;
    public static final int BOTTOM = 2;

    private static final int DEFAULT_CHILD_GRAVITY = TOP;

    @ChildGravity
    public int childGravity = DEFAULT_CHILD_GRAVITY;

    private final SparseIntArray lineLastViewIndexArray = new SparseIntArray();
    private final SparseIntArray lineMaxHeightArray = new SparseIntArray();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        //noinspection WrongConstant
        childGravity = a.getInt(R.styleable.FlowLayout_childGravity, DEFAULT_CHILD_GRAVITY);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //ViewGroup的测量模式和大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //最大宽度和高度
        int maxWidth = 0;
        int height = getPaddingTop() + getPaddingBottom();

        //reset array
        lineLastViewIndexArray.clear();
        lineMaxHeightArray.clear();

        //for-measure
        boolean firstShowChild = true;//当前child是否为第一个要展示的child
        int currentLine = 0;
        int curLineWidth = getPaddingLeft() + getPaddingRight();
        int curLineHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) {//GONE不参与计算
                continue;
            }
            //根据margin测量child
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            //child的占用空间
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //加入line
            if ((curLineWidth + childWidth > widthSize) && !firstShowChild) {//换行展示,如果第一个要展示的child就大于widthSize,则将其加入第一行,和后面保持一致
                lineLastViewIndexArray.put(currentLine, i - 1);//当前行的view截至到i
                lineMaxHeightArray.put(currentLine, curLineHeight);//当前行最大高度
                maxWidth = Math.max(maxWidth, curLineWidth);//更新最大宽度
                height += curLineHeight;//更新总的最大高度
                //reset new line
                ++currentLine;
                curLineWidth = getPaddingLeft() + getPaddingRight();
                curLineHeight = 0;
                //put this child to new line
                curLineWidth += childWidth;
                curLineHeight = Math.max(curLineHeight, childHeight);//更新本行最大高度
            } else {//本行展示
                firstShowChild = false;
                curLineWidth += childWidth;//更新本行宽度
                curLineHeight = Math.max(curLineHeight, childHeight);//更新本行最大高度
            }
            //last child need put
            if (i == childCount - 1) {
                lineLastViewIndexArray.put(currentLine, i);//当前行的view截至到i之前
                lineMaxHeightArray.put(currentLine, curLineHeight);//当前行最大高度
                maxWidth = Math.max(maxWidth, curLineWidth);//更新最大宽度
                height += curLineHeight;//更新最大高度
            }
        }

        //set dimension
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize
                : maxWidth, (heightMode == MeasureSpec.EXACTLY) ? heightSize
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //for-lines
        int lines = lineLastViewIndexArray.size();
        int childCount = getChildCount();
        int startIndex = 0;
        int top = getPaddingTop();//top
        int lineMaxHeight;//当前行高度
        for (int lineIndex = 0; lineIndex < lines; lineIndex++) {
            lineMaxHeight = lineMaxHeightArray.get(lineIndex);
            int left = getPaddingLeft();//left
            int lastViewIndex = lineLastViewIndexArray.get(lineIndex);
            for (int i = startIndex; i <= lastViewIndex && i < childCount; i++) {
                View child = getChildAt(i);
                if (child == null || child.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();
                //计算childView的left,top,right,bottom
                int childLeft = left + lp.leftMargin;
                int childRight = childLeft + child.getMeasuredWidth();
                int childTop;
                switch (childGravity) {
                    case CENTER_VERTICAL:
                        childTop = top + (lineMaxHeight - child.getMeasuredHeight()) / 2 + lp.topMargin - lp.bottomMargin;
                        break;
                    case BOTTOM:
                        childTop = top + lineMaxHeight - child.getMeasuredHeight() - lp.bottomMargin;
                        break;
                    case TOP:
                    default:
                        childTop = top + lp.topMargin;
                }
                int childBottom = childTop + child.getMeasuredHeight();
                //layout
                child.layout(childLeft, childTop, childRight, childBottom);
                //更新当前行的left
                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            startIndex = lastViewIndex + 1;//更新startIndex
            top += lineMaxHeight;//更新top
        }
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        //noinspection ResourceType
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

}
