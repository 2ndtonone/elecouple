package com.sulong.elecouple.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTitleList = new ArrayList<>();

    public MainPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragmentList = list;
    }

    public MainPageAdapter(FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);
        this.mFragmentList = list;
        for (int i = 0; i < titles.length; i++) {
            mTitleList.add(titles[i]);
        }
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragmentList == null || mFragmentList.isEmpty()
                || position < 0 || position >= mFragmentList.size()) {
            return null;
        }
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null && position >= 0 && position < mTitleList.size()) {
            return mTitleList.get(position);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void removeItem(int position) {
        mFragmentList.remove(position);
        mTitleList.remove(position);
        notifyDataSetChanged();
    }
}