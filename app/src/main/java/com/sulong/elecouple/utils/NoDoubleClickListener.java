package com.sulong.elecouple.utils;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ydh on 2015/9/9.  防止多次点击
 */
public abstract class NoDoubleClickListener implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public abstract void onNoDoubleClick(Object... object);

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        long currentTime = java.util.Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(adapterView, view, position, id);
        }
    }

    @Override
    public void onClick(View v) {
        long currentTime = java.util.Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
