package com.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.utils.Utils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @InjectView(R.id.textInputLayout)
    private TextInputLayout textInputLayout;

    @InjectView(R.id.textInputLayout1)
    private TextInputLayout textInputLayout1;

    @InjectView(R.id.testSnackBar)
    private Button testSnackBar;

    @InjectView(R.id.testCoordinatorLayout)
    private Button testCoordinatorLayout;

    @InjectView(R.id.testRecyclerView)
    private Button testRecyclerView;

    @InjectView(R.id.floatBtn)
    private FloatingActionButton floatBtn;

    @InjectView(R.id.testNavigationView)
    private Button testNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testTextInputLayout();
        testSnackBar();
        testRecyclerView();
        testCoordinatorLayout();
        testFloatingButton();
        testNavigationView();
    }

    private void testNavigationView() {
        testNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NavigationViewActivity.class));
            }
        });
    }


    private void testFloatingButton() {
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(MainActivity.this, "FloatingButton");
            }
        });
    }

    private void testRecyclerView() {
        testRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
            }
        });
    }

    private void testCoordinatorLayout() {
        testCoordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestCoordinatorLayout.class));
            }
        });
    }

    private void testSnackBar() {
        testSnackBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSnackBar();
            }
        });
    }

    private void openSnackBar() {
        final Snackbar snackbar = Snackbar.make(textInputLayout, "I am snackBar", Snackbar.LENGTH_SHORT);
        snackbar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.color50));
//        snackbar.setCallback(new Snackbar.Callback() {
//            @Override
//            public void onDismissed(Snackbar snackbar, int event) {
//                super.onDismissed(snackbar, event);
//                testSnackBar.setText("snackBar取消");
//            }
//
//            @Override
//            public void onShown(Snackbar snackbar) {
//                super.onShown(snackbar);
//                testSnackBar.setText("snackBar显示");
//            }
//        });
//        if (!snackbar.isShown())
//            snackbar.show();
    }

    private void testTextInputLayout() {
//        textInputLayout.setHintAnimationEnabled(true);
        textInputLayout.setHint("这是提示,永不消失");
        textInputLayout1.setHint("这是提示,永不消失");
//        textInputLayout.setError("这是错误提示,赫赫");//错误提示显示时hint也会变成和其一样的颜色
//        textInputLayout.setErrorEnabled(false);
//        textInputLayout.setCounterMaxLength(10);//设置右下角的最长输入长度提示
//        if (textInputLayout.getEditText() != null)
//            textInputLayout.getEditText().setText("赫赫");
    }

}
