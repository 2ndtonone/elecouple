package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class ModuleGrid extends GridView {
    private int position = 0;

    public ModuleGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST
        );
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        position = getSelectedItemPosition() - getFirstVisiblePosition();
        if (position < 0) {
            return i;
        } else {
            if (i == childCount - 1) {//这是最后一个需要刷新的item
                if (position > i) {
                    position = i;
                }
                return position;
            }
            if (i == position) {//这是原本要在最后一个刷新的item
                return childCount - 1;
            }
        }
        return i;
    }
}