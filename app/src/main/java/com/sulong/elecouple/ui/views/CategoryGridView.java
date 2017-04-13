package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class CategoryGridView extends GridView {
    //自定义GridView,解决ScrollView嵌套GridView,GridView只能显示第一行
    public CategoryGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryGridView(Context context) {
        super(context);
    }

    public CategoryGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }

}
