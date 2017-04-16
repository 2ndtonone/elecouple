package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sulong.elecouple.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/15.
 */

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.tv_send_verify_code)
    TextView tvSendVerifyCode;
    @BindView(R.id.btn_next)
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_send_verify_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_verify_code:
                break;
            case R.id.btn_next:
                gotoNextStep();
                break;
        }
    }

    private void gotoNextStep() {
        startActivity(new Intent(this,ResetPwdActivity.class));
    }
}
