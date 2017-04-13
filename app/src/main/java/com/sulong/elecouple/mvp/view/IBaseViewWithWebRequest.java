package com.sulong.elecouple.mvp.view;

/**
 * Created by ydh on 2016/6/22.
 */
public interface IBaseViewWithWebRequest {
    void showNoNetworkError();

    void showWrongResponse(int code, String message);
}