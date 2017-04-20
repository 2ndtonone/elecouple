package com.sulong.elecouple.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.views.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ydh on 2017/4/19.
 */

public class FindTabListAdapter extends BasePtrLoadMoreListAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_find_list, null);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_user_head)
        CircleImageView ivUserHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
