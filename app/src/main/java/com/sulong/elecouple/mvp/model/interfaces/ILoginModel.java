package com.sulong.elecouple.mvp.model.interfaces;

import com.sulong.elecouple.mvp.model.callback.interfaces.ILoginCallback;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginModel {

    void loginByAccount(String loginName, String password,boolean rememberPwd, ILoginCallback callback);

}