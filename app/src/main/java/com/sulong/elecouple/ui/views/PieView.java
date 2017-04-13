package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.sulong.elecouple.utils.Debug;

/**
 * Created by ydh on 2015/10/9.
 */
public class PieView extends View {

    private float degree = 0;


    public PieView(Context context) {
        super(context);
    }

    public void setDegree(float degree) {
        this.degree = degree;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (degree == 0) {
            return;
        }
        float sweetDegree = 30;
        if (degree >= 70) {
            sweetDegree = 30;
        } else {
            sweetDegree = (int) degree >> 1;
        }
        Paint p = new Paint();
        p.setColor(Color.RED);// 设置灰色
        p.setStyle(Paint.Style.FILL);//设置填满
        p.setAntiAlias(true);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        Debug.i("height=" + height + "--width=" + width);
        int centenX = width >> 1;
        int centenY = height >> 1;
        int smallR = (centenY >> 1);
        int bigR = (centenY >> 1) + 40;
        RectF oval1 = new RectF(centenX - smallR, centenY - smallR, centenX + smallR, centenY + smallR);// 设置个新的长方形，扫描测量
        p.setColor(Color.parseColor("#f3ad53"));
        canvas.drawArc(oval1, -90, degree, true, p);

        // find the 30degree small circle point
        float point1X = (float) (centenX + smallR * Math.cos((90 - sweetDegree) * Math.PI / 180));
        float point1Y = (float) (centenY - smallR * Math.sin((90 - sweetDegree) * Math.PI / 180));

        float point2X = point1X + 45;
        if (sweetDegree < 7) {
            point2X = point1X;
        }
        float point2Y = point1Y - 70;

        float point3X = point2X + 90;
        float point3Y = point2Y;

        float text1X = point2X + 5;
        float text1Y = point2Y - 10;

        float text2X = text1X;
        float text2Y = point2Y + 30;

        canvas.drawLine(point1X, point1Y, point2X, point2Y, p);
        canvas.drawLine(point2X, point2Y, point3X, point3Y, p);
        p.setTextSize(25);
        float percent = degree * 100 / 360;
        canvas.drawText(percent + "%", text1X, text1Y, p);
        canvas.drawText("粉丝贡献", text2X, text2Y, p);


        float point4X = (float) (centenX - bigR * Math.cos(sweetDegree * Math.PI / 180));
        float point4Y = (float) (centenY - bigR * Math.sin(sweetDegree * Math.PI / 180));

        float point5X = point4X - 35;
        float point5Y = point4Y - 35;

        float point6X = point5X - 90;
        float point6Y = point5Y;

        float text3X = point5X - p.measureText("67%") - 10;
        float text3Y = point5Y - 10;

        float text4X = point5X - p.measureText("自营") - 10;
        float text4Y = point5Y + 30;

        p.setColor(Color.parseColor("#fe6723"));
        canvas.drawLine(point4X, point4Y, point5X, point5Y, p);
        canvas.drawLine(point5X, point5Y, point6X, point6Y, p);

        canvas.drawText((100 - percent) + "%", text3X, text3Y, p);
        canvas.drawText("自营", text4X, text4Y, p);
        // canvas.drawPoint((float) (centenX + smallR * Math.cos(60 * Math.PI / 180)), (float) (centenY - smallR * Math.sin(60 * Math.PI / 180)), p);

        RectF oval2 = new RectF(centenX - bigR, centenY - bigR, centenX + bigR, centenY + bigR);
        canvas.drawArc(oval2, -90 + degree, 360 - degree, true, p);
    }

    public void test() {
        degree = 0;
        post(new Runnable() {
            @Override
            public void run() {
                if (degree < 0 || degree >= 180) {
                    return;
                }
                degree++;
                setDegree(degree);
                postDelayed(this, 800);
            }
        });

    }

}
