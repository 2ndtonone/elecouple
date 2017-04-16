package com.sulong.elecouple.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.sulong.elecouple.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/16.
 */

public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocotol);
        ButterKnife.bind(this);
    }
}
