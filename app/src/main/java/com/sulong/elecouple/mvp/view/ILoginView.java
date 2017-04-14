package com.sulong.elecouple.mvp.view;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginView extends IBaseViewWithWebRequest, IBaseViewWithLoading {
    void showLoadingView();

    void hideLoadingView();

}