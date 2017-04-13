package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sulong.elecouple.R;

/**
 * Created by ydh on 2016/7/6.
 */
public class TimeCountDownTextView extends TextView {

    private TimeCount mTimer;
    private OnTimeCountDownListener mOnTimeCountDownListener;
    private boolean isCountDownFinished = true;

    public TimeCountDownTextView(Context context) {
        super(context);
    }

    public TimeCountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeCountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startCount() {
        mTimer = new TimeCount(60000, 1000);
        mTimer.start();
        isCountDownFinished = false;
    }

    public void setOnTimeCountDownListener(OnTimeCountDownListener listener) {
        this.mOnTimeCountDownListener = listener;
    }

    private void stopCount() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        isCountDownFinished = true;
    }

    public boolean isCountDownFinished() {
        return isCountDownFinished;
    }

    public interface OnTimeCountDownListener {
        void onTimeCountDownFinished();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            stopCount();
            if (mOnTimeCountDownListener != null) {
                mOnTimeCountDownListener.onTimeCountDownFinished();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(getResources().getString(R.string.xx_second, millisUntilFinished / 1000));
        }
    }
}