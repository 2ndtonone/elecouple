package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liu Qing on 2015/9/10.
 */
public class UpTriangleView extends View {

    private Paint paintTriangle;
    private int colorTriangle = 0xfff6f3f3;
    private Path pathTriangle;

    public UpTriangleView(Context context) {
        super(context);
        init();
    }

    public UpTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public UpTriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(pathTriangle, paintTriangle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initShape();
        invalidate();
    }

    private void init() {
        paintTriangle = new Paint();
        paintTriangle.setColor(colorTriangle);
        paintTriangle.setStyle(Paint.Style.FILL);
        paintTriangle.setAntiAlias(true);
    }

    private void initShape() {
        float point1x = getWidth() / 2;
        float point1y = 0;
        float point2x = 0;
        float point2y = getHeight();
        float point3x = getWidth();
        float point3y = getHeight();

        pathTriangle = new Path();
        pathTriangle.moveTo(point1x, point1y);
        pathTriangle.lineTo(point2x, point2y);
        pathTriangle.lineTo(point3x, point3y);
        pathTriangle.close();
    }

}
