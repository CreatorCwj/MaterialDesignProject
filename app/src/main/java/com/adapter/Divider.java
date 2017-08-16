package com.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cwj on 16/9/1.
 * 自定义divider,header和footer不显示divider
 */
public class Divider extends RecyclerView.ItemDecoration {

    private final int height;//高度,px
    private final Drawable drawable;

    public Divider(int height, int color) {
        this.height = height;
        drawable = new ColorDrawable(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            if (params.height == 0) {
                continue;
            }
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + height;
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int viewHeight = params != null ? params.height : 0;
        outRect.set(0, 0, 0, viewHeight != 0 ? height : 0);
    }
}
