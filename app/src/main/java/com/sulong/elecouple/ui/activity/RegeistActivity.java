package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulong.elecouple.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RegeistActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.tv_send_verify_code)
    TextView tvSendVerifyCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_next)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_next)
    public void gotoNext() {
        startActivity(new Intent(this, RegisterSuccessActivity.class));
    }

    @OnClick(R.id.cb_agree_protocol)
    public void onViewClicked() {
        startActivity(new Intent(this,ProtocolActivity.class));
    }
}
