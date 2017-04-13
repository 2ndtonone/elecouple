package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wujian on 2016/9/26.
 * 倒计时 00:00:00
 */

public class TimeDownTextView extends TextView {

    Subscription mSubscription;
    long act_end_time;

    public TimeDownTextView(Context context) {
        super(context);
    }

    public TimeDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * @param act_end_time 倒计时结束时间戳13位
     */
    public void setEndTime(long act_end_time) {
        this.act_end_time = act_end_time;
        startShowTime();
    }


    public void startShowTime() {
        stopSubscripte();

        if (act_end_time < 1000) {
            setText("00:00:00");
            return;
        }

        mSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long long1) {

                        long leave_time_dist = act_end_time - System.currentTimeMillis();

                        if (leave_time_dist < 1000) {
                            setText("00:00:00");
                        } else {
                            long hour = leave_time_dist / (60 * 60 * 1000);
                            long min = (leave_time_dist - (hour * 60 * 60 * 1000)) / (60 * 1000);
                            long second = ((leave_time_dist - (hour * 60 * 60 * 1000) - (min * 60 * 1000))) / (1000);

                            StringBuilder leaveTimeStr = new StringBuilder();
                            if (hour < 10) {
                                leaveTimeStr.append("0");
                            }
                            leaveTimeStr.append(hour + ":");
                            if (min < 10) {
                                leaveTimeStr.append("0");
                            }
                            leaveTimeStr.append(min + ":");
                            if (second < 10) {
                                leaveTimeStr.append("0");
                            }
                            leaveTimeStr.append(second);
                            setText(leaveTimeStr.toString());
                        }
                    }
                });
    }


    public void stopSubscripte() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }


}
