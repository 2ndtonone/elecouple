package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.sulong.elecouple.utils.Debug;

public class StickyNavLayout extends LinearLayout {

    private View mTopView;
    private View mNavView;
    private ViewPager mViewPager;
    private View mCurInnerScrollView;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity, mMinimumVelocity;
    private int mPagingTouchSlop;
    private int mTouchSlop;
    private int mTopViewHeight;
    private ViewPager.OnPageChangeListener mOnPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            getCurrentScrollView();
        }
    };

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new Scroller(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mMaximumVelocity = config.getScaledMaximumFlingVelocity();
        mMinimumVelocity = config.getScaledMinimumFlingVelocity();
        mPagingTouchSlop = config.getScaledTouchSlop();
        mTouchSlop = (int) (1 * getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = getChildAt(0);
        mNavView = getChildAt(1);
        mViewPager = (ViewPager) getChildAt(2);
        mViewPager.addOnPageChangeListener(mOnPagerChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewPager.removeOnPageChangeListener(mOnPagerChangeListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNavView.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();
        if (actionIndex > 0) {
            return true;
        }
        Debug.i(getLogTag(), "----------------");
        Debug.i(getLogTag(), "getAction():" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                getCurrentScrollView();
                Debug.i(getLogTag(), "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = getDeltaX(ev);
                float dy = getDeltaY(ev);
                float absDx = Math.abs(dx);
                float absDy = Math.abs(dy);
                if (absDx > mPagingTouchSlop && absDx > absDy) {
                    // 不拦截水平方向触摸事件
                    break;
                }
                Debug.i(getLogTag(), "mTouchSlop:" + mTouchSlop);
                if (absDy <= mTouchSlop) {
                    // 没有纵向位移不拦截触摸事件
                    break;
                }
                View scrollView = mCurInnerScrollView;
                boolean isScrollFromUpToDown = dy > 0;
                boolean isScrollFromDownToUp = dy < 0;
                Debug.i(getLogTag(), "dy:" + dy);
                Debug.i(getLogTag(), "scrollView:" + scrollView);
                Debug.i(getLogTag(), "isTopViewHidden:" + isTopViewHidden());
                Debug.i(getLogTag(), "isScrollFromUpToDown:" + isScrollFromUpToDown);
                // 判断是否需要拦截子View事件
                if (scrollView == null
                        || !isTopViewHidden()
                        || (isScrollFromUpToDown && isInAbsoluteStart(scrollView))) {
                    Debug.i(getLogTag(), "拦截子View事件");
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    sendCancelEvent(ev);
                    scrollBy(0, (int) -dy);
                    // 如果回到了内部滚动，就给子View再发送DOWN事件，内部滚动时不会调用这里
                    if (scrollView != null && isTopViewHidden()) {
                        boolean scrollAtTop = isInAbsoluteStart(scrollView);
                        boolean scrollAtEnd = isInAbsoluteEnd(scrollView);
                        Debug.i(getLogTag(), "scrollAtTop:" + scrollAtTop);
                        Debug.i(getLogTag(), "scrollAtEnd:" + scrollAtEnd);
                        if (isScrollFromUpToDown && !scrollAtTop // 下拉但没有拉到顶部
                                || isScrollFromDownToUp && !scrollAtEnd) {// 上拉但没有拉到底部
                            Debug.i(getLogTag(), "回到了内部滚动");
                            sendDownEvent(ev);
                            recycleVelocityTracker();
                            break;
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                recycleVelocityTracker();
                Debug.i(getLogTag(), "ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP: {
                if (mVelocityTracker != null) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocityY = (int) mVelocityTracker.getYVelocity();
                    if (Math.abs(velocityY) > mMinimumVelocity) {
                        fling(-velocityY);
                    }
                    recycleVelocityTracker();
                    Debug.i(getLogTag(), "ACTION_UP, velocityY:" + velocityY + ",mMaximumVelocity:" + mMaximumVelocity);
                }
            }
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void sendCancelEvent(MotionEvent lastEvent) {
        sendEvent(lastEvent, MotionEvent.ACTION_CANCEL);
    }

    private void sendDownEvent(MotionEvent lastEvent) {
        sendEvent(lastEvent, MotionEvent.ACTION_DOWN);
    }

    private void sendEvent(MotionEvent ev, int action) {
        MotionEvent e = MotionEvent.obtain(
                ev.getDownTime(),
                ev.getEventTime(),
                action,
                ev.getX(), ev.getY(),
                ev.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private float getDeltaX(MotionEvent event) {
        if (event.getHistorySize() > 0) {
            return event.getX() - event.getHistoricalX(0);
        } else {
            return 0f;
        }
    }

    private float getDeltaY(MotionEvent event) {
        if (event.getHistorySize() > 0) {
            return event.getY() - event.getHistoricalY(0);
        } else {
            return 0f;
        }
    }

    private boolean isInAbsoluteStart(View view) {
        return !view.canScrollVertically(-1);
    }

    private boolean isInAbsoluteEnd(View view) {
        return !view.canScrollVertically(1);
    }

    private boolean isTopViewHidden() {
        return getScrollY() == mTopViewHeight;
    }

    private void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    private void scrollTo(int y) {
        scrollTo(0, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        // 修正限制偏移量
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(getScrollX(), y);
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Debug.i(getLogTag(), "computeScroll->" +
                    "getCurrY:" + mScroller.getCurrY() + ","
                    + "getStartY:" + mScroller.getStartY() + ","
                    + "getFinalY:" + mScroller.getFinalY() + ","
                    + "getCurrVelocity:" + mScroller.getCurrVelocity() + ","
            );
            scrollTo(mScroller.getCurrY());
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void getCurrentScrollView() {
        mCurInnerScrollView = null;
        int currentItem = mViewPager.getCurrentItem();
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) mViewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        Fragment fragment = adapter.getItem(currentItem);
        if (fragment == null) {
            return;
        }
        View fragmentView = fragment.getView();
        if (fragmentView == null) {
            return;
        }
        View view = fragmentView.findViewWithTag("scroll");
        if (view != null && view.getVisibility() == VISIBLE) {
            mCurInnerScrollView = view;
        }
    }

    private String getLogTag() {
        return getClass().getSimpleName();
    }
}