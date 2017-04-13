package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.sulong.elecouple.R;

/**
 * Created by ydh on 2017/1/10.
 */

public class PsbcZebraCrossing extends View {

    private float height;
    private float first_width;
    private float second_width;
    private float offset_width;
    private int first_color;
    private int second_color;
    private int canvas_color;

    public PsbcZebraCrossing(Context context) {
        super(context);
    }

    public PsbcZebraCrossing(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ZebraCrossingView);
        height = typedArray.getDimension(R.styleable.ZebraCrossingView_height, 15);
        first_width = typedArray.getDimension(R.styleable.ZebraCrossingView_first_width, 10);
        second_width = typedArray.getDimension(R.styleable.ZebraCrossingView_second_width, 10);
        offset_width = typedArray.getDimension(R.styleable.ZebraCrossingView_offset_width, 10);
        first_color = typedArray.getColor(R.styleable.ZebraCrossingView_first_color, Color.RED);
        second_color = typedArray.getColor(R.styleable.ZebraCrossingView_second_color, Color.GREEN);
        canvas_color = typedArray.getColor(R.styleable.ZebraCrossingView_canvas_color, Color.WHITE);
        typedArray.recycle();
    }

    public PsbcZebraCrossing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();//获取控件宽
        Path path = new Path();
        //先判断被除数是否为0决定绘制多少个平行四边形
        int count = (first_width + second_width == 0) ? 0 : width / (int) (first_width + second_width) + 1;
        for (int i = 0; i < count; i++) {
            canvas.save();
            path.reset();//重置路径
            path.moveTo(offset_width + (first_width + second_width) * i, 0);//左上点
            path.lineTo((first_width + second_width) * i, height);//左下点
            path.lineTo((first_width + second_width) * (i + 1), height);//右下点
            path.lineTo(offset_width + (first_width + second_width) * (i + 1), 0);//右上点
            canvas.clipPath(path);//截取路径所绘制的图形
            if (i % 2 == 0) {
                canvas.drawColor(second_color);//绘制第二种颜色
            } else {
                canvas.drawColor(first_color);//绘制第一种颜色
            }
            path.reset();//重置路径，准备绘制第三种颜色的平行四边形
            path.moveTo(offset_width + first_width + (first_width + second_width) * i, 0);
            path.lineTo(first_width + (first_width + second_width) * i, height);
            path.lineTo((first_width + second_width) * (i + 1), height);
            path.lineTo(offset_width + (first_width + second_width) * (i + 1), 0);
            canvas.clipPath(path);
            canvas.drawColor(canvas_color);
            canvas.restore();
        }
    }


}
