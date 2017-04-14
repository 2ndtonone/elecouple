package com.sulong.elecouple.mvp.presenter.impl;

import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.callback.impl.BaseWebRequestShowLoadingCallback;
import com.sulong.elecouple.mvp.model.callback.impl.LoginCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.IParsedResultWebRequestCallback;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;

/**
 * 除了UI交互的直接逻辑在Activity或Fragment中处理，其他逻辑都在此处理
 * Created by ydh on 2016/6/21.
 */
public class LoginPresenter implements ILoginPresenter {

    private ILoginView mView;
    private ILoginModel mModel;
    private LoginManager mLoginManager;
    private boolean mShowPassword = false;

    public LoginPresenter(ILoginView loginView, ILoginModel loginModel, LoginManager loginManager) {
        this.mView = loginView;
        this.mModel = loginModel;
        this.mLoginManager = loginManager;
    }

    @Override
    public void loginByAccount(String loginName, String password,boolean rememberPassword) {

        mModel.loginByAccount(loginName, password, rememberPassword, new LoginCallback(mView,mView) {
            @Override
            public void onSuccess() {

            }
        });
    }

}