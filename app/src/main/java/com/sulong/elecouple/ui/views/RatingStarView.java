package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sulong.elecouple.R;

public class RatingStarView extends LinearLayout {

    private static final int STAR_COUNT = 5;
    private static final float MAX_SCORE = 5;
    private int[] mStarImageIds = new int[]{
            R.drawable.star_grey,
            R.drawable.star1,
            R.drawable.star2,
            R.drawable.star3,
            R.drawable.star4,
            R.drawable.star5,
            R.drawable.star6,
            R.drawable.star7,
            R.drawable.star8,
            R.drawable.star9,
            R.drawable.star_full,
    };
    private int[] mRedStarImageIds = new int[]{
            R.drawable.star_full_gray,
            R.drawable.star_half_red,
            R.drawable.star_half_red,
            R.drawable.star_half_red,
            R.drawable.star_half_red,
            R.drawable.star_half_red,
            R.drawable.star_full_red,
            R.drawable.star_full_red,
            R.drawable.star_full_red,
            R.drawable.star_full_red,
            R.drawable.star_full_red,
    };
    private ImageView[] mStarViews;
    private float mScore = 0;
    private float mMaxScore = MAX_SCORE;
    private int starSpacing;
    private int starWidth;
    private int starHeight;
    private StarColor mStarColor;

    public RatingStarView(Context context) {
        this(context, null);
    }

    public RatingStarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        initAttr(attrs);
        initStarViews();
        setRatingScore(mScore);
        updateUi();
    }

    private void initAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RatingStarView);
        mMaxScore = typedArray.getFloat(R.styleable.RatingStarView_rsv_max_score, MAX_SCORE);
        mScore = typedArray.getFloat(R.styleable.RatingStarView_rsv_score, mScore);
        starSpacing = typedArray.getDimensionPixelOffset(R.styleable.RatingStarView_rsv_star_spacing, 2);
        starWidth = typedArray.getDimensionPixelOffset(R.styleable.RatingStarView_rsv_star_width, 20);
        starHeight = typedArray.getDimensionPixelOffset(R.styleable.RatingStarView_rsv_star_height, 20);
        int starColorIndex = typedArray.getInt(R.styleable.RatingStarView_rsv_star_color, StarColor.YELLOW.ordinal());
        mStarColor = StarColor.values()[starColorIndex];
        typedArray.recycle();
    }

    private void initStarViews() {
        int count = STAR_COUNT;
        mStarViews = new ImageView[count];
        for (int i = 0; i < count; i++) {
            ImageView star = new ImageView(getContext());
            LayoutParams lp = new LayoutParams(starWidth, starHeight);
            lp.gravity = Gravity.CENTER_VERTICAL;
            if (i > 0) {
                lp.leftMargin = starSpacing;
            }
            star.setLayoutParams(lp);
            addView(star);
            mStarViews[i] = star;
        }
    }

    private void updateUi() {
        int solidCount = 0;
        int incompleteIndex = 0;
        if (mScore > 0) {
            solidCount = (int) mScore;
            incompleteIndex = Math.round((mScore - solidCount) * 100) / 10;
        }
        for (int i = 0; i < STAR_COUNT; i++) {
            if (i < solidCount) {
                mStarViews[i].setImageResource(getFullFilledStar());
            } else if (i == solidCount) {
                mStarViews[i].setImageResource(getIncompleteStar(incompleteIndex));
            } else {
                mStarViews[i].setImageResource(getFullUnfilledStar());
            }
        }
    }

    int getIncompleteStar(int incompleteIndex) {
        switch (mStarColor) {
            case RED:
                return mRedStarImageIds[incompleteIndex];
            case YELLOW:
            default:
                return mStarImageIds[incompleteIndex];
        }
    }

    int getFullUnfilledStar() {
        switch (mStarColor) {
            case RED:
                return R.drawable.star_full_gray;
            case YELLOW:
            default:
                return R.drawable.star_grey;
        }
    }

    int getFullFilledStar() {
        switch (mStarColor) {
            case RED:
                return R.drawable.star_full_red;
            case YELLOW:
            default:
                return R.drawable.star_full;
        }
    }

    public void setRatingScore(float score) {
        if (score < 0) {
            mScore = 0;
        } else if (score > mMaxScore) {
            mScore = mMaxScore;
        } else {
            mScore = score;
        }
        updateUi();
    }

    enum StarColor {
        YELLOW,
        RED
    }

}
