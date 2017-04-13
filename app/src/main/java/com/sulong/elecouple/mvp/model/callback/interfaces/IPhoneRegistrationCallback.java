package com.sulong.elecouple.mvp.model.callback.interfaces;

/**
 * Created by ydh on 2016/7/19.
 */
public interface IPhoneRegistrationCallback extends IBaseWebRequestCallback {
    void onPhoneHasRegistered();

    void onPhoneHasNotRegistered();
}