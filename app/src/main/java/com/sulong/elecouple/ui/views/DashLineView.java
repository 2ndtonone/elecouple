package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sulong.elecouple.R;

/**
 * Created by ydh on 2016/9/30.
 */

public class DashLineView extends View {

    @ColorInt
    int mDashColor;
    int mDashSolidLength;
    int mDashWidth;
    int mDashGap;
    @ColorInt
    int mCircleColor;
    int mCircleRadius;
    Paint mDashPaint;
    Paint mCirclePaint;

    public DashLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DashLineView(Context context) {
        super(context);
        init(null);
    }

    void init(AttributeSet attrs) {
        mDashColor = 0xffe5e5e5;
        mDashSolidLength = dip2px(8);
        mDashGap = dip2px(4);
        mDashWidth = dip2px(1);
        mCircleColor = Color.WHITE;
        mCircleRadius = dip2px(10);
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DashLineView);
            mDashColor = ta.getColor(R.styleable.DashLineView_dl_dash_color, mDashColor);
            mDashSolidLength = ta.getDimensionPixelSize(R.styleable.DashLineView_dl_dash_solid_length, mDashSolidLength);
            mDashWidth = ta.getDimensionPixelSize(R.styleable.DashLineView_dl_dash_width, mDashWidth);
            mDashGap = ta.getDimensionPixelSize(R.styleable.DashLineView_dl_dash_gap, mDashGap);
            mCircleColor = ta.getColor(R.styleable.DashLineView_dl_half_circle_color, mCircleColor);
            mCircleRadius = ta.getDimensionPixelSize(R.styleable.DashLineView_dl_half_circle_radius, mCircleRadius);
            ta.recycle();
        }
        mDashPaint = new Paint();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{mDashSolidLength, mDashGap}, 0));
        mDashPaint.setColor(mDashColor);
        mDashPaint.setStrokeWidth(mDashWidth);
        setLayerType(LAYER_TYPE_SOFTWARE, mDashPaint);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCircleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        int centerY = getHeight() / 2;
        canvas.drawLine(left, centerY, right, centerY, mDashPaint);
        canvas.drawCircle(left, centerY, mCircleRadius, mCirclePaint);
        canvas.drawCircle(right, centerY, mCircleRadius, mCirclePaint);
    }

    int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }
}