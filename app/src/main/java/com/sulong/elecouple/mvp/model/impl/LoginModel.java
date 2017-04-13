package com.sulong.elecouple.mvp.model.impl;

import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.eventbus.LoginSuccessEvent;
import com.sulong.elecouple.login.LoginItem;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.callback.interfaces.ILoginCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.IPhoneRegistrationCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.ISimpleWebRequestCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.WeChatGetTokenCallback;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.JpushHelper;
import com.sulong.elecouple.utils.MvpAsyncHttpResponseHandler;
import com.sulong.elecouple.web.WebClient;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import de.greenrobot.event.EventBus;

/**
 * Created by ydh on 2016/6/21.
 */
public class LoginModel implements ILoginModel {

    private WebClient mWebClient;
    private JpushHelper mJpushHelper;
    private IWXAPI mWxapi;
    private LoginManager mLoginManager;
    private EventBus mEventBus;

    public LoginModel(WebClient client, JpushHelper jpushHelper, IWXAPI iwxapi,
                      LoginManager loginManager, EventBus eventBus) {
        this.mWebClient = client;
        this.mJpushHelper = jpushHelper;
        this.mWxapi = iwxapi;
        this.mLoginManager = loginManager;
        this.mEventBus = eventBus;
    }

    @Override
    public void loginByAccount(final String loginName, final String password, String regId, LocationInfo locationInfo,
                               final boolean rememberPassword, final ILoginCallback callback) {
        mWebClient.loginByAccount(loginName, password, regId,
                locationInfo.latitude, locationInfo.longitude, locationInfo.province,
                locationInfo.selectedCityName, locationInfo.gpsDistrict, "0",
                new LoginResponseHandler(callback, LoginType.ACCOUNT) {
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

    @Override
    public void getWeChatToken(String wechatAppId, WeChatGetTokenCallback callback) {
        boolean isInstallWX = mWxapi.isWXAppInstalled();
        if (!isInstallWX) {
            if (callback != null) {
                callback.onWeChatNotInstalled();
            }
            return;
        }
        mWxapi.registerApp(wechatAppId);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_" + System.currentTimeMillis() / 1000;
        mWxapi.sendReq(req);
    }

    @Override
    public void loginByWeChat(String wechatAuthCode, String regId, LocationInfo locationInfo, final ILoginCallback callback) {
        mWebClient.loginByWechat(wechatAuthCode, regId,
                locationInfo.latitude, locationInfo.longitude, locationInfo.province,
                locationInfo.selectedCityName, locationInfo.gpsDistrict,
                new LoginResponseHandler(callback, LoginType.WECHAT));
    }

    @Override
    public void checkPhoneHasRegistered(String phone, final IPhoneRegistrationCallback callback) {
        mWebClient.checkPhoneHasRegisteredUserAccount(phone, new PhoneRegistrationResponseHandler(callback));
    }

    @Override
    public void getVerifyCode(String phone, boolean isSmsCode, final ISimpleWebRequestCallback callback) {
        mWebClient.getPhoneVerifyCode(phone, isSmsCode, new MvpAsyncHttpResponseHandler<>(SimpleResult1.class, callback));
    }

    @Override
    public void loginByVerifyCode(String phone, String verifyCode, String regId, LocationInfo locationInfo, ILoginCallback callback) {
        mWebClient.loginByVerifyCode(phone, verifyCode, regId, locationInfo.latitude, locationInfo.longitude, locationInfo.province,
                locationInfo.selectedCityName, locationInfo.gpsDistrict, "0", new LoginResponseHandler(callback, LoginType.VERIFY_CODE));
    }

    static class PhoneRegistrationResponseHandler extends MvpAsyncHttpResponseHandler<SimpleResult1> {

        IPhoneRegistrationCallback callback;

        public PhoneRegistrationResponseHandler(IPhoneRegistrationCallback callback) {
            super(SimpleResult1.class, callback);
            this.callback = callback;
        }

        @Override
        public void onSuccess(SimpleResult1 result, String responseBody) {
            if (callback != null) {
                callback.onPhoneHasRegistered();
            }
        }

        @Override
        public void onSuccessButWrongResponse(int errorCode, String errorMessage) {
            if (callback != null) {
                boolean hasNotRegistered = errorCode == ConstantUtils.STATUS_NOT_EXPECT_RESULT;
                if (hasNotRegistered) {
                    callback.onPhoneHasNotRegistered();
                } else {
                    super.onSuccessButWrongResponse(errorCode, errorMessage);
                }
            }
        }
    }

    class LoginResponseHandler extends MvpAsyncHttpResponseHandler<LoginItem> {

        ILoginCallback mLoginCallback;
        LoginType mLoginType;

        public LoginResponseHandler(ILoginCallback callback, LoginType loginType) {
            super(LoginItem.class, callback);
            this.mLoginCallback = callback;
            this.mLoginType = loginType;
        }

        @Override
        public void onSuccess(LoginItem result, String responseBody) {
            if (result.data == null) {
                notifyCallbackNoData();
                return;
            }
            // 保存登录数据
            mLoginManager.saveUserLoginData(result.data);
            mLoginManager.setLoginType(mLoginType);
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