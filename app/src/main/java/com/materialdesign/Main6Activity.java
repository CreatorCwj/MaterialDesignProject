package com.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.utils.Utils;
import com.widget.TestClipView;

public class Main6Activity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    private TestClipView testClipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
//        testClipView = (TestClipView) findViewById(R.id.testClipView);
//        ((View) testClipView.getParent()).scrollTo(0, 100);
//        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.tv1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.myLl).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1300));
//                findViewById(R.id.myLl).invalidate();
            }
        });
//        textView.invalidate();
//        TextView textView = new TextView(this);
//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
//        textView.scrollTo();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                String text = oldFocus != null ? oldFocus.getClass().getName() : "old is null;";
                text += newFocus != null ? newFocus.getClass().getName() : "new is null.";
                Utils.showToast(getApplicationContext(), text);
            }
        });
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        event.getAction();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
        }
        return super.onTouchEvent(event);
    }
}
