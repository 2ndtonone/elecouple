package com.sulong.elecouple.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sulong.elecouple.R;
import com.sulong.elecouple.utils.Utility;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Liu Qing on 2015/9/24.
 */
public class AggPtrHeaderView extends LinearLayout implements PtrUIHandler {

    private FrameLayout mMascotContainer;
    private WebView mGifView;
    private ImageView mStaticMascotView;
    private ImageView mCloudView;
    private Drawable mCloudDrawable;
    private ObjectAnimator mCloudAnim;
    private boolean mIsRefreshing = false;

    public AggPtrHeaderView(Context context) {
        super(context);
        init();
    }

    public AggPtrHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mGifView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        double cloudHeightRatio = 1.0 * mCloudDrawable.getIntrinsicHeight() / mCloudDrawable.getIntrinsicWidth();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * cloudHeightRatio);
        mCloudView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void init() {
        inflate(getContext(), R.layout.ah_ptr_default_header_view, this);
        findViews();
        addGifView();
        post(new Runnable() {
            @Override
            public void run() {
                mStaticMascotView.setPivotX(mStaticMascotView.getWidth() / 2);
                mStaticMascotView.setPivotY(mStaticMascotView.getHeight());
                mCloudView.setTranslationY(-mCloudView.getHeight() + getHeight());
            }
        });
    }

    private void findViews() {
        mMascotContainer = (FrameLayout) findViewById(R.id.agg_ptr_header_mascot_container);
        mStaticMascotView = (ImageView) findViewById(R.id.agg_ptr_header_mascot_static);
        mCloudView = (ImageView) findViewById(R.id.agg_ptr_header_cloud);
        mCloudDrawable = getResources().getDrawable(R.drawable.agg_ptr_header_cloud_bg);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mGifView.setVisibility(INVISIBLE);
        mStaticMascotView.setVisibility(VISIBLE);
        updateStaticMascotScale(0);
        if (mCloudAnim != null && mCloudAnim.isStarted()) {
            mCloudAnim.cancel();
            mCloudAnim = null;
        }
        mCloudView.setTranslationY(-mCloudView.getHeight() + getHeight());
        mGifView.loadData(null, null, null);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        loadGif();
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mIsRefreshing = true;
        mGifView.setVisibility(VISIBLE);
        mStaticMascotView.setVisibility(INVISIBLE);
        mCloudAnim = ObjectAnimator.ofFloat(mCloudView, View.TRANSLATION_Y, mCloudView.getTranslationY(), getHeight());
        mCloudAnim.setInterpolator(new LinearInterpolator());
        mCloudAnim.setDuration((long) (1.0 * 8000 * (mCloudView.getHeight() - getHeight()) / mCloudView.getHeight()));
        mCloudAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mIsRefreshing) {
                    mCloudAnim = ObjectAnimator.ofFloat(mCloudView, View.TRANSLATION_Y, -mCloudView.getHeight(), getHeight());
                    mCloudAnim.setRepeatMode(ValueAnimator.RESTART);
                    mCloudAnim.setRepeatCount(ValueAnimator.INFINITE);
                    mCloudAnim.setInterpolator(new LinearInterpolator());
                    mCloudAnim.setDuration(8000);
                    mCloudAnim.start();
                }
            }
        });
        mCloudAnim.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mIsRefreshing = false;
        mGifView.setVisibility(INVISIBLE);
        mStaticMascotView.setVisibility(VISIBLE);
        if (mCloudAnim != null && mCloudAnim.isStarted()) {
            mCloudAnim.cancel();
            mCloudAnim = null;
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        int mOffsetToRefresh = frame.getOffsetToRefresh();
        int currentPos = ptrIndicator.getCurrentPosY();
        if (currentPos <= mOffsetToRefresh) {
            float scaleRatio = (float) (1.0 * currentPos / mOffsetToRefresh);
            updateStaticMascotScale(scaleRatio);
        }
    }

    private void updateStaticMascotScale(float scale) {
        mStaticMascotView.setScaleX(scale);
        mStaticMascotView.setScaleY(scale);
    }

    private void addGifView() {
        mGifView = new WebView(getContext().getApplicationContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mGifView.setScrollBarSize(0);
        }
        mGifView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mGifView.setBackgroundColor(Color.TRANSPARENT);
        mGifView.setVerticalScrollBarEnabled(false);
        mGifView.setHorizontalScrollBarEnabled(false);
        // disable scroll on touch
        mGifView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        int gifWidth = Utility.dip2px(getContext(), 54);
        int gifHeight = Utility.dip2px(getContext(), 54);
        LayoutParams gifLp = new LayoutParams(gifWidth, gifHeight);
        mMascotContainer.addView(mGifView, 0, gifLp);
    }

    private void removeGifView() {
        if (mGifView == null) {
            return;
        }
        mMascotContainer.removeView(mGifView);
        mGifView.removeAllViews();
        mGifView.destroy();
        mGifView = null;
    }

    private void loadGif() {
        if (mGifView != null) {
            String data = "<html><body><head><style>*{ margin:0; padding: 0;}</style></head><img src=\""
                    + "mascot_pull_to_refresh_indicator.gif" +
                    "\" width=\"100%\" loop=\"false\"/></body></html>";
            mGifView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
        }
    }


}
