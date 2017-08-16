package com.materialdesign;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.test_fragment.BaseActivity;
import com.widget.FlowLayout;
import com.widget.PoiBizView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwj on 17/5/8.
 */

public class FlowLayoutActivity extends BaseActivity {

    @BindView(R.id.poiBizView)
    PoiBizView poiBizView;

    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        ButterKnife.bind(this);
        poiBizView.setText("二abj", "周红", false);
    }
}
