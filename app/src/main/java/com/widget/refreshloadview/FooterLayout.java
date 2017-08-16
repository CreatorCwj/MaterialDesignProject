package com.widget.refreshloadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.materialdesign.R;

/**
 * Created by cwj on 16/7/22.
 * Footer
 */
public class FooterLayout extends FrameLayout implements FooterController {

    protected TextView textView;
    protected ProgressBar progressBar;

    private int backColor;
    private int textColor;

    private String resetText;
    private String loadingText;
    private String noMoreText;

    public FooterLayout(Context context) {
        this(context, null);
    }

    public FooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, null, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadView);
            backColor = typedArray.getColor(R.styleable.RefreshLoadView_footerBackgroundColor, Color.TRANSPARENT);
            textColor = typedArray.getColor(R.styleable.RefreshLoadView_footerTextColor, Color.BLACK);
            resetText = typedArray.getString(R.styleable.RefreshLoadView_footerResetText);
            loadingText = typedArray.getString(R.styleable.RefreshLoadView_footerLoadingText);
            noMoreText = typedArray.getString(R.styleable.RefreshLoadView_footerNoMoreText);
            typedArray.recycle();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.footer_layout, this, true);
        textView = (TextView) findViewById(R.id.footer_item_textView);
        progressBar = (ProgressBar) findViewById(R.id.footer_item_progressBar);
        setAttrs();
    }

    private void setAttrs() {
        setBackgroundColor(backColor);
        textView.setTextColor(textColor);
    }

    @Override
    public void onResetState() {
        progressBar.setVisibility(GONE);
        if (!TextUtils.isEmpty(resetText)) {
            textView.setText(resetText);
        } else {
            textView.setText("加载更多");
        }
    }

    @Override
    public void onLoadingState() {
        progressBar.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(loadingText)) {
            textView.setText(loadingText);
        } else {
            textView.setText("正在加载...");
        }
    }

    @Override
    public void onLoadStop() {
        progressBar.setVisibility(GONE);
        if (!TextUtils.isEmpty(noMoreText)) {
            textView.setText(noMoreText);
        } else {
            textView.setText("无更多数据");
        }
    }

}
