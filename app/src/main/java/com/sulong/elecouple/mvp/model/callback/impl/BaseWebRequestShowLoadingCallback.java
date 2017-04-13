package com.sulong.elecouple.mvp.model.callback.impl;

import com.sulong.elecouple.mvp.model.callback.interfaces.ILoadStartFinishCallback;
import com.sulong.elecouple.mvp.view.IBaseViewWithLoading;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;

/**
 * Created by ydh on 2016/7/26.
 */
public abstract class BaseWebRequestShowLoadingCallback extends BaseWebRequestCallback
        implements ILoadStartFinishCallback {

    protected IBaseViewWithLoading mLoadingView;

    public BaseWebRequestShowLoadingCallback(IBaseViewWithWebRequest webRequestView, IBaseViewWithLoading loadingView) {
        super(webRequestView);
        this.mLoadingView = loadingView;
    }

    @Override
    public void onLoadStart() {
        mLoadingView.showLoadingView();
    }

    @Override
    public void onLoadFinish() {
        mLoadingView.hideLoadingView();
    }
}