package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

import com.sulong.elecouple.R;

public class DynamicHeightImageView extends ClickEffectImageView {

    protected double mHeightRatio = 0;
    protected int mMaxWidth = 0;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeightImageView);
            mHeightRatio = a.getFloat(R.styleable.DynamicHeightImageView_dhiv_heightRatio, 0);
            mMaxWidth = a.getDimensionPixelSize(R.styleable.DynamicHeightImageView_dhiv_maxWidth, mMaxWidth);
            a.recycle();
        }
        if (mMaxWidth == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mMaxWidth = getMaxWidth();
        }
    }

    public DynamicHeightImageView(Context context) {
        this(context, null);
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.001d) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (mMaxWidth > 0) {
                width = Math.min(width, mMaxWidth);
            }
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
