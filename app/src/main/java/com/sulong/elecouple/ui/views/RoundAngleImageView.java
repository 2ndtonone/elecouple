package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sulong.elecouple.R;

public class RoundAngleImageView extends ClickEffectImageView {

    private static int CORNER_LEFT_TOP = 1;
    private static int CORNER_RIGHT_TOP = 2;
    private static int CORNER_LEFT_BOTTOM = 4;
    private static int CORNER_RIGHT_BOTTOM = 8;
    private Paint paint;
    private int roundWidth = 5;
    private int roundHeight = 5;
    private Paint paint2;
    private Paint paint3;
    private float edgeWidth = 0f;
    private float mHeightRatio;
    private int corner = CORNER_LEFT_TOP | CORNER_RIGHT_TOP | CORNER_LEFT_BOTTOM | CORNER_RIGHT_BOTTOM;
    private Paint paint4;
    private boolean haveEdge = false;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public void setHaveEdge(boolean haveEdge) {
        this.haveEdge = haveEdge;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        mShowClickEffect = true;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            roundWidth = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
            edgeWidth = a.getDimension(R.styleable.RoundAngleImageView_edgeWidth, edgeWidth);
            mHeightRatio = a.getFloat(R.styleable.RoundAngleImageView_heightRatio, mHeightRatio);
            corner = a.getInt(R.styleable.RoundAngleImageView_corner, corner);
            a.recycle();
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth * density);
            roundHeight = (int) (roundHeight * density);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);

        paint3 = new Paint();
        paint3.setColor(Color.WHITE);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(edgeWidth / 2);
        paint3.setAntiAlias(true);

        paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeWidth(edgeWidth / 2);
        paint4.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLiftUp(canvas2);
        drawRightUp(canvas2);
        drawLiftDown(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        if (haveEdge) {
            drawEdge(canvas2);
        }
        bitmap.recycle();
    }

    private void drawEdge(Canvas canvas) {
        canvas.drawArc(new RectF(
                        0,
                        0,
                        (roundWidth) * 2,
                        (roundHeight) * 2),
                -90,
                -90, false, paint4);
        canvas.drawArc(new RectF(
                        0,
                        getHeight() - roundHeight * 2,
                        0 + roundWidth * 2,
                        getHeight()),
                90,
                90, false, paint4);
        canvas.drawArc(new RectF(
                getWidth() - roundWidth * 2,
                getHeight() - roundHeight * 2,
                getWidth(),
                getHeight()), 0, 90, false, paint4);
        canvas.drawArc(new RectF(
                        getWidth() - roundWidth * 2,
                        0,
                        getWidth(),
                        0 + roundHeight * 2),
                -90,
                90, false, paint4);
        canvas.drawLine(0, roundHeight, 0, getHeight() - roundHeight, paint3);
        canvas.drawLine(roundWidth, 0, getWidth() - roundWidth, 0, paint3);
        canvas.drawLine(getWidth(), (float) roundHeight, getWidth(), (float) (getHeight() - roundHeight), paint3);
        canvas.drawLine(roundWidth, getHeight(), getWidth() - roundWidth, getHeight(), paint3);
    }

    private void drawLiftUp(Canvas canvas) {
        if ((corner & CORNER_LEFT_TOP) == 0) {
            return;
        }
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(
                        0,
                        0,
                        roundWidth * 2,
                        roundHeight * 2),
                -90,
                -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        if ((corner & CORNER_LEFT_BOTTOM) == 0) {
            return;
        }
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(
                        0,
                        getHeight() - roundHeight * 2,
                        0 + roundWidth * 2,
                        getHeight()),
                90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        if ((corner & CORNER_RIGHT_BOTTOM) == 0) {
            return;
        }
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(
                getWidth() - roundWidth * 2,
                getHeight() - roundHeight * 2,
                getWidth(),
                getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        if ((corner & CORNER_RIGHT_TOP) == 0) {
            return;
        }
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(
                        getWidth() - roundWidth * 2,
                        0,
                        getWidth(),
                        0 + roundHeight * 2),
                -90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    public void setHeightRatio(float ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.001d) {
            // set the image views size
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
