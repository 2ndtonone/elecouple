package com.sulong.elecouple.mvp.presenter.impl;

import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.callback.impl.LoginCallback;
import com.sulong.elecouple.mvp.model.callback.impl.PhoneRegistrationCallback;
import com.sulong.elecouple.mvp.model.callback.impl.SimpleWebRequestCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.WeChatGetTokenCallback;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;
import com.sulong.elecouple.utils.StringUtils;

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
    public void fillLoginFieldsIfNeed() {
        String loginName = mLoginManager.getRememberedLoginName();
        String loginPassword = mLoginManager.getLoginPassword();
        boolean rememberPassword = mLoginManager.isRememberPassword();
        mView.fillLoginFieldsIfNeed(loginName, loginPassword, rememberPassword);
    }

    @Override
    public boolean isShowPassword() {
        return mShowPassword;
    }

    @Override
    public void showPassword(boolean shown) {
        mShowPassword = shown;
        mView.showPassword(shown);
    }

    @Override
    public void toggleShowPassword() {
        showPassword(!isShowPassword());
    }

    @Override
    public void loginByAccount(String loginName, String password, String regId, LocationInfo locationInfo, boolean rememberPassword) {
        // 检查输入的登录名是否合法
        if (StringUtils.isEmpty(loginName)) {
            mView.showWrongInputLoginName();
            return;
        }
        // 检查输入的密码是否合法
        if (StringUtils.isEmpty(password)) {
            mView.showWrongInputPassword();
            return;
        }
        mModel.loginByAccount(loginName, password, regId, locationInfo, rememberPassword, new MyLoginCallback(LoginType.ACCOUNT));
    }

    @Override
    public void getWeChatToken(String wechatAppId) {
        mView.showLoadingView();
        mModel.getWeChatToken(wechatAppId, new WeChatGetTokenCallback() {
            @Override
            public void onWeChatNotInstalled() {
                mView.hideLoadingView();
                mView.showWeChatNotInstalled();
            }
        });
    }

    @Override
    public void loginByWeChat(String wechatAuthCode, String regId, LocationInfo locationInfo) {
        if (StringUtils.isEmpty(wechatAuthCode)) {
            mView.hideLoadingView();
            mView.showLoginFailed(LoginType.WECHAT);
        } else {
            mModel.loginByWeChat(wechatAuthCode, regId, locationInfo, new MyLoginCallback(LoginType.WECHAT));
        }
    }

    @Override
    public void checkPhoneHasRegistered(String phone) {
        mModel.checkPhoneHasRegistered(phone, new PhoneRegistrationCallback(mView) {
            @Override
            public void onPhoneHasRegistered() {
                updateUi(true);
            }

            @Override
            public void onPhoneHasNotRegistered() {
                mView.showInputPhoneHasNotRegistered();
                updateUi(false);
            }

            @Override
            public boolean onFailed(int errorCode, String errorMessage, boolean noNetwork) {
                updateUi(false);
                return super.onFailed(errorCode, errorMessage, noNetwork);
            }

            public void updateUi(boolean hasRegistered) {
                mView.markInputPhoneRegistrationState(hasRegistered);
                mView.enableSendVerifyCodeButtons(hasRegistered);
                mView.updateLoginButtonUi();
            }
        });
    }

    @Override
    public void getVerifyCode(String phone, final boolean isSmsCode) {
        mModel.getVerifyCode(phone, isSmsCode, new SimpleWebRequestCallback(mView) {
            @Override
            public void onSuccess() {
                mView.showVerifyCodeSentMessage(isSmsCode);
                mView.showTimeCountDownView(true);
            }
        });
    }

    @Override
    public void loginByVerifyCode(String phone, String verifyCode, String regId, LocationInfo locationInfo) {
        mModel.loginByVerifyCode(phone, verifyCode, regId, locationInfo, new MyLoginCallback(LoginType.VERIFY_CODE));
    }

    public class MyLoginCallback extends LoginCallback {

        public MyLoginCallback(LoginType type) {
            super(mView, mView, type);
        }

        @Override
        public void onSuccess() {
            mView.exit();
        }

        @Override
        public boolean onFailed(int errorCode, String errorMessage, boolean noNetwork) {
            if (!super.onFailed(errorCode, errorMessage, noNetwork)) {
                mView.showLoginFailed(mLoginType);
            }
            return true;
        }
    }

}