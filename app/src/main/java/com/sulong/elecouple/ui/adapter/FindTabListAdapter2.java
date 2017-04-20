package com.sulong.elecouple.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.views.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ydh on 2017/4/19.
 */

public class FindTabListAdapter2 extends BasePtrLoadMoreListAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_find_list2, null);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_engagements_state)
        TextView tvEngagementsState;
        @BindView(R.id.tv_engagements_time)
        TextView tvEngagementsTime;
        @BindView(R.id.tv_engagements_place)
        TextView tvEngagementsPlace;
        @BindView(R.id.tv_engagements_theme)
        TextView tvEngagementsTheme;
        @BindView(R.id.tv_engagements_pay)
        TextView tvEngagementsPay;
        @BindView(R.id.tv_engagements_type)
        TextView tvEngagementsType;
        @BindView(R.id.iv_user_head1)
        CircleImageView ivUserHead1;
        @BindView(R.id.iv_user_head2)
        CircleImageView ivUserHead2;
        @BindView(R.id.iv_star)
        ImageView ivStar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.btn_check_engagements)
        Button btnCheckEngagements;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
