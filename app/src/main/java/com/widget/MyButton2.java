package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by cwj on 16/11/10.
 */
public class MyButton2 extends Button {
    public MyButton2(Context context) {
        super(context);
    }

    public MyButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        Log.i("BUTTON-DRAW", "performTraversal");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        int idCounts = event.getPointerCount();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                log("ACTION_DOWN", index, id, idCounts);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                log("ACTION_POINTER_DOWN", index, id, idCounts);
                break;
            case MotionEvent.ACTION_MOVE:
//                log("ACTION_MOVE", index, id,idCounts);
                break;
            case MotionEvent.ACTION_UP:
                log("ACTION_UP", index, id, idCounts);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                log("ACTION_POINTER_UP", index, id, idCounts);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void log(String action, int index, int id, int idCounts) {
        Log.i("Action:Button2:", action + "   index:" + index + "   id:" + id + "   idCounts:" + idCounts);
    }

}
