package com.sulong.elecouple.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.sulong.elecouple.R;

/**
 * Created by ydh on 2015/11/10.
 */
public class ClickEffectImageView extends ImageView {

    int mPressedColor = 0x60000000;
    boolean mShowClickEffect = false;
    ValueAnimator mMaskFadeOutAnim;
    int mTouchSlop;

    public ClickEffectImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ClickEffectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClickEffectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClickEffectImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClickEffectImageView);
            mShowClickEffect = a.getBoolean(R.styleable.ClickEffectImageView_ceiv_show_click_effect, mShowClickEffect);
            mPressedColor = a.getColor(R.styleable.ClickEffectImageView_ceiv_pressed_color, mPressedColor);
            a.recycle();
        }
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isClickable() && mShowClickEffect) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    addGrayMaskToMakeClickedEffect();
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    restoreColorMask();
                }
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    void addGrayMaskToMakeClickedEffect() {
        setColorFilter(mPressedColor, PorterDuff.Mode.SRC_ATOP);
        invalidate();
    }

    void restoreColorMask() {
        ValueAnimator anim = mMaskFadeOutAnim;
        if (anim != null) {
            anim.cancel();
        }
        anim = new ValueAnimator();
        anim.setIntValues(mPressedColor, Color.TRANSPARENT);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setDuration(getResources().getInteger(R.integer.exit_fade_duration));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setColorFilter((Integer) animation.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP);
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clearColorFilter();
                invalidate();
                mMaskFadeOutAnim = null;
            }
        });
        anim.start();
        mMaskFadeOutAnim = anim;
    }
}