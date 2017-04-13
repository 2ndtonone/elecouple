package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 此类专用于O2O商家列表里的商家名称和尾随其后的满减送优惠图标的显示效果（优惠图标先跟随文字，文字过长时图标固定住不被挤出屏幕）
 * Created by ydh on 2016/11/1.
 */
public class FollowThenFixLayout extends LinearLayout {

    public FollowThenFixLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FollowThenFixLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowThenFixLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 先测量子View原始宽度
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.UNSPECIFIED);
        getChildAt(0).measure(childWidthMeasureSpec, heightMeasureSpec);
        getChildAt(1).measure(childWidthMeasureSpec, heightMeasureSpec);
        int leftChildWidth = getChildAt(0).getMeasuredWidth();
        int rightChildWidth = getChildAt(1).getMeasuredWidth();

        // 调用原来的测量方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 如果子View原始宽度总和超过容器宽度，就固定住第二个View的宽度，减小第一个View的宽度
        int containerWidth = getMeasuredWidth();
        if (leftChildWidth + rightChildWidth > containerWidth) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(containerWidth - rightChildWidth, MeasureSpec.EXACTLY);
            getChildAt(0).measure(childWidthMeasureSpec, heightMeasureSpec);
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(rightChildWidth, MeasureSpec.EXACTLY);
            getChildAt(1).measure(childWidthMeasureSpec, heightMeasureSpec);
        }
    }
}