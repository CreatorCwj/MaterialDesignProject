package com.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.materialdesign.R;
import com.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cwj on 17/4/25.
 */

public class ButterKnifeLayout extends LinearLayout {

    @Nullable
    @BindView(R.id.bk_layout_btn)
    TextView textView;

    public ButterKnifeLayout(Context context) {
        this(context, null);
    }

    public ButterKnifeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButterKnifeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.bk_layout_btn, this, true);
    }

    //不能加optional
    @OnClick
    public void onClick() {
        Utils.showToast(getContext(), textView.getText().toString());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
