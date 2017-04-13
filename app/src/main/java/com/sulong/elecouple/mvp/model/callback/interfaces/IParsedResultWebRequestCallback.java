package com.sulong.elecouple.mvp.model.callback.interfaces;

/**
 * Created by ydh on 2016/7/25.
 */
public interface IParsedResultWebRequestCallback<T> extends IBaseWebRequestCallback {
    void onSuccess(T result);
}