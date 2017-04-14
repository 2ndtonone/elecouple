package com.sulong.elecouple.mvp.presenter.interfaces;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginPresenter {
    void loginByAccount(String loginName, String password,boolean rememberPassword);
}