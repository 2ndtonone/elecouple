package com.sulong.elecouple.mvp.model.callback.impl;

import com.sulong.elecouple.mvp.model.callback.interfaces.IPhoneRegistrationCallback;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;

/**
 * Created by ydh on 2016/7/25.
 */
public abstract class PhoneRegistrationCallback extends BaseWebRequestCallback implements IPhoneRegistrationCallback {

    public PhoneRegistrationCallback(IBaseViewWithWebRequest view) {
        super(view);
    }

}