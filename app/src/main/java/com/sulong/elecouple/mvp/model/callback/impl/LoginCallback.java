package com.sulong.elecouple.mvp.model.callback.impl;

import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.mvp.model.callback.interfaces.ILoginCallback;
import com.sulong.elecouple.mvp.view.IBaseViewWithLoading;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;

/**
 * Created by ydh on 2016/7/25.
 */
public abstract class LoginCallback extends BaseWebRequestShowLoadingCallback implements ILoginCallback {


    public LoginCallback(IBaseViewWithWebRequest webRequestView, IBaseViewWithLoading loadingView) {
        super(webRequestView, loadingView);
    }

}