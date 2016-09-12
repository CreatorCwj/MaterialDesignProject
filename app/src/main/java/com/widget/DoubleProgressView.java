package com.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.materialdesign.R;
import com.utils.Utils;


/**
 * Created by cwj on 16/6/28.
 * 显示两种百分比的圆形进度条
 */
public class DoubleProgressView extends View {

    private static final int ANIMATOR_DURATION = 400;//ms

    public static final int PERCENT_TEXT_VERTICAL = 0;
    public static final int PERCENT_TEXT_HORIZONTAL = 1;

    public static final int ANIMATION_NONE = 0;
    public static final int ANIMATION_CLOCKWISE = 1;
    public static final int ANIMATION_COUNTERCLOCKWISE = 2;

    private static final String VERTICAL_TEXT_FORMAT = "%d%%";
    private static final String HORIZONTAL_TEXT_FORMAT = "%s:%s";
    private static final String FLOAT_TEXT_FORMAT = "%.2f";

    private int color1;
    private int color2;
    private int emptyColor;
    private int gapColor;
    private int colonColor;

    private float gapAngle;
    private int roundWidth;
    private int percentTextSize;

    private int percentTextOrientation;
    private int animationStyle;

    private float data1;//数据1
    private float data2;//数据2
    private float[] percents = new float[2];//数据所占百分比
    private String[] texts = new String[2];//显示文本

    private Paint paint;//画笔
    private RectF oval;//绘制区域
    private int centerX;//中心点

    private int currentAngle;//当前时刻应该绘制到的角度

    private ValueAnimator valueAnimator;//动画
    private ValueAnimator.AnimatorUpdateListener clockwiseLis;
    private ValueAnimator.AnimatorUpdateListener counterClockwiseLis;

    public DoubleProgressView(Context context) {
        this(context, null);
    }

    public DoubleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DoubleProgressView);
            color1 = typedArray.getColor(R.styleable.DoubleProgressView_color1, Color.TRANSPARENT);
            color2 = typedArray.getColor(R.styleable.DoubleProgressView_color2, Color.TRANSPARENT);
            emptyColor = typedArray.getColor(R.styleable.DoubleProgressView_emptyColor, Color.GRAY);
            gapColor = typedArray.getColor(R.styleable.DoubleProgressView_gapColor, Color.WHITE);
            colonColor = typedArray.getColor(R.styleable.DoubleProgressView_colonColor, Color.GRAY);
            gapAngle = typedArray.getFloat(R.styleable.DoubleProgressView_gapAngle, 0);
            roundWidth = typedArray.getDimensionPixelSize(R.styleable.DoubleProgressView_roundWidth, Utils.dp2px(context, 10));
            percentTextSize = typedArray.getDimensionPixelSize(R.styleable.DoubleProgressView_percentTextSize, Utils.sp2px(context, 15));
            percentTextOrientation = typedArray.getInt(R.styleable.DoubleProgressView_percentTextOrientation, PERCENT_TEXT_VERTICAL);
            animationStyle = typedArray.getInt(R.styleable.DoubleProgressView_animationStyle, ANIMATION_NONE);
            typedArray.recycle();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); // 绘制边框,不填充
        paint.setAntiAlias(true); // 消除锯齿
        paint.setTextSize(percentTextSize);//字体大小
        paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体加粗
        oval = new RectF(0, 0, 0, 0);
    }

    private void getPercents(float[] percents) {
        if (percents == null || percents.length != 2) {
            return;
        }
        if (percentTextOrientation == PERCENT_TEXT_VERTICAL && data1 == 0 && data2 == 0) {//vertical empty
            percents[0] = percents[1] = 0;
            return;
        } else if (percentTextOrientation == PERCENT_TEXT_HORIZONTAL && data1 == 0) {//horizontal empty
            percents[0] = percents[1] = 0;
            return;
        }
        percents[0] = data1 / (data1 + data2);
        percents[1] = 1 - percents[0];
    }

    private boolean isEmpty(float[] percents) {
        if (percents == null || percents.length != 2) {
            return true;
        }
        //不同类型的空状态
        if (percentTextOrientation == PERCENT_TEXT_VERTICAL) {
            return percents[0] == 0 && percents[1] == 0;
        } else if (percentTextOrientation == PERCENT_TEXT_HORIZONTAL) {
            return percents[0] == 0;
        }
        return true;
    }

    private boolean hasGap(float[] percents) {
        return !isEmpty(percents) && percents[0] != 0 && percents[1] != 0;
    }

    private void getVerticalTexts(String[] texts, float[] percents) {
        if (texts == null || texts.length != 2 || percents == null || percents.length != 2) {
            return;
        }
        int firstPercentText = Math.round(percents[0] * 100);
        texts[0] = String.format(VERTICAL_TEXT_FORMAT, firstPercentText);
        if (percents[1] == 0) {//第二种为0%(不能与else归位一类，因为第一种为0时第二种可能不为100)
            texts[1] = String.format(VERTICAL_TEXT_FORMAT, 0);
        } else {//100-first(由于是四舍五入,所以要避免相加超出100)
            texts[1] = String.format(VERTICAL_TEXT_FORMAT, 100 - firstPercentText);
        }
    }

    private void getHorizontalTexts(String[] texts, float[] percents) {
        if (texts == null || texts.length != 2 || percents == null || percents.length != 2) {
            return;
        }
        texts[0] = percents[0] == 0 ? "0" : "1";
        if (percents[1] == 0) {//无数据
            texts[1] = "0";
        } else {//todo:除0外的数默认保留两位--待商榷
            texts[1] = String.format(FLOAT_TEXT_FORMAT, data2 / data1);
        }
    }

    public void setData(float data1, float data2) {
        this.data1 = data1;
        this.data2 = data2;
        setAnim();
    }

    private void setAnim() {
        if (valueAnimator != null) {//取消之前的动画
            valueAnimator.cancel();
        }
        switch (animationStyle) {
            case ANIMATION_NONE:
                currentAngle = 360;//一次性画出
                postInvalidate();
                break;
            case ANIMATION_CLOCKWISE:
            case ANIMATION_COUNTERCLOCKWISE:
                createAnimation(animationStyle);//动画绘制
                break;
        }
    }

    private void createAnimation(int animationStyle) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(ANIMATOR_DURATION);
        }
        if (animationStyle == ANIMATION_CLOCKWISE) {//顺时针
            if (clockwiseLis == null) {
                clockwiseLis = new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currentAngle = (int) animation.getAnimatedValue();
                        postInvalidate();//刷新
                    }
                };
            }
            valueAnimator.addUpdateListener(clockwiseLis);
            valueAnimator.start();
        } else if (animationStyle == ANIMATION_COUNTERCLOCKWISE) {//逆时针
            if (counterClockwiseLis == null) {
                counterClockwiseLis = new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                    }
                };
            }
            valueAnimator.addUpdateListener(counterClockwiseLis);
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2; // 获取圆心的x坐标
        int radius = centerX - roundWidth / 2; // 圆环的半径
        oval.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius);//绘制圆形的矩形区域
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //更新当前数据
        getPercents(percents);
        //绘制圆环
        drawProgress(canvas);
        //绘制文字
        drawText(canvas);

        testMultiShape(canvas);
    }

    private void drawProgress(Canvas canvas) {
        //根据currentAngle画圆弧
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        if (isEmpty(percents)) {//空状态
            paint.setColor(emptyColor);
            canvas.drawArc(oval, -90, currentAngle, false, paint);
        } else {
            float secondSweepAngle, realSecondSweepAngle;
            secondSweepAngle = realSecondSweepAngle = 360 * percents[1];//第二种进度扫过的角度(理论值和实际值)

            boolean flag = false;//是否已经超过最大可画角度
            if (realSecondSweepAngle > currentAngle) {
                flag = true;//已经超过,第一种不用画了
                realSecondSweepAngle = currentAngle;//不能超过(实际值)
            }
            paint.setColor(color2);//设置圆环的颜色
            canvas.drawArc(oval, -90, realSecondSweepAngle, false, paint);//画圆弧
            if (!flag) {//没有超过最大值则继续画第一种
                paint.setColor(color1);//设置圆环的颜色
                canvas.drawArc(oval, -90 + realSecondSweepAngle, currentAngle - realSecondSweepAngle, false, paint);//画圆弧
            }

            //绘制空隙(两种都有时才绘制间隙)
            if (hasGap(percents)) {
                paint.setColor(gapColor);
                float halfGapAngle = gapAngle / 2;
                //第一段
                float sweepAngle = halfGapAngle;
                if (sweepAngle > currentAngle) {
                    sweepAngle = currentAngle;
                }
                canvas.drawArc(oval, -90, sweepAngle, false, paint);
                //第二段
                float startAngle = secondSweepAngle - halfGapAngle;
                if (currentAngle <= startAngle) {//不用再画了
                    return;
                }
                sweepAngle = currentAngle - startAngle;
                if (sweepAngle > gapAngle) {
                    sweepAngle = gapAngle;
                }
                canvas.drawArc(oval, -90 + startAngle, sweepAngle, false, paint);//画圆弧
                //第三段gap
                startAngle = 360 - halfGapAngle;
                if (currentAngle <= startAngle) {
                    return;
                }
                sweepAngle = currentAngle - startAngle;
                if (sweepAngle > halfGapAngle) {
                    sweepAngle = halfGapAngle;
                }
                canvas.drawArc(oval, -90 + startAngle, sweepAngle, false, paint);//画圆弧
            }
        }
    }

    private void drawText(Canvas canvas) {
        /**
         * FontMetrics:
         * baseline:文本基准线
         * ascent:baseline到顶部字符距离
         * descent:baseline到底部字符距离
         * top:文本最高处到baseline距离,ascent最大值(为负数)
         * bottom:文本最低处到baseline距离,descent最大值(为正数)
         * 标准居中参考公式:int baseline = (getHeight() - (fontMetrics.descent - fontMetrics.ascent)) / 2 - fontMetrics.ascent;
         */
        //画进度百分比text
        paint.setStrokeWidth(0);//重置圆环宽度(drawText不需要stroke)
        if (percentTextOrientation == PERCENT_TEXT_VERTICAL) {//纵向
            //text
            getVerticalTexts(texts, percents);
            String firstPercentText = texts[0];
            String secondPercentText = texts[1];
            //width
            float firstTextWidth = paint.measureText(firstPercentText);
            float secondTextWidth = paint.measureText(secondPercentText);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline;
            //firstText
            paint.setColor(color1);
            baseline = getHeight() * 3 / 8 - (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.ascent;
            if (firstTextWidth >= secondTextWidth) {
                canvas.drawText(firstPercentText, centerX - firstTextWidth / 2, baseline, paint);//使用自己的宽绘制
            } else {
                canvas.drawText(firstPercentText, centerX + secondTextWidth / 2 - firstTextWidth, baseline, paint);//与长的右边对齐
            }
            //secondText
            paint.setColor(color2);
            baseline = getHeight() * 5 / 8 - (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.ascent;
            if (secondTextWidth >= firstTextWidth) {
                canvas.drawText(secondPercentText, centerX - secondTextWidth / 2, baseline, paint);//使用自己的宽绘制
            } else {
                canvas.drawText(secondPercentText, centerX + firstTextWidth / 2 - secondTextWidth, baseline, paint);//与长的右边对齐
            }
        } else if (percentTextOrientation == PERCENT_TEXT_HORIZONTAL) {//横向
            //text
            getHorizontalTexts(texts, percents);
            String firstPercentText = texts[0];
            String secondPercentText = texts[1];
            String totalText = String.format(HORIZONTAL_TEXT_FORMAT, firstPercentText, secondPercentText);
            //width
            float totalTextWidth = paint.measureText(totalText);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getHeight() - (fontMetrics.descent - fontMetrics.ascent)) / 2 - fontMetrics.ascent;
            float startX = centerX - totalTextWidth / 2;
            //first
            paint.setColor(color1);
            canvas.drawText(firstPercentText, startX, baseline, paint);
            startX += paint.measureText(firstPercentText);
            //:
            paint.setColor(colonColor);
            canvas.drawText(":", startX, baseline, paint);
            startX += paint.measureText(":");
            //second
            paint.setColor(color2);
            canvas.drawText(secondPercentText, startX, baseline, paint);
        }
    }

    private void testMultiShape(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(50, 50);
        path.lineTo(50, 100);
        path.lineTo(100, 100);
        path.lineTo(100, 50);
        path.lineTo(50, 50);

        //渐变
        LinearGradient gradient = new LinearGradient(50, 50, 100, 100, Color.RED, Color.YELLOW, Shader.TileMode.CLAMP);
        paint.setShader(gradient);

        canvas.drawPath(path, paint);

    }

}