package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sulong.elecouple.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerViewPager<T> extends RelativeLayout {

    private static final int SWITCH_PERIOD = 4000;
    private double mHeightRatio = 0f;
    private CirclePageIndicator mPagerIndicator;
    private ViewPager mViewPager;
    private List<T> mDataList = new ArrayList<>();
    private SubjectPagerAdapter mPagerAdapter;
    private Callback mCallback = null;
    private Timer mTimer = null;
    private PageSwitchTask mAdSwitchTask = null;
    private OnPageChangeListener mPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    startSwitchPager();
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                case ViewPager.SCROLL_STATE_DRAGGING:
                    stopSwitchPager();
                    break;
            }
        }
    };

    public BannerViewPager(Context context) {
        super(context);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startSwitchPager();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopSwitchPager();
    }

    public List<T> getData() {
        return mDataList;
    }

    public void setData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            mDataList.clear();
            mDataList.addAll(data);
        }
        mPagerAdapter.notifyDataSetChanged();
        updateUi();
    }

    public void setCallback(Callback<?> callback) {
        mCallback = callback;
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.banner_viewpager, this);
        findViews();
        initViews();
        updateUi();
    }

    private void findViews() {
        mViewPager = (ViewPager) findViewById(R.id.banner_viewpager_pager);
        mPagerIndicator = (CirclePageIndicator) findViewById(R.id.banner_pager_indicator);
    }

    private void initViews() {
        mPagerAdapter = new SubjectPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mPagerIndicator.setSnap(true);
        mPagerIndicator.setViewPager(mViewPager);
        mPagerIndicator.setOnPageChangeListener(mPagerChangeListener);
    }

    private void updateUi() {
        if (mPagerAdapter.getCount() > 1) {
            mPagerIndicator.setVisibility(View.VISIBLE);
            stopSwitchPager();
            startSwitchPager();
        } else {
            mPagerIndicator.setVisibility(View.GONE);
        }
        mPagerIndicator.notifyDataSetChanged();
    }

    private void startSwitchPager() {
        if (mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 1) {
            if (mAdSwitchTask == null || mTimer == null) {
                mAdSwitchTask = new PageSwitchTask();
                mTimer = new Timer();
                mTimer.schedule(mAdSwitchTask, SWITCH_PERIOD, SWITCH_PERIOD);
            }
        }
    }

    private void stopSwitchPager() {
        if (mAdSwitchTask != null) {
            mAdSwitchTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        mTimer = null;
        mAdSwitchTask = null;
    }

    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public interface Callback<T> {
        void onBannerClicked(Context context, int position, T item);

        void onBindView(DynamicHeightImageView imageView, int position, T item);
    }

    private class PageSwitchTask extends TimerTask {
        @Override
        public void run() {
            post(new Runnable() {
                @Override
                public void run() {
                    if (mViewPager.getAdapter() != null) {
                        int count = mViewPager.getAdapter().getCount();
                        int newItem = (mViewPager.getCurrentItem() + 1) % count;
                        mViewPager.setCurrentItem(newItem);
                    }
                }
            });
        }
    }

    private class SubjectPagerAdapter extends PagerAdapter implements OnClickListener {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = container.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_banner, container, false);
            final DynamicHeightImageView imageView = (DynamicHeightImageView) itemView.findViewById(R.id.image);
            imageView.setOnClickListener(this);
            imageView.setTag(R.id.position, position);
            if (mCallback != null) {
                mCallback.onBindView(imageView, position, mDataList.get(position));
            }
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.position);
            if (mCallback != null) {
                mCallback.onBannerClicked(v.getContext(), position, mDataList.get(position));
            }
        }
    }
}
