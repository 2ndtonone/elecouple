package com.sulong.elecouple.web;

import com.loopj.android.http.IResponseHandler;

/**
 * {@link WebAPI}的非静态包装类<br>
 * <p>Mockito不能mock静态方法，为了让Mockito可以捕获handler回调，做一层非静态包装，以便被mock</p>
 * Created by ydh on 2016/6/24.
 */
public class WebClient {

    public void loginByAccount(String loginName, String password, IResponseHandler handler) {

    }

    public void getPhoneVerifyCode(String phone, boolean isSmsCode, IResponseHandler handler) {
    }

}