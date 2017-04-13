package com.sulong.elecouple.utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sulong.elecouple.R;

public class GuidePageUtils {
    public static final boolean isCycle = false;

    public static View getPageView(Context context, int imgResId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View pageView = inflater.inflate(R.layout.page_item, null);
        ImageView imgPage = (ImageView) pageView.findViewById(R.id.imgPage);
        imgPage.setBackgroundResource(imgResId);
        return pageView;
    }

}
