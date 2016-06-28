package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.utils.Utils;

/**
 * 显示两种百分比的圆形进度条
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环/百分比文字的颜色
     */
    private int color1;
    private int color2;

    /**
     * 中间进度百分比的字符串的字体大小
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 圆弧区域
     */
    private RectF oval;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        // 获取自定义属性和默认值
        color1 = Color.parseColor("#255FDC");
        color2 = Color.parseColor("#4EB6EE");
        textSize = Utils.sp2px(getContext(), 15);
        roundWidth = Utils.dp2px(getContext(), 10);
        max = 100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (oval == null) {
            oval = new RectF(0, 0, 0, 0);
        }

        /**
         * 画圆弧
         */
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); // 圆环的半径
        oval.set(centre - radius, centre - radius, centre + radius, centre + radius);
        paint.setColor(color1); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
//        canvas.drawCircle(centre, centre, radius, paint);//整个圆形
        canvas.drawArc(oval, -90, -(float) 360 * 36 / max, false, paint);//画圆弧
        paint.setColor(color2);
        canvas.drawArc(oval, -90, (float) 360 * 64 / max, false, paint);//画圆弧
        //绘制空隙
        paint.setColor(Color.WHITE);
        canvas.drawArc(oval, -90, 1, false, paint);//画圆弧
        canvas.drawArc(oval, -90, -1, false, paint);//画圆弧
        canvas.drawArc(oval, -(90 + (float) 360 * 36 / max), 1, false, paint);//画圆弧
        canvas.drawArc(oval, -(90 + (float) 360 * 36 / max), -1, false, paint);//画圆弧

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(color1);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
        int percent = (int) (((float) 36 / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        /**
         * FontMetrics:
         * baseline:文本基准线
         * top:文本最高处到baseline距离(为负数)
         * bottom:文本最低处到baseline距离(为正数)
         */
        //根据font属性来决定baseline作为字体的y轴中心
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = getMeasuredHeight() * 3 / 8 - fontMetrics.top / 2;
        canvas.drawText(percent + "%", centre - textWidth / 2, baseline, paint); // 画出进度百分

        paint.setColor(color2);
        percent = max - percent;
        textWidth = paint.measureText(percent + "%");
        baseline = getMeasuredHeight() * 5 / 8 - fontMetrics.top / 2;
        canvas.drawText(percent + "%", centre - textWidth / 2, baseline, paint); // 画出进度百分

        //横向文字展示比例
        textWidth = paint.measureText("1:3.44");
        baseline = (getMeasuredHeight() - fontMetrics.top) / 2;
        float startX = centre - textWidth / 2;
        //第一个比例
        paint.setColor(color1);
        canvas.drawText("1", startX, baseline, paint);
        startX += paint.measureText("1");
        //:
        paint.setColor(Color.LTGRAY);
        canvas.drawText(":", startX, baseline, paint);
        startX += paint.measureText(":");
        //第二个比例
        paint.setColor(color2);
        canvas.drawText("3.44", startX, baseline, paint);

        //多边形测试
//        testMultiShape(canvas);
    }

    private void testMultiShape(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(50, 50);
        path.lineTo(50, 100);
        path.lineTo(100, 150);
        path.lineTo(120, 50);
        path.lineTo(50, 50);

        //渐变
        LinearGradient gradient = new LinearGradient(85, 50, 85, 150, Color.RED, Color.YELLOW, Shader.TileMode.MIRROR);
        paint.setShader(gradient);

        canvas.drawPath(path, paint);

    }

}