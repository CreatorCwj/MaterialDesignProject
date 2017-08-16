package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.materialdesign.R;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.ButterKnife;

/**
 * Created by cwj on 17/5/9.
 * 门店列表item的业务状态自定义view
 */

public class PoiBizView extends View {

    @BindColor(R.color.color_88A6BF)
    int onlineTextColor;

    @BindColor(R.color.color_999999)
    int offlineTextColor;

    @BindColor(R.color.color_E8F0FE)
    int onlineBackgroundColor;

    @BindColor(R.color.color_E7E7E7)
    int offlineBackgroundColor;

    @BindColor(android.R.color.white)
    int bdRegionColor;

    @BindDimen(R.dimen.dp_1_5)
    int strokeWidth;

    @BindDimen(R.dimen.dp_5)
    int lrPadding;

    @BindDimen(R.dimen.dp_2)
    int tbPadding;

    @BindDimen(R.dimen.sp_13)
    int textSize;

    @BindDimen(R.dimen.dp_2)
    int radius;

    private String bizName;
    private String bdName;
    private boolean isOnline;

    private int bdNameWidth;

    private final Paint paint = new Paint();
    private final RectF rectF = new RectF();

    public PoiBizView(Context context) {
        this(context, null);
    }

    public PoiBizView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiBizView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
        initPaint();
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 刷新
     */
    public void setText(String bizName, String bdName, boolean isOnline) {
        this.bizName = bizName;
        this.bdName = bdName;
        this.isOnline = isOnline;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        resetTextPaint();
        //width
        int bizNameWidth = TextUtils.isEmpty(bizName) ? 0 : (int) paint.measureText(bizName);
        bdNameWidth = TextUtils.isEmpty(bdName) ? 0 : (int) paint.measureText(bdName);
        int width = bizNameWidth + bdNameWidth + lrPadding * 4;
        //height
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int textHeight = fontMetrics.descent - fontMetrics.ascent;
        int height = textHeight + tbPadding * 2;
        //set dimension
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawRegion(canvas);
        drawText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        resetBackgroundPaint();
        rectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    private void drawRegion(Canvas canvas) {
        resetBDRegionPaint();
        rectF.set(getWidth() - (bdNameWidth + lrPadding * 2), strokeWidth, getWidth() - strokeWidth, getHeight() - strokeWidth);
        canvas.drawRect(rectF, paint);
    }

    private void drawText(Canvas canvas) {
        resetTextPaint();
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getHeight() - (fontMetrics.descent - fontMetrics.ascent)) / 2 - fontMetrics.ascent;
        //bizName
        if (!TextUtils.isEmpty(bizName)) {
            canvas.drawText(bizName, lrPadding, baseline, paint);
        }
        //bdName
        if (!TextUtils.isEmpty(bdName)) {
            canvas.drawText(bdName, getWidth() - (lrPadding + bdNameWidth), baseline, paint);
        }
    }

    private void resetTextPaint() {
        paint.setTextSize(textSize);
        paint.setColor(isOnline ? onlineTextColor : offlineTextColor);
    }

    private void resetBackgroundPaint() {
        paint.setColor(isOnline ? onlineBackgroundColor : offlineBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
    }

    private void resetBDRegionPaint() {
        paint.setColor(bdRegionColor);
        paint.setStyle(Paint.Style.FILL);
    }
}
