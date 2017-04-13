package com.sulong.elecouple.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.Utility;

/**
 * Created by Administrator on 2015/6/17.
 */
public class LoadingProgressDialog extends ProgressDialog {

    private String mLoadingTip;
    private TextView mLoadingTv;
    private WebView gifView;
    private LinearLayout mLoadingContainer;

    public LoadingProgressDialog(Context context) {
        this(context, context.getResources().getString(R.string.data_loading), 0);
    }

    public LoadingProgressDialog(Context context, String content, int id) {
        super(context);
        this.mLoadingTip = content;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        Debug.i(getLogTag(), "");
    }

    @Override
    public void onStart() {
        super.onStart();
        addGifView();
        loadGif();
        Debug.i(getLogTag(), "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeGifView();
        Debug.i(getLogTag(), "");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Debug.i(getLogTag(), "");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Debug.i(getLogTag(), "");
    }

    private void initData() {
        mLoadingTv.setText(mLoadingTip);
    }

    public void setContent(String loadingTip) {
        mLoadingTip = loadingTip;
        if (mLoadingTv != null) {
            mLoadingTv.setText(loadingTip);
        }
    }

    public void setContent(int resid) {
        mLoadingTip = getContext().getString(resid);
        if (mLoadingTv != null) {
            mLoadingTv.setText(mLoadingTip);
        }
    }


    private void initView() {
        setContentView(R.layout.progress_dialog);
        mLoadingContainer = (LinearLayout) findViewById(R.id.loading_container);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
    }

    private void addGifView() {
        gifView = new WebView(getContext().getApplicationContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            gifView.setScrollBarSize(0);
        }
        gifView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        gifView.setBackgroundColor(Color.TRANSPARENT);
        gifView.setVerticalScrollBarEnabled(false);
        gifView.setHorizontalScrollBarEnabled(false);
        // disable scroll on touch
        gifView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        int gifWidth = Utility.dip2px(getContext(), 77.33f);
        int gifHeight = Utility.dip2px(getContext(), 87.66f);
        LinearLayout.LayoutParams gifLp = new LinearLayout.LayoutParams(gifWidth, gifHeight);
        mLoadingContainer.addView(gifView, 0, gifLp);
    }

    private void removeGifView() {
        mLoadingContainer.removeView(gifView);
        gifView.removeAllViews();
        gifView.destroy();
        gifView = null;
    }

    private void loadGif() {
        if (gifView != null) {
            String data = "<html><body><head><style>*{ margin:0; padding: 0;}</style></head><img src=\""
                    + "mascot_loading.gif" +
                    "\" width=\"100%\" loop=\"false\"/></body></html>";
            gifView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
        }
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }
}
