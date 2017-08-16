package com.refreshloadview;

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
 * 1.可以配置不同状态的显示文字以及footer的背景色
 */
public class FooterLayout extends FrameLayout implements IFooter {

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
        if (TextUtils.isEmpty(resetText)) {
            resetText = getContext().getResources().getString(R.string.footer_reset_text);
        }
        if (TextUtils.isEmpty(loadingText)) {
            loadingText = getContext().getResources().getString(R.string.footer_loading_text);
        }
        if (TextUtils.isEmpty(noMoreText)) {
            noMoreText = getContext().getResources().getString(R.string.footer_no_more_text);
        }
    }

    @Override
    public void onResetState() {
        progressBar.setVisibility(GONE);
        textView.setText(resetText);
    }

    @Override
    public void onLoadingState() {
        progressBar.setVisibility(VISIBLE);
        textView.setText(loadingText);
    }

    @Override
    public void onLoadStop() {
        progressBar.setVisibility(GONE);
        textView.setText(noMoreText);
    }

}
