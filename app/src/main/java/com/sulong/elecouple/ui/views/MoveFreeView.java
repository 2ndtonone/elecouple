package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.OnClickReCall;

public class MoveFreeView extends LinearLayout {


    public static boolean isTouchMoveFreeView = false;
    public boolean canMove = false;
    public int L, T;
    private OnClickReCall mClickReCall;

    public MoveFreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLocation(int x, int y, View view) {

        int l = x - (int) ((float) this.getWidth() / 2);
        int t = y - (int) ((float) this.getHeight() / 2);
        int r = x + (int) ((float) this.getWidth() / 2);
        int b = y + (int) ((float) this.getHeight() / 2);

        if (l < view.getLeft()) {
            l = view.getLeft();
            r = this.getWidth();
        }

        if (r > view.getRight()) {
            r = view.getRight();
            l = view.getRight() - this.getWidth();
        }

        if (t < view.getTop()) {
            t = view.getTop();
            b = this.getBottom();
        }

        if (b > view.getBottom()) {
            b = view.getBottom();
            t = view.getBottom() - this.getHeight();
        }

        this.layout(l, t, r, b);
        L = l;
        T = t;

        Debug.i(" L=" + L + ",T=" + T);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            canMove = true;
            setTagId((long) this.getTag());
//            System.out.println("in ACTION_DOWNx="+event.getX()+",y="+event.getY());
            isTouchMoveFreeView = true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
//            System.out.println("in ACTION_UPx="+event.getX()+",y="+event.getY());

        }

        return super.onTouchEvent(event);
    }

    public void setClickCall(OnClickReCall onClickReCall) {
        mClickReCall = onClickReCall;
    }

    public void setTagId(long id) {
        mClickReCall.setTagId(id);
    }


    public boolean autoMouse(MotionEvent event, View view, int realH) {
        boolean rb = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (canMove) {
                    Debug.i("tag_test inside MotionEvent.ACTION_MOVE x=" + event.getX() + ",y=" + event.getY() + ",event.getY()-realH=" + ((int) event.getY() - realH));
                    this.setLocation((int) event.getX(), (int) event.getY() - realH, view);
                    rb = false;
                }

                break;
        }
        return rb;
    }
}
