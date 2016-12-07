package com.materialdesign;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.utils.Utils;

public class TouchActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "TouchActivity";

    private ImageView imageView;

    private Matrix oriMatrix = new Matrix();//最初状态的matrix,用于恢复
    private Matrix matrix = new Matrix();//imageView的matrix,实时更新
    private Matrix savedMatrix = new Matrix();//一次事件的初始matrix状态

    private static final int NONE = 0;//无
    private static final int DRAG = 1;//单指拖拽
    private static final int ZOOM = 2;//双指缩放
    private int mode = NONE;//mode

    private boolean isInit = true;//是否待初始化oriMatrix

    // 第一个按下的手指的点
    private PointF startPoint = new PointF();
    // 两个按下的手指的触摸点的中点
    private PointF midPoint = new PointF();
    // 初始的两个手指按下的触摸点的距离
    private float oriDis;

    private GestureDetector gestureDetector;//手势检测

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_touch);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {
                Utils.showToast(getApplicationContext(), "保存成功");
            }

            //双击事件,onDoubleTapEvent会在双击事件过程中相应所有事件
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (matrix.equals(oriMatrix)) {//在原始态,放大两倍
                    matrix.set(oriMatrix);
                    matrix.postScale(2, 2, e.getX(), e.getY());
                } else {//回到最初态
                    matrix.set(oriMatrix);
                }
                imageView.setImageMatrix(matrix);
                return true;
            }

            //真正的单击事件,onSingleTapUp只是第一次点击点弹起时调用
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                finish();//单击退出activity
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean handled = gestureDetector.onTouchEvent(event);
        if (handled) {//手势已经处理了
            return true;
        }
        //处理触摸事件
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN://单指按下
                imageView.setScaleType(ImageView.ScaleType.MATRIX);//置为matrix模式用于缩放(一开始为CENTER模式,为了居中显示)
                Matrix currentMatrix = imageView.getImageMatrix();
                //首次记录oriMatrix
                if (isInit) {
                    isInit = false;
                    oriMatrix.set(currentMatrix);
                }
                //更新ImageView矩阵
                matrix.set(currentMatrix);
                //记录此次事件最初的matrix
                savedMatrix.set(currentMatrix);
                //记录按下时的点坐标
                startPoint.set(event.getX(), event.getY());
                //状态为单指拖拽
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://非第一点触摸
                //计算两点间初始距离
                oriDis = distance(event);
                savedMatrix.set(matrix);//保存当前矩阵,准备开始缩放
                updateMiddle(event);//更新中间点坐标
                mode = ZOOM;//状态为缩放
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //手指(单指、全部)放开事件,状态置空
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {//单指拖拽中
                    matrix.set(savedMatrix);//先置为此次事件初始时的矩阵
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY()
                            - startPoint.y);//再根据从初始位置到现在位置的改变做矩阵的平移
                } else if (mode == ZOOM) {//缩放中
                    float newDist = distance(event);//计算新的距离
                    matrix.set(savedMatrix);//先置为此次事件初始时的矩阵
                    float scale = newDist / oriDis;//根据新距离和初始距离算出当前缩放比
                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);//根据缩放比，以初始中点为中心进行矩阵的缩放
                }
                break;
        }
        //设置ImageView的Matrix
        imageView.setImageMatrix(matrix);
        return true;//处理完触摸事件
    }

    // 计算两个触摸点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);//0,1代表的触摸点的index,只取前两个点
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 计算两个触摸点的中点
    private void updateMiddle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        midPoint.set(x / 2, y / 2);
    }

}