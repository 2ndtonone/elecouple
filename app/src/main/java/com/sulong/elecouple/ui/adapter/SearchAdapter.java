package com.sulong.elecouple.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.activity.UserDetailActivity;
import com.sulong.elecouple.ui.views.DynamicHeightImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ydh on 2017/4/17.
 */

public class SearchAdapter extends BasePtrLoadMoreListAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_search, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserDetail(context);

            }
        });
        return convertView;
    }

    private void goToUserDetail(Context context) {
        context.startActivity(new Intent(context, UserDetailActivity.class));
    }

    static class ViewHolder {
        @BindView(R.id.rl_content)
        RelativeLayout rl_content;
        @BindView(R.id.iv_head)
        DynamicHeightImageView ivHead;
        @BindView(R.id.tv_account_type)
        TextView tvAccountType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_expect)
        TextView tvExpect;
        @BindView(R.id.tv_age)
        TextView tvAge;
        @BindView(R.id.tv_height)
        TextView tvHeight;
        @BindView(R.id.tv_degree)
        TextView tvDegree;
        @BindView(R.id.tv_income)
        TextView tvIncome;
        @BindView(R.id.ll_tag)
        LinearLayout llTag;
        @BindView(R.id.tv_desc)
        TextView tvDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
