package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avatarqing.lib.loadmore.LoadMoreContainer;
import com.avatarqing.lib.loadmore.LoadMoreUIHandler;
import com.sulong.elecouple.R;

/**
 * Created by Liu Qing on 2015/9/24.
 */
public class AggLoaderMoreFooterView extends RelativeLayout implements LoadMoreUIHandler {

    private View mProgressBar;
    private TextView mTextView;
    private ViewGroup mContainer;

    private String mDefaultErrorHint;
    private String mNoDataHint;
    private String mLoadFinishHint;
    private String mClickToLoadMoreHint;
    private String mLoadingHint;

    private int mHeight = 0;

    public AggLoaderMoreFooterView(Context context) {
        super(context);
        initView();
    }

    public AggLoaderMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        Resources r = getResources();
        mDefaultErrorHint = r.getString(R.string.cube_views_load_more_error);
        mNoDataHint = r.getString(R.string.no_data);
        mLoadFinishHint = r.getString(R.string.load_more_load_finish);
        mClickToLoadMoreHint = r.getString(R.string.cube_views_load_more_click_to_load_more);
        mLoadingHint = r.getString(R.string.data_loading);
        mHeight = r.getDimensionPixelSize(R.dimen.agg_load_more_view_height);

        mContainer = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.agg_loader_more_foot_view, null);
        LayoutParams containerLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
        addView(mContainer, containerLp);
        mProgressBar = findViewById(R.id.loading_progress);
        mTextView = (TextView) findViewById(R.id.loading_text);
        mContainer.setVisibility(INVISIBLE);
    }

    public void setNoDataHint(String hint) {
        mNoDataHint = hint;
    }

    public void setLoadFinishHint(String hint) {
        mLoadFinishHint = hint;
    }

    public void setClickToLoadMoreHint(String hint) {
        mClickToLoadMoreHint = hint;
    }

    public void setDefaultErrorHint(String hint) {
        mDefaultErrorHint = hint;
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        showContainer();
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText(mLoadingHint);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
            hideContainer();
        } else {
            mContainer.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        showContainer();
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        mTextView.setText(mClickToLoadMoreHint);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        showContainer();
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        String msg = errorMessage;
        if (TextUtils.isEmpty(errorMessage)) {
            msg = mDefaultErrorHint;
        }
        mTextView.setText(msg);
    }

    private void showContainer() {
        mContainer.setVisibility(VISIBLE);
        mContainer.getLayoutParams().height = mHeight;
        mContainer.requestLayout();
    }

    private void hideContainer() {
        mContainer.setVisibility(INVISIBLE);
        mContainer.getLayoutParams().height = 0;
        mContainer.requestLayout();
    }
}
