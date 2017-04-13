package com.sulong.elecouple.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.fragment.GuideFragment;
import com.sulong.elecouple.ui.views.RoundLinePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGuideActivity extends BaseActivity {

    @BindView(R.id.guide_pager)
    ViewPager mViewPager;
    @BindView(R.id.circle_indicator)
    RoundLinePageIndicator mPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        GuidePagerAdapter pagerAdapter = new GuidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(pagerAdapter.getCount());

        mPageIndicator.setViewPager(mViewPager);
        mPageIndicator.setCurrentItem(0);
        if (pagerAdapter.getCount() <= 1) {
            mPageIndicator.setVisibility(View.GONE);
        }
    }

    private static class GuidePagerAdapter extends FragmentPagerAdapter {

        public GuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(position == getCount() - 1, position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
