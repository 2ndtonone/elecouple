package com.sulong.elecouple.mvp.model.callback.impl;

import com.sulong.elecouple.mvp.model.callback.interfaces.IBaseWebRequestCallback;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;
import com.sulong.elecouple.utils.StringUtils;

/**
 * Created by ydh on 2016/7/25.
 */
public class BaseWebRequestCallback implements IBaseWebRequestCallback {

    protected IBaseViewWithWebRequest mBaseView;

    public BaseWebRequestCallback(IBaseViewWithWebRequest view) {
        this.mBaseView = view;
    }

    @Override
    public boolean onFailed(int errorCode, String errorMessage, boolean noNetwork) {
        return handleCommonError(errorCode, errorMessage, noNetwork);
    }

    public boolean handleCommonError(int errorCode, String errorMessage, boolean noNetwork) {
        if (noNetwork) {
            mBaseView.showNoNetworkError();
            return true;
        } else if (!StringUtils.isEmpty(errorMessage)) {
            mBaseView.showWrongResponse(errorCode, errorMessage);
            return true;
        } else {
            return false;
        }
    }

}