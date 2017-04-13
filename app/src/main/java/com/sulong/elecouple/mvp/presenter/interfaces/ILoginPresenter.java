package com.sulong.elecouple.mvp.presenter.interfaces;

import com.sulong.elecouple.entity.LocationInfo;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginPresenter {

    void fillLoginFieldsIfNeed();

    void showPassword(boolean shown);

    void toggleShowPassword();

    boolean isShowPassword();

    void loginByAccount(String loginName, String password, String regId, LocationInfo locationInfo, boolean rememberPassword);

    void loginByWeChat(String wechatAuthCode, String regId, LocationInfo locationInfo);

    void loginByVerifyCode(String phone, String verifyCode, String regId, LocationInfo locationInfo);

    void getWeChatToken(String wechatAppId);

    void checkPhoneHasRegistered(String phone);

    void getVerifyCode(String phone, boolean isSmsCode);
}