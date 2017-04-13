package com.sulong.elecouple.mvp.model.interfaces;

import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.mvp.model.callback.interfaces.ILoginCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.IPhoneRegistrationCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.ISimpleWebRequestCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.WeChatGetTokenCallback;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginModel {

    void loginByAccount(String loginName, String password, String regId, LocationInfo locationInfo, boolean rememberPassword, ILoginCallback callback);

    void getWeChatToken(String wechatAppId, WeChatGetTokenCallback callback);

    void loginByWeChat(String wechatAuthCode, String regId, LocationInfo locationInfo, ILoginCallback callback);

    void checkPhoneHasRegistered(String phone, IPhoneRegistrationCallback callback);

    void getVerifyCode(String phone, boolean isSmsCode, ISimpleWebRequestCallback callback);

    void loginByVerifyCode(String phone, String verifyCode, String regId, LocationInfo locationInfo, ILoginCallback callback);
}