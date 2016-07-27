package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.materialdesign.R;
import com.widget.AnimationHeaderLayout;
import com.widget.ClassicHeaderLayout;
import com.widget.RotateHeaderLayout;

/**
 * Created by cwj on 16/7/22.
 * Header
 */
public abstract class HeaderLayout extends FrameLayout implements HeaderController {

    public enum HeaderStyle {
        CLASSIC(0),
        ROTATE(1),
        ANIMATION(2);

        private int id;

        HeaderStyle(int id) {
            this.id = id;
        }

        public static HeaderStyle getStyle(int style) {
            for (HeaderStyle headerStyle : HeaderStyle.values()) {
                if (headerStyle.id == style) {
                    return headerStyle;
                }
            }
            return CLASSIC;
        }

        public int getId() {
            return id;
        }

        public HeaderLayout getHeaderLayout(Context context, AttributeSet attrs) {
            switch (this) {
                case ROTATE:
                    return new RotateHeaderLayout(context, attrs);
                case ANIMATION:
                    return new AnimationHeaderLayout(context, attrs);
                case CLASSIC:
                default:
                    return new ClassicHeaderLayout(context, attrs);
            }
        }
    }

    private View containerView;
    private View contentView;

    public HeaderLayout(Context context) {
        this(context, null);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //属性交由子类处理,子类不用回收,此处回收
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            handleAttrs(typedArray);
            typedArray.recycle();
        }
    }

    protected abstract void handleAttrs(TypedArray typedArray);

    private void initView() {
        //添加containerView
        containerView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        if (containerView == null) {
            throw new IllegalArgumentException("layoutId is invalid");
        }
        addView(containerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //获取内容view
        contentView = findViewById(getContentViewId());
        if (contentView == null) {
            throw new IllegalArgumentException("contentViewId is invalid");
        }
        //处理各自view
        onViewCreated();
    }

    protected abstract void onViewCreated();

    protected abstract int getContentViewId();

    protected abstract int getLayoutId();

    public void setVisibleHeight(int height) {
        if (height < 0)
            height = 0;
        ViewGroup.LayoutParams params = containerView.getLayoutParams();
        if (params != null) {
            params.height = height;
            containerView.setLayoutParams(params);
        }
    }

    public int getVisibleHeight() {
        ViewGroup.LayoutParams params = containerView.getLayoutParams();
        if (params != null) {
            return params.height;
        }
        return 0;
    }

    /**
     * 获取内容高度
     */
    public int getContentHeight() {
        return contentView.getHeight();
    }

}
