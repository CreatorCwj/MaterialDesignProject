package com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cwj on 16/10/26.
 */
public class TestClipView extends View {

    private Paint paint;

    public TestClipView(Context context) {
        this(context, null);
    }

    public TestClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(100, 100, 300, 300);
        canvas.translate(100, 100);
        canvas.drawRect(0, 0, 50, 50, paint);
        canvas.restore();
    }
}
