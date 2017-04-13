package com.sulong.elecouple.web;

import android.os.Build;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Utility;
import com.loopj.android.http.RequestParams;

/**
 *
 * Created by ydh on 6/2/15.
 */
public class WebParam extends RequestParams {

    public static final String CLIENT_TYPE = "android";

    public WebParam() {
        put(ConstantUtils.KEY_ANDROID_VERSION, Build.VERSION.SDK_INT);
        put(ConstantUtils.KEY_VERSION_CODE, Utility.getVersionCode(LocationApplication.getInstance()));
        put(ConstantUtils.KEY_VERSION_NAME, Utility.getRawVersionName(LocationApplication.getInstance()));
        put(ConstantUtils.KEY_TOKEN, LoginManager.getInstance().getToken());
        put(ConstantUtils.KEY_CLIENT_TYPE, CLIENT_TYPE);
        put(ConstantUtils.KEY_MEMBER_ID, LoginManager.getInstance().getUserId());
        put(ConstantUtils.KEY_TOKEN_MEMBER_ID, LoginManager.getInstance().getUserId());
    }
}
