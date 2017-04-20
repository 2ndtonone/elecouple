package com.sulong.elecouple.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.adapter.MainPageAdapter;
import com.sulong.elecouple.ui.views.DynamicHeightImageView;
import com.sulong.elecouple.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ydh on 2017/4/18.
 */

public class FindFragment extends BaseFragment {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_find_head)
    DynamicHeightImageView ivFindHead;
    @BindView(R.id.tv_likes_me)
    TextView tvLikesMe;
    @BindView(R.id.tv_apply_chat)
    TextView tvApplyChat;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_engagements)
    TextView tvEngagements;
    @BindView(R.id.tv_list_type)
    TextView tv_list_type;
    @BindView(R.id.pager)
    ViewPager pager;

    private LinearLayout rootView;
    private Unbinder unbinder;
    private MainPageAdapter adapter;
    private ArrayList<Fragment> fragments;
    private String[] listType = new String[]{
            ConstantUtils.FIND_LIST_TYPE_1,
            ConstantUtils.FIND_LIST_TYPE_2,
            ConstantUtils.FIND_LIST_TYPE_3,
            ConstantUtils.FIND_LIST_TYPE_4
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = (LinearLayout) inflater.inflate(R.layout.fragment_find, container, false);
//            readGpsData();
        }
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initViewpager();
        return rootView;
    }

    private void initView() {

    }

    private void initViewpager() {
        fragments = new ArrayList<>();

        BaseFragment fragment1 = new FindTabListFragment();
        fragments.add(fragment1);

        BaseFragment fragment2 = new FindTabListFragment();
        fragments.add(fragment2);
        BaseFragment fragment3 = new FindTabListFragment();
        fragments.add(fragment3);
        BaseFragment fragment4 = new FindTabListFragment2();
        fragments.add(fragment4);

        // tab标题
        List<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.who_likes_me));
        titleList.add(getString(R.string.apply_chat));
        titleList.add(getString(R.string.chat));
        titleList.add(getString(R.string.engagements));
        String[] titles = new String[titleList.size()];
        titleList.toArray(titles);

        // ViewPager设置
        adapter = new MainPageAdapter(getChildFragmentManager(), fragments, titles);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size() - 1);// 缓存页面,显示第一个缓存最后一个

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_likes_me, R.id.tv_apply_chat, R.id.tv_chat, R.id.tv_engagements})
    public void onViewClicked(View view) {
        resetTabStyle();
        switch (view.getId()) {
            case R.id.tv_likes_me:
                tvLikesMe.setSelected(true);
                switchPage(0);
                break;
            case R.id.tv_apply_chat:
                tvApplyChat.setSelected(true);
                switchPage(1);
                break;
            case R.id.tv_chat:
                tvChat.setSelected(true);
                switchPage(2);
                break;
            case R.id.tv_engagements:
                tvEngagements.setSelected(true);
                switchPage(3);
                break;
        }
    }

    private void switchPage(int i) {

        pager.setCurrentItem(i);
        tv_list_type.setText(listType[i]);
    }

    private void resetTabStyle() {
        tvLikesMe.setSelected(false);
        tvApplyChat.setSelected(false);
        tvChat.setSelected(false);
        tvEngagements.setSelected(false);
    }
}
