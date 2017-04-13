package com.sulong.elecouple.mvp.view;

/**
 * Created by HerbertDai on 2016/7/11.
 */
public interface IBaseListFragmentView<T> {

    void showData(T result);

    void onDataNotAvailable(int statusCode, String responseBody, Throwable error);

}
