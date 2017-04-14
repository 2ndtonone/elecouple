package com.sulong.elecouple.mvp.model.impl;

import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.eventbus.LoginSuccessEvent;
import com.sulong.elecouple.login.LoginItem;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.callback.interfaces.ILoginCallback;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.utils.JpushHelper;
import com.sulong.elecouple.utils.MvpAsyncHttpResponseHandler;
import com.sulong.elecouple.web.WebClient;

import de.greenrobot.event.EventBus;

/**
 * Created by ydh on 2016/6/21.
 */
public class LoginModel implements ILoginModel {

    private WebClient mWebClient;
    private JpushHelper mJpushHelper;
    private LoginManager mLoginManager;
    private EventBus mEventBus;

    public LoginModel(WebClient client, JpushHelper jpushHelper,
                      LoginManager loginManager, EventBus eventBus) {
        this.mWebClient = client;
        this.mJpushHelper = jpushHelper;
        this.mLoginManager = loginManager;
        this.mEventBus = eventBus;
    }

    @Override
    public void loginByAccount(final String loginName, final String password,
                               final boolean rememberPassword, final ILoginCallback callback) {
        mWebClient.loginByAccount(loginName, password,
                new LoginResponseHandler(callback) {
                    @Override
                    public void onSuccess(LoginItem result, String responseBody) {
                        // 保存登录密码
                        mLoginManager.setRememberedLoginName(loginName);
                        mLoginManager.saveLoginPassword(password);
                        mLoginManager.setRememberPassword(rememberPassword);
                        super.onSuccess(result, responseBody);
                    }
                });
    }

    class LoginResponseHandler extends MvpAsyncHttpResponseHandler<LoginItem> {

        ILoginCallback mLoginCallback;

        public LoginResponseHandler(ILoginCallback callback) {
            super(LoginItem.class, callback);
            this.mLoginCallback = callback;
        }

        @Override
        public void onSuccess(LoginItem result, String responseBody) {
            if (result.data == null) {
                notifyCallbackNoData();
                return;
            }
            // 保存登录数据
            mLoginManager.saveUserLoginData(result.data);
            // 设置极光推送
            String loginName = result.data.username;
            mJpushHelper.setupJpush(loginName);
            // 发送登录成功事件
            if (mLoginCallback != null) {
                mLoginCallback.onSuccess();
            }
            mEventBus.post(new LoginSuccessEvent());
        }
    }
}