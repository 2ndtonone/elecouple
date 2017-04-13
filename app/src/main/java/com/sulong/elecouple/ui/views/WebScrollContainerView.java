package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.sulong.elecouple.BuildConfig;

/**
 * ScrollView和WebView共存的容器，可翻页滑动
 * Created by ydh on 2015/11/26.
 */
public class WebScrollContainerView extends ViewGroup {

    private static final int RELEASE_BOUNCE_DURATION = 600;

    private boolean isDebug = BuildConfig.PRINT_LOG;

    private WebView mWebView;
    private ScrollView mScrollView;
    private View mBottomView;

    private Scroller mScroller;

    private boolean isScrollFromUpToDown = false;
    private boolean isScrollFromDownToUp = false;
    private boolean mHasMoved = false;
    private float mLastY;

    private MotionEvent mDownEvent;
    private MotionEvent mLastMoveEvent;

    public WebScrollContainerView(Context context) {
        super(context);
        init();
    }

    public WebScrollContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 3) {
            mScrollView = (ScrollView) getChildAt(0);
            mWebView = (WebView) getChildAt(1);
            mBottomView = getChildAt(2);
        } else {
            throw new IllegalStateException("There must be 3 child");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        log("widthSize:" + widthSize + ",heightSize:" + heightSize);

        // 测量ScrollView
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        mScrollView.measure(widthSpec, heightSpec);

        View scrollContentView = mScrollView.getChildAt(0);
        int scrollContentHeight = scrollContentView.getMeasuredHeight();
        if (scrollContentHeight < heightSize) {
            int scrollViewWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            int scrollViewHeightSpec = MeasureSpec.makeMeasureSpec(scrollContentHeight, MeasureSpec.EXACTLY);
            mScrollView.measure(scrollViewWidthSpec, scrollViewHeightSpec);
        }

        mWebView.measure(widthSpec, heightSpec);

        mBottomView.measure(widthSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int top = childView.getTop();
            if (i > 0) {
                top = getChildAt(i - 1).getBottom();
            }
            childView.layout(l, top, l + childView.getMeasuredWidth(), top + childView.getMeasuredHeight());
            log("child[" + i + "] width:" + childView.getWidth() + " , height:" + childView.getHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        log("dispatchTouchEvent()----");

        final int action = ev.getActionMasked();
        final int actionIndex = ev.getActionIndex();

        if (actionIndex > 0) {
            return true;
        }
        View firstView = getChildAt(0);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                log("ACTION_DOWN");
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                mDownEvent = ev;
                mLastY = ev.getY();
                mHasMoved = false;
                isScrollFromUpToDown = false;
                isScrollFromDownToUp = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                log("ACTION_MOVE");
                mLastMoveEvent = ev;
                float yDiff = ev.getY() - mLastY;
                mLastY = ev.getY();
                if (yDiff == 0) {
                    break;
                }
                mHasMoved = true;
                isScrollFromUpToDown = yDiff > 0;
                isScrollFromDownToUp = !isScrollFromUpToDown;

                // 只有以下情况可以触发翻页
                boolean canTriggerPaging = isSwitchingPage()
                        || isInAbsoluteEnd(getChildAt(0)) && getChildAt(0).getTop() == 0 && isScrollFromDownToUp
                        || isInAbsoluteStart(getChildAt(1)) && getChildAt(1).getTop() == 0 && isScrollFromUpToDown
                        || isInAbsoluteEnd(getChildAt(1)) && getChildAt(1).getTop() == 0 && isScrollFromDownToUp
                        || isInAbsoluteStart(getChildAt(2)) && getChildAt(2).getTop() == 0 && isScrollFromUpToDown;
                if (canTriggerPaging) {
                    log("canTriggerPaging");

                    // 取消子View的事件
                    sendCancelEvent();

                    // 移动整个区域
                    offsetContent(yDiff);

                    // 如果回到了内部滚动，就给子View再发送DOWN事件
                    for (int i = 0; i < getChildCount() - 1; i++) {
                        View view = getChildAt(i);
                        int top = view.getTop();
                        if (top == 0 && (
                                (!isInAbsoluteStart(view) && isScrollFromUpToDown)
                                        || (!isInAbsoluteEnd(view) && isScrollFromDownToUp))) {
                            sendDownEvent();
                            break;
                        }
                    }
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                log("ACTION_UP");
                boolean intercept = false;
                boolean isSwitchingBetweenFirstSecondView = getChildAt(0).getTop() > getFirstViewTopAfterSwitchPage(1);
                if (mHasMoved && isSwitchingPage()) {
                    intercept = true;
                    sendCancelEvent();

                    int startY = firstView.getTop();
                    int endY = startY;

                    if (isScrollFromDownToUp) {
                        if (isSwitchingBetweenFirstSecondView) {
                            endY = getFirstViewTopAfterSwitchPage(1);
                        } else {
                            endY = getFirstViewTopAfterSwitchPage(2);
                        }
                    } else if (isScrollFromUpToDown) {
                        if (isSwitchingBetweenFirstSecondView) {
                            endY = 0;
                        } else {
                            endY = getFirstViewTopAfterSwitchPage(1);
                        }
                    }

                    mScroller.startScroll(
                            0, startY,
                            0, endY - startY,
                            RELEASE_BOUNCE_DURATION
                    );
                    invalidate();
                }
                mHasMoved = false;
                isScrollFromUpToDown = false;
                isScrollFromDownToUp = false;
                if (intercept) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
                log("ACTION_CANCEL");
                mHasMoved = false;
                isScrollFromUpToDown = false;
                isScrollFromDownToUp = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            View firstView = getChildAt(0);
            int offset = mScroller.getCurrY() - firstView.getTop();
            offsetContent(offset);
        }
    }

    public boolean isInAbsoluteStart(View view) {
        return !view.canScrollVertically(-1);
    }

    public boolean isInAbsoluteEnd(View view) {
        return !view.canScrollVertically(1);
    }

    private boolean isSwitchingPage() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getTop() == 0) {
                return false;
            }
        }
        return true;
    }

    private void sendCancelEvent() {
        sendEvent(mDownEvent, MotionEvent.ACTION_CANCEL);
    }

    private void sendDownEvent() {
        sendEvent(mLastMoveEvent, MotionEvent.ACTION_DOWN);
    }

    private void sendEvent(final MotionEvent ev, int action) {
        MotionEvent e = MotionEvent.obtain(
                ev.getDownTime(),
                ev.getEventTime(),
                action,
                ev.getX(), ev.getY(),
                ev.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private void offsetContent(float offset) {
        // 限制滑动范围
        View firstView = getChildAt(0);
        int oldTop = firstView.getTop();
        int newTop = (int) (oldTop + offset);
        int minTopY = getFirstViewTopAfterSwitchPage(getChildCount() - 1);
        int maxTopY = 0;
        if (newTop < minTopY) {  // 下限
            newTop = minTopY;
        } else if (newTop > maxTopY) { // 上限
            newTop = maxTopY;
        }

        for (int i = 1; i < getChildCount(); i++) {
            int criticalTop = getFirstViewTopAfterSwitchPage(i);
            // 如果刚越过边界
            if (oldTop > criticalTop && newTop < criticalTop // 往上滑动
                    || oldTop < criticalTop && newTop > criticalTop) // 往下滑动
            {
                // 用边界值来赋值一次，以方便判断是否进入子View内部滑动
                newTop = criticalTop;
                break;
            }
        }

        // 偏移量
        int fixedOffset = newTop - firstView.getTop();
        // 移动整个内容
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).offsetTopAndBottom(fixedOffset);
        }
        invalidate();
        log("offsetContent():" + fixedOffset);
    }

    private int getFirstViewTopAfterSwitchPage(int switchedPageCount) {
        int top = 0;
        for (int i = 0; i < switchedPageCount; i++) {
            top += -getChildAt(i).getHeight();
        }
        return top;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -1);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams lp) {
        return new MarginLayoutParams(lp.width, lp.height);
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    private void log(String msg) {
        if (isDebug) {
            String tag = getLogTag();
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            for (int i = 2; i < trace.length; i++) {
                StackTraceElement element = trace[i];
                if (element.getClassName().equals(getClass().getName())
                        && !element.getMethodName().equals("log")) {
                    String simpleClassName = element.getClassName().substring(element.getClassName().lastIndexOf(".") + 1);
                    tag = simpleClassName + "-" + element.getMethodName();
                    break;
                }
            }
            Log.d(tag, msg);
        }
    }
}
