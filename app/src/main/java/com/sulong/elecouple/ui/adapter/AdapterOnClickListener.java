package com.sulong.elecouple.ui.adapter;

import android.view.View;

/**
 * Created by ydh on 2016/9/19.
 */

public interface AdapterOnClickListener<D> {
    void onClick(View view, int position, D itemData);
}