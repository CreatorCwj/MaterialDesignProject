package com.materialdesign;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.utils.Utils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_uiperformance)
public class UIPerformanceActivity extends RoboActivity implements View.OnClickListener {

    @InjectView(R.id.btn0)
    private Button btn0;

    @InjectView(R.id.viewStub)
    private ViewStub viewStub;

    private Button stub_btn1;
    private Button stub_btn2;

    //    @InjectView(R.id.include_item1)
    private View include_item1;
    //
//    @InjectView(R.id.stub_item)
//    private View stub_item;
//
    @InjectView(R.id.btn1)
    private Button btn1;

    @InjectView(R.id.btn2)
    private Button btn2;
//
//    private Button btn3;
//    private Button btn4;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setListener();
    }

    private void testAnimator() {
        btn0.animate().translationY(0).setDuration(400).alpha(0.5f);
    }

    private void init() {
        include_item1 = findViewById(R.id.include_item1);//merge没有根布局,无法通过id获取view,怎么办
//        btn1 = (Button) item1.findViewById(R.id.btn1);
//        btn2 = (Button) item1.findViewById(R.id.btn2);
//        btn3 = (Button)stub_item.findViewById(R.id.btn1);
//        btn4 = (Button)stub_item.findViewById(R.id.btn2);
    }

    private void setListener() {
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        testAnimator();
        if (v instanceof Button)
            Utils.showToast(this, ((Button) v).getText().toString());
        else Utils.showToast(this, "Not a Button!");
        if (view == null) {
            view = viewStub.inflate();//只能inflate一次,inflate后没有了parent,再次inflate会抛异常
            stub_btn1 = (Button) view.findViewById(R.id.stub_btn1);
            stub_btn2 = (Button) view.findViewById(R.id.stub_btn2);
            stub_btn1.setOnClickListener(this);
            stub_btn2.setOnClickListener(this);
        }
    }
}
