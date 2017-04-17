package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sulong.elecouple.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RegisterSuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_success);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_complete_user_info, R.id.tv_browsing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_complete_user_info:
                break;
            case R.id.tv_browsing:
                goToSelectSex();
                break;
        }
    }

    private void goToSelectSex() {
        startActivity(new Intent(this,SelectSexActivity.class));
    }
}
