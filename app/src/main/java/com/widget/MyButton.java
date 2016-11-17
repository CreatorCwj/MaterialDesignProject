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
public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private int firstId = -1;
    private int secondId = -1;

    private float firstX = -1;
    private float firstY = -1;
    private float secondX = -1;
    private float secondY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        int idCounts = event.getPointerCount();
        int hisSize = event.getHistorySize();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                log("ACTION_DOWN", index, id, idCounts);
                //init first if needed
                if (firstId == -1) {
                    firstId = id;
                    firstX = event.getX(index);
                    firstY = event.getY(index);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                log("ACTION_POINTER_DOWN", index, id, idCounts);
                //init second if needed
                if (secondId == -1) {
                    secondId = id;
                    secondX = event.getX(index);
                    secondY = event.getY(index);
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                log("ACTION_MOVE", index, id,idCounts);
                Log.i("ACTION_MOVE_HIS:", "" + hisSize);
                //calculate distance between first and second,if valid
                float firstNewX = 0, firstNewY = 0;
                if (firstId != -1) {
                    firstNewX = event.getX(event.findPointerIndex(firstId));
                    firstNewY = event.getY(event.findPointerIndex(firstId));
                }
                float secondNewX = 0, secondNewY = 0;
                if (secondId != -1) {
                    secondNewX = event.getX(event.findPointerIndex(secondId));
                    secondNewY = event.getY(event.findPointerIndex(secondId));
                }
                if (firstId != -1 && secondId != -1) {
                    float xDis = Math.abs(firstNewX - secondNewX);
                    float yDis = Math.abs(firstNewY - secondNewY);
                    double dis = Math.sqrt(xDis * xDis + yDis * yDis);
                    Log.i("Distance:", dis + "");
                }
                break;
            case MotionEvent.ACTION_UP:
                log("ACTION_UP", index, id, idCounts);
                //reset pointer by id
                if (id == firstId) {
                    firstId = -1;
                    firstX = firstY = -1;
                } else if (id == secondId) {
                    secondId = -1;
                    secondX = secondY = -1;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                log("ACTION_POINTER_UP", index, id, idCounts);
                //reset pointer by id
                if (id == firstId) {
                    firstId = -1;
                    firstX = firstY = -1;
                } else if (id == secondId) {
                    secondId = -1;
                    secondX = secondY = -1;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void log(String action, int index, int id, int idCounts) {
        Log.i("Action:Button1:", action + "   index:" + index + "   id:" + id + "   idCounts:" + idCounts);
    }

}
