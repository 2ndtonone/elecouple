package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.ModuleProvider;
import com.sulong.elecouple.dagger.component.DaggerLoginComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.eventbus.RegisterSuccessEvent;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.et_login_account)
    EditText et_login_account;
    @BindView(R.id.iv_clear_account)
    ImageView iv_clear_account;
    @BindView(R.id.et_login_password)
    EditText et_login_password;
    @BindView(R.id.iv_show_pwd)
    ImageView iv_show_pwd;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Inject
    ILoginPresenter mLoginPresenter;
    @Inject
    EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initMvp();
        parseArguments();
        initViews();
        mEventBus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, "umAcountLogin");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    public void onEventMainThread(RegisterSuccessEvent event) {
        finish();
    }

    void initMvp() {
        DaggerLoginComponent.builder()
                .loginModule(ModuleProvider.getInstance().provideLoginModule(this))
                .appComponent(ComponentHolder.getAppComponent())
                .build()
                .inject(this);
    }

    void parseArguments() {
    }

    void initViews() {
        // 状态栏颜色
        setTintColor(getResources().getColor(R.color.general_bg));

    }


    @OnClick(R.id.btn_login)
    void onLoginButtonClick() {

    }

    @OnClick(R.id.tv_forget_pwd)
    void goToForgetPasswordPage() {
        startActivity(new Intent(this,ForgetPwdActivity.class));
    }

}