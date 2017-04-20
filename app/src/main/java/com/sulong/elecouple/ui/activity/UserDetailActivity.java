package com.sulong.elecouple.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.adapter.MainPageAdapter;
import com.sulong.elecouple.ui.fragment.BaseFragment;
import com.sulong.elecouple.ui.fragment.MyCenterFragment;
import com.sulong.elecouple.ui.fragment.SearchFragment;
import com.sulong.elecouple.ui.fragment.UserInfoFragment;
import com.sulong.elecouple.ui.views.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.banner)
    BannerViewPager banner;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_uid)
    TextView tvUid;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.pager_tab_strip)
    PagerSlidingTabStrip pagerTabStrip;
    @BindView(R.id.pager)
    ViewPager pager;

    private Unbinder unbinder;
    private MainPageAdapter adapter;
    private ArrayList<Fragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        initView();
        initViewpager();
    }

    private void initViewpager() {
        fragments = new ArrayList<>();

        BaseFragment fragment1 = new UserInfoFragment();
        fragments.add(fragment1);

        BaseFragment fragment2 = new SearchFragment();
        fragments.add(fragment2);

        // tab标题
        List<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.person_data));
        titleList.add(getString(R.string.mate_choice));
        String[] titles = new String[titleList.size()];
        titleList.toArray(titles);

        // ViewPager设置
        adapter = new MainPageAdapter(getSupportFragmentManager(), fragments, titles);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size() - 1);// 缓存页面,显示第一个缓存最后一个

        pagerTabStrip.setViewPager(pager);
        pagerTabStrip.setTypeface(null, Typeface.NORMAL);
    }

    private void initView() {

        List<String> dataList = new ArrayList<>();
        dataList.add("http://wx1.sinaimg.cn/mw690/630c007egy1fbxgopsqmpj20as0f4act.jpg");
        dataList.add("http://wx1.sinaimg.cn/mw690/630c007egy1fc0hku22i6j20as0f4acj.jpg");
        dataList.add("http://wx1.sinaimg.cn/mw690/630c007egy1fcdccpeh7jj20as0f40uz.jpg");
        dataList.add("http://wx1.sinaimg.cn/mw690/630c007egy1fchtdplsk9j20as0f4q6g.jpg");
        banner.setData(dataList);


    }

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                break;
        }
    }
}
