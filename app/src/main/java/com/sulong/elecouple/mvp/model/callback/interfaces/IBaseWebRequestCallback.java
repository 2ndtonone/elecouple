package com.sulong.elecouple.mvp.model.callback.interfaces;

/**
 * Created by ydh on 2016/7/25.
 */
public interface IBaseWebRequestCallback {
    boolean onFailed(int errorCode, String errorMessage, boolean noNetwork);
}