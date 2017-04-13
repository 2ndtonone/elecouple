package com.sulong.elecouple.web;

import android.content.Context;

import com.loopj.android.http.IResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by ydh on 7/1/15.
 */
public class AhSyncHttpClient extends SyncHttpClient {

    @Override
    public RequestHandle post(Context context, String url, RequestParams params, IResponseHandler responseHandler) {
        return super.post(context, url, params, responseHandler);
    }

    @Override
    public RequestHandle get(String url, IResponseHandler responseHandler) {
        return super.get(url, responseHandler);
    }
}