package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by ydh on 2015/11/3.
 */
public class LineView extends View {

    private ArrayList<Float> doubleArrayList;

    public LineView(Context context) {
        super(context);
    }

    public LineView(ArrayList<Float> doubles, Context context) {
        super(context);
        this.doubleArrayList = doubles;
    }

    public ArrayList<Float> getArrayListDouble() {
        return doubleArrayList;
    }

    public void setArrayListDouble(ArrayList<Float> doubleArrayList) {
        this.doubleArrayList = doubleArrayList;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size;
        if (doubleArrayList == null || (size = doubleArrayList.size()) == 0) {
            return;
        }

        double[] doubles = getDoubleList(doubleArrayList);
        double dValue = getDValue(doubles[0], doubles[1]);

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        //int actualWidth = width - getPaddingLeft() - getPaddingRight();
        int actualHeight = height - getPaddingTop() - getPaddingBottom();

        float offsetX = width / (size + 1);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#eae1df"));
        paint.setStrokeWidth(1f);

        for (int i = 1; i <= size; i++) {
            canvas.drawLine(offsetX * i, 0, offsetX * i, height, paint);
        }

        int radio = 5;
        int padding = 50;
        //画圆
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            double pointY;
            if (dValue == 0) {
                pointY = actualHeight - padding;
            } else {
                pointY = (actualHeight - 2 * padding) * getDValue(doubles[0], doubleArrayList.get(i - 1)) / dValue + padding;
            }
//            if (pointY < radio) {
//                pointY = 2 * padding;
//            } else if (pointY >= actualHeight - radio) {
//                pointY = pointY - padding;
//            }
            points.add(new Point(offsetX * i, Float.parseFloat(pointY + "")));
            //canvas.drawCircle(offsetX * i, Float.parseFloat(pointY + ""), 5, paint);
        }
        //画线以及画圆
        for (int i = 0; i < points.size(); i++) {
            if (i < size - 1) {
                paint.setColor(Color.RED);
                canvas.drawLine(points.get(i).pointX, points.get(i).pointY, points.get(i + 1).pointX, points.get(i + 1).pointY, paint);
            }
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(offsetX * (i + 1), Float.parseFloat(points.get(i).pointY + ""), radio, paint);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(offsetX * (i + 1), Float.parseFloat(points.get(i).pointY + ""), radio, paint);
        }

    }

    /**
     * @param doubleArrayList
     * @return double[0] max    doule[1]min
     */

    public double[] getDoubleList(ArrayList<Float> doubleArrayList) {
        int size;
        if (doubleArrayList == null || (size = doubleArrayList.size()) == 0) {
            return null;
        }
        double[] doubles = new double[2];
        doubles[0] = doubleArrayList.get(0);
        doubles[1] = doubles[0];
        for (int i = 0; i < size; i++) {
            double value = doubleArrayList.get(i);
            if (value > doubles[0]) // 判断最大值
                doubles[0] = value;
            if (value < doubles[1]) // 判断最小值
                doubles[1] = value;
        }
        return doubles;
    }

    public double getDValue(double b1, double b2) {
        BigDecimal bigDecimal1 = new BigDecimal(b1);
        BigDecimal bigDecimal2 = new BigDecimal(b2);
        return bigDecimal1.subtract(bigDecimal2).doubleValue();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public class Point {
        public float pointX;
        public float pointY;

        public Point(float pointX, float pointY) {
            this.pointX = pointX;
            this.pointY = pointY;
        }

    }
}
