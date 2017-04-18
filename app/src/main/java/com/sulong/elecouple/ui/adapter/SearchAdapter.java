package com.sulong.elecouple.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sulong.elecouple.R;

/**
 * Created by ydh on 2017/4/17.
 */

public class SearchAdapter extends BasePtrLoadMoreListAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_search,null);
        }
        return convertView;
    }
}
