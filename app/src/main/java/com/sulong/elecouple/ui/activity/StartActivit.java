package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sulong.elecouple.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/15.
 */

public class StartActivit extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_browsing, R.id.btn_login, R.id.btn_regeist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_browsing:

                break;
            case R.id.btn_login:
                goToUserLoginPage(StartActivit.class,null);
                break;
            case R.id.btn_regeist:
                goToRegeist();
                break;
        }
    }

    private void goToRegeist() {
        startActivity(new Intent(this,RegeistActivity.class));
    }
}
