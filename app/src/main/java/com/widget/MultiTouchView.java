package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.utils.Utils;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by cwj on 16/11/15.
 * 多点触摸
 */
public class MultiTouchView extends View {

    private HashMap<Integer, CoordModel> hashMap = new HashMap<>();

    private Paint linePaint;

    private int[] colors = {Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.GRAY};

    public MultiTouchView(Context context) {
        this(context, null);
    }

    public MultiTouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(Utils.dp2px(getContext(), 1.5f));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index, pointerId;
        CoordModel coordModel;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                log(event);
                //将pointerId及其对应的初始化坐标保存到map中
                index = event.getActionIndex();
                pointerId = event.getPointerId(index);
                coordModel = new CoordModel();
                coordModel.startX = coordModel.currentX = event.getX(index);
                coordModel.startY = coordModel.currentY = event.getY(index);
                coordModel.color = colors[hashMap.size() % colors.length];
                hashMap.put(pointerId, coordModel);
                break;
            case MotionEvent.ACTION_MOVE:
                log(event);
                //将每个触摸点坐标更新

                //根据id遍历
//                for (Map.Entry<Integer, CoordModel> entry : hashMap.entrySet()) {
//                    pointerId = entry.getKey();
//                    index = event.findPointerIndex(pointerId);
//                    if (index != -1) {
//                        coordModel = entry.getValue();
//                        coordModel.currentX = event.getX(index);
//                        coordModel.currentY = event.getY(index);
//                    }
//                }

                //根据index遍历
                int pointerCount = event.getPointerCount();
                for (index = 0; index < pointerCount; index++) {
                    pointerId = event.getPointerId(index);
                    coordModel = hashMap.get(pointerId);
                    if (coordModel != null) {
                        coordModel.currentX = event.getX(index);
                        coordModel.currentY = event.getY(index);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //将离开的触摸点从map中移除
                index = event.getActionIndex();
                pointerId = event.getPointerId(index);
                hashMap.remove(pointerId);
                break;
            case MotionEvent.ACTION_CANCEL:
                //全部重置
                hashMap.clear();
                break;
        }
        invalidate();//刷新UI
        return true;
    }

    private void log(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        Log.i("IndexId:", "rawX:" + event.getRawX() + " x:" + event.getX(index) + " index:" + index);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //遍历绘制每个路径
        Collection<CoordModel> coordModels = hashMap.values();
        for (CoordModel coordModel : coordModels) {
            if (coordModel == null) {
                continue;
            }
            linePaint.setColor(coordModel.color);
            canvas.drawLine(coordModel.startX, coordModel.startY, coordModel.currentX, coordModel.currentY, linePaint);
        }
    }

    private static class CoordModel {
        private float startX;
        private float startY;
        private float currentX;
        private float currentY;
        private int color;
    }
}
