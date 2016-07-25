package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.materialdesign.R;


/**
 * Created by cwj on 16/7/21.
 * Header和Footer的基类
 */
public abstract class BaseHeaderFooterLayout extends FrameLayout {

    private View contentView;
    protected ImageView imageView;
    protected TextView textView;

    public BaseHeaderFooterLayout(Context context) {
        this(context, null);
    }

    public BaseHeaderFooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseHeaderFooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //属性交由子类处理,子类不用回收,此处回收
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshBaseView);
            handleAttrs(typedArray);
            typedArray.recycle();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.loading_layout, this, true);
        contentView = findViewById(R.id.load_layout);
        imageView = (ImageView) findViewById(R.id.load_item_imageView);
        textView = (TextView) findViewById(R.id.load_item_textView);
    }

    /**
     * 处理属性
     */
    protected abstract void handleAttrs(@NonNull TypedArray typedArray);

    /**
     * 设置整体高度(最大滚动高度)
     */
    public void setHeight(int newHeight) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            params.height = newHeight;
            setLayoutParams(params);
        }
    }

    /**
     * 获取内容高度
     */
    public int getContentHeight() {
        return contentView.getHeight();
    }

}
