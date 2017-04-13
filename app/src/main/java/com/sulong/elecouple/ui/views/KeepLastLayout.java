package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Liu Qing on 2015/9/25.
 */
public class KeepLastLayout extends LinearLayout {

    public KeepLastLayout(Context context) {
        super(context);
    }

    public KeepLastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeepLastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalStateException("There must be two child view, but current is " + getChildCount());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View firstView = getChildAt(0);
        View secondView = getChildAt(1);
        int secondViewWidth = 0;
        if (secondView.getVisibility() != GONE) {
            measureChildWithMargins(secondView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            secondViewWidth = secondView.getMeasuredWidth();
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int firstWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize - secondViewWidth, widthMode);
        measureChildWithMargins(firstView, firstWidthMeasureSpec, 0, heightMeasureSpec, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View firstView = getChildAt(0);
        View secondView = getChildAt(1);
        final MarginLayoutParams firstLp = (MarginLayoutParams) firstView.getLayoutParams();
        final MarginLayoutParams secondLp = (MarginLayoutParams) secondView.getLayoutParams();

        int top = getTop() + getPaddingTop();
        int bottom = getBottom() + getPaddingBottom();
        int actualHeight = bottom - top;

        int firstViewLeft = getLeft() + getPaddingLeft() + firstLp.leftMargin;
        int firstViewRight = firstViewLeft + firstView.getMeasuredWidth();
        int firstViewTop = top + (actualHeight - firstView.getMeasuredHeight()) / 2;
        int firstViewBottom = firstViewTop + firstView.getMeasuredHeight();
        firstView.layout(firstViewLeft, firstViewTop, firstViewRight, firstViewBottom);

        if (secondView.getVisibility() != GONE) {
            int secondViewLeft = firstViewRight + firstLp.rightMargin + secondLp.leftMargin;
            int secondViewRight = secondViewLeft + secondView.getMeasuredWidth();
            if (secondViewRight > getRight()) {
                secondViewRight = getRight() - getPaddingRight() - secondLp.rightMargin;
                secondViewLeft = secondViewRight - secondView.getMeasuredWidth();
            }
            int secondViewTop = top + (actualHeight - secondView.getMeasuredHeight()) / 2;
            int secondViewBottom = secondViewTop + secondView.getMeasuredHeight();
            secondView.layout(secondViewLeft, secondViewTop, secondViewRight, secondViewBottom);
        }
    }

}
