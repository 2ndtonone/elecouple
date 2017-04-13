package com.loopj.android.http;

import java.net.URI;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ydh on 2016/6/29.
 */
public interface IResponseHandler {
    void onStart(URI requestURI, RequestParams requestParams);

    void onSuccess(int statusCode, Header[] headers, byte[] responseBody);

    void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error);

    void onRetry(int retryNo);

    void onCancel();

    void onFinish();

    void onProgress(long bytesWritten, long totalSize);
}