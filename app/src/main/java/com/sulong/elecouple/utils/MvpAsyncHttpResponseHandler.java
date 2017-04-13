package com.sulong.elecouple.utils;


import android.support.annotation.VisibleForTesting;

import com.sulong.elecouple.dagger.component.AppComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.eventbus.NeedLoginEvent;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.callback.interfaces.IBaseWebRequestCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.ILoadStartFinishCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.IParsedResultWebRequestCallback;
import com.sulong.elecouple.mvp.model.callback.interfaces.ISimpleWebRequestCallback;
import com.loopj.android.http.IResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 *
 * Created by ydh on 6/18/15.
 */
public class MvpAsyncHttpResponseHandler<T extends SimpleResult1> implements IResponseHandler {

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA);
    protected long startTime = 0;
    protected URI mRequestURI;
    protected RequestParams mRequestParams;
    protected Class<T> mJsonResultClass;
    protected IBaseWebRequestCallback mLoadCallback;
    protected LoginManager mLoginManagerInHandler;
    protected EventBus mEventBusInHandler;
    protected JsonParser mJsonParserInHandler;

    public MvpAsyncHttpResponseHandler(Class<T> cls, IBaseWebRequestCallback baseCallback) {
        this.mJsonResultClass = cls;
        this.mLoadCallback = baseCallback;
        initMvp();
    }

    protected void initMvp() {
        AppComponent globalComponent = ComponentHolder.getAppComponent();
        mLoginManagerInHandler = globalComponent.loginManager();
        mEventBusInHandler = globalComponent.defaultEventBus();
        mJsonParserInHandler = globalComponent.jsonParser();
    }

    protected boolean processSimpleResult(SimpleResult1 simpleResult) {
        if (simpleResult != null) {
            if (simpleResult.isOk()) {
                return true;
            } else {
                switch (simpleResult.getCode()) {
                    case SimpleResult1.STATUS_AUTH_FAIL: {
                        Debug.li(getLogTag(), SimpleResult1.STATUS_AUTH_FAIL + ":用户授权错误");
                        mLoginManagerInHandler.exitLogin();
                        mEventBusInHandler.post(new NeedLoginEvent());
                    }
                    break;
                }
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onStart(URI requestURI, RequestParams requestParams) {
        startTime = System.currentTimeMillis();
        mRequestURI = requestURI;
        mRequestParams = requestParams;
        onStart();
    }

    public void onStart() {
        if (mLoadCallback instanceof ILoadStartFinishCallback) {
            ((ILoadStartFinishCallback) mLoadCallback).onLoadStart();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        logJson(responseBody);
        T result = mJsonParserInHandler.fromJson(responseBody, mJsonResultClass);
        if (processSimpleResult(result)) {
            onSuccess(result, Utility.getStrFromByte(responseBody));
        } else {
            int errorCode = ConstantUtils.STATUS_NO_DATA;
            String errorMsg = null;
            if (result != null) {
                errorCode = result.getCode();
                errorMsg = result.getMessage();
            }
            onSuccessButWrongResponse(errorCode, errorMsg);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        logJson(responseBody);
        boolean noNetwork = false;
        // TODO 通过抛出的异常，不知道是不是判定无网络的情况，得返回一个500试一下
        if (error instanceof SocketTimeoutException
                || error instanceof ConnectTimeoutException
                || error instanceof UnknownHostException
                || error instanceof IOException) {
            noNetwork = true;
        }
        onFailure(statusCode, Utility.getStrFromByte(responseBody), error, noNetwork);
    }

    @Override
    public void onRetry(int retryNo) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onFinish() {
        if (mLoadCallback != null) {
            if (mLoadCallback instanceof ILoadStartFinishCallback) {
                ((ILoadStartFinishCallback) mLoadCallback).onLoadFinish();
            }
        }
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
    }

    public void onSuccess(T result, String responseBody) {
        if (mLoadCallback != null) {
            if (mLoadCallback instanceof ISimpleWebRequestCallback) {
                ((ISimpleWebRequestCallback) mLoadCallback).onSuccess();
            } else if (mLoadCallback instanceof IParsedResultWebRequestCallback) {
                ((IParsedResultWebRequestCallback<T>) mLoadCallback).onSuccess(result);
            }
        }
    }

    public void onSuccessButWrongResponse(int errorCode, String errorMessage) {
        if (mLoadCallback != null) {
            mLoadCallback.onFailed(errorCode, errorMessage, false);
        }
    }

    public void onFailure(int statusCode, String responseBody, Throwable error, boolean noNetwork) {
        if (mLoadCallback != null) {
            mLoadCallback.onFailed(ConstantUtils.STATUS_FAILED, responseBody, noNetwork);
        }
    }

    protected void notifyCallbackNoData() {
        if (mLoadCallback != null) {
            mLoadCallback.onFailed(ConstantUtils.STATUS_NO_DATA, null, false);
        }
    }

    private URI getRequestURI() {
        return mRequestURI;
    }

    private RequestParams getParams() {
        return mRequestParams;
    }

    private void logJson(byte[] responseBody) {
        String log = "";
        // -------- print url in curl style--------
        log += "POST: curl \"" + getRequestURI();
        if (getParams() != null) {
            log += "\" -d \"" + getParams() + "\"";
        }
        log += "\n";
        // -------- print url in normal style --------
        log += "POST: " + getRequestURI();
        if (getParams() != null) {
            if (getRequestURI() != null && !getRequestURI().toString().contains("?")) {
                log += "?";
            }
            log += "&" + getParams();
        }
        log += "\n";
        // -------- print time --------
        long finishTime = System.currentTimeMillis();
        long elapsedTime = finishTime - startTime;
        log += "Request start time : " + sDateFormat.format(new Date(startTime));
        log += " | ";
        log += "finish time : " + sDateFormat.format(new Date(finishTime));
        log += " | ";
        log += "elapsed time : " + Utility.formatInterval(elapsedTime);
        log += "\n";
        // -------- print data size and transfer speed--------
        if (responseBody != null) {
            log += "Response body size : " + Formatter.formatFileSize(responseBody.length);
            log += "\n";
            double speed = 1.0 * responseBody.length / elapsedTime; // unit: B/millisecond , as same as KB/s
            log += "Transfer speed : " + Utility.formatDouble(speed, 4) + " KB/s";
        }
        // print json with complete url
        Debug.json(getLogTag(), 3, log, Utility.getStrFromByte(responseBody));
    }

    protected String getLogTag() {
        return getClass().getSimpleName();
    }

    @VisibleForTesting
    public IBaseWebRequestCallback getLoadCallback() {
        return mLoadCallback;
    }

}