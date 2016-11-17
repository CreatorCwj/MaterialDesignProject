package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.utils.Utils;

/**
 * Created by cwj on 16/11/15.
 */
public class ScaleGestureView extends View {

    private float firstDownX = -1;
    private float firstDownY = -1;
    private float secondDownX = -1;
    private float secondDownY = -1;

    private float firstCurrentX = -1;
    private float firstCurrentY = -1;
    private float secondCurrentX = -1;
    private float secondCurrentY = -1;

    private Paint linePaint;

    private ScaleGestureDetector scaleGestureDetector;

    public ScaleGestureView(Context context) {
        this(context, null);
    }

    public ScaleGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(Utils.dp2px(getContext(), 1.5f));
        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleGestureListener);
    }

    private ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //true代表不再检测scale，false代表继续检测
            Log.i("scaleFactor:", detector.getScaleFactor() + "");
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //true代表开始检测scale，false代表不开始检测
//            detector.getScaleFactor();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
//        int action = event.getActionMasked();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_POINTER_DOWN:
//                if (firstId == -1) {//初始化first点
//                    int index = event.getActionIndex();
//                    firstId = event.getPointerId(index);
//                    firstDownX = event.getX(index);
//                    firstDownY = event.getY(index);
//                } else if (secondId == -1) {//初始化second点
//                    int index = event.getActionIndex();
//                    secondId = event.getPointerId(index);
//                    secondDownX = event.getX(index);
//                    secondDownY = event.getY(index);
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (firstId != -1) {//记录first
//                    int index = event.findPointerIndex(firstId);
//                    if (index != -1) {
//                        firstCurrentX = event.getX(index);
//                        firstCurrentY = event.getY(index);
//                    }
//                }
//                if (secondId != -1) {//记录second
//                    int index = event.findPointerIndex(secondId);
//                    if (index != -1) {
//                        secondCurrentX = event.getX(index);
//                        secondCurrentY = event.getY(index);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                int id = event.getPointerId(event.getActionIndex());
//                if (id == firstId) {//first失效
//                    firstId = -1;
//                    firstDownX = firstDownY = firstCurrentX = firstCurrentY = -1;
//                } else if (id == secondId) {//second失效
//                    secondId = -1;
//                    secondDownX = secondDownY = secondCurrentX = secondCurrentY = -1;
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                //全部重置
//                firstId = -1;
//                firstDownX = firstDownY = firstCurrentX = firstCurrentY = -1;
//                secondId = -1;
//                secondDownX = secondDownY = secondCurrentX = secondCurrentY = -1;
//                break;
//        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstCanDraw()) {
            linePaint.setColor(Color.BLUE);
            canvas.drawLine(firstDownX, firstDownY, firstCurrentX, firstCurrentY, linePaint);
        }
        if (secondCanDraw()) {
            linePaint.setColor(Color.GREEN);
            canvas.drawLine(secondDownX, secondDownY, secondCurrentX, secondCurrentY, linePaint);
        }
    }

    private boolean secondCanDraw() {
        return secondDownX != -1 && secondDownY != -1 && secondCurrentX != -1 && secondCurrentY != -1;
    }

    private boolean firstCanDraw() {
        return firstDownX != -1 && firstDownY != -1 && firstCurrentX != -1 && firstCurrentY != -1;
    }
}
