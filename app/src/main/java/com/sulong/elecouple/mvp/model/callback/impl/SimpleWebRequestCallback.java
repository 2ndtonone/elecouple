package com.sulong.elecouple.mvp.model.callback.impl;

import com.sulong.elecouple.mvp.model.callback.interfaces.ISimpleWebRequestCallback;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;

/**
 * Created by ydh on 2016/7/25.
 */
public abstract class SimpleWebRequestCallback extends BaseWebRequestCallback
        implements ISimpleWebRequestCallback {

    public SimpleWebRequestCallback(IBaseViewWithWebRequest view) {
        super(view);
    }

}