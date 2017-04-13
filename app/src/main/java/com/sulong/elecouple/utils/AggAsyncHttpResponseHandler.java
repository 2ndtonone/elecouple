package com.sulong.elecouple.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.eventbus.NeedLoginEvent;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.ui.activity.LoginActivity;
import com.sulong.elecouple.ui.views.CustomToast;
import com.loopj.android.http.IResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 *
 * Created by ydh on 6/18/15.
 */
public class AggAsyncHttpResponseHandler implements IResponseHandler {

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA);
    protected long startTime = 0;
    private URI mRequestURI;
    private RequestParams mRequestParams;

    protected boolean processSimpleResult(SimpleResult1 simpleResult, Context context) {
        return processSimpleResult(simpleResult, context, false);
    }

    protected boolean processSimpleResult(SimpleResult1 simpleResult, Context context, boolean mute) {
        return processSimpleResult(simpleResult, context, mute, false);
    }

    protected boolean processSimpleResult(SimpleResult1 simpleResult, Context context, boolean mute, boolean doNothing) {
        if (context == null) {
            return false;
        }

        if (simpleResult != null) {
            if (simpleResult.isOk()) {
                return true;
            } else {
                if (!mute && !StringUtils.isEmpty(simpleResult.getMessage())) {
                    CustomToast.makeText(context, simpleResult.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (!doNothing) {
                    if (simpleResult.getCode() == SimpleResult1.STATUS_AUTH_FAIL) {
                        LoginManager.getInstance().exitLogin();
                        EventBus.getDefault().post(new NeedLoginEvent());
                        Intent intent = new Intent(context, LoginActivity.class);
                        if (!(context instanceof Activity)) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                    }
                }

                return false;
            }
        } else {
            if (!mute) {
                CustomToast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
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
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        logJson(responseBody);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        logJson(responseBody);
        if (toastWhenNoNetworkErrorOccurred() &&
                (error instanceof SocketTimeoutException
                        || error instanceof ConnectTimeoutException
                        || responseBody == null)) {
            CustomToast.makeText(LocationApplication.getInstance(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRetry(int retryNo) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
    }

    protected boolean toastWhenNoNetworkErrorOccurred() {
        return true;
    }

    protected URI getRequestURI() {
        return mRequestURI;
    }

    protected RequestParams getParams() {
        return mRequestParams;
    }

    private void logJson(byte[] responseBody) {
        StringBuilder logSb = new StringBuilder();
        // -------- print url in curl style--------
        logSb.append("POST: curl \"" + getRequestURI());
        if (getParams() != null) {
            logSb.append("\" -d \"" + getParams() + "\"");
        }
        logSb.append("\n");
        // -------- print url in normal style --------
        logSb.append("POST: " + getRequestURI());
        if (getParams() != null) {
            if (getRequestURI() != null && !getRequestURI().toString().contains("?")) {
                logSb.append("?");
            }
            logSb.append("&" + getParams());
        }
        logSb.append("\n");
        // -------- print time --------
        long finishTime = System.currentTimeMillis();
        long elapsedTime = finishTime - startTime;
        logSb.append("Request start time : " + sDateFormat.format(new Date(startTime)));
        logSb.append(" | ");
        logSb.append("finish time : " + sDateFormat.format(new Date(finishTime)));
        logSb.append(" | ");
        logSb.append("elapsed time : " + Utility.formatInterval(elapsedTime));
        logSb.append("\n");
        // -------- print data size and transfer speed--------
        if (responseBody != null) {
            logSb.append("Response body size : " + Formatter.formatFileSize(responseBody.length));
            logSb.append("\n");
            double speed = 1.0 * responseBody.length / elapsedTime; // unit: B/millisecond , as same as KB/s
            logSb.append("Transfer speed : " + Utility.formatDouble(speed, 4) + " KB/s");
        }
        // print json with complete url
        Debug.json(getLogTag(), 3, logSb.toString(), Utility.getStrFromByte(responseBody));
    }

    public String getLogTag() {
        return getInvokeClassName();
    }

    private String getInvokeClassName() {
        try {
            StackTraceElement[] elements = Thread.currentThread()
                    .getStackTrace();
            // 约定期望的正常层级关系如下
            // [0]:dalvik.system.VMStack -> getThreadStackTrace()
            // [1]:java.lang.Thread -> getStackTrace()
            // [2]:本类 -> 本方法
            // [3]:本类 -> getLogTag方法
            // [4]:本类 -> onSuccess、onFailure方法
            // [5]:生成本类实例的外部类 -> onSuccess、onFailure方法
            for (int i = 2; i < elements.length; i++) {
                String clsName = elements[i].getClassName();
                if (!TextUtils.equals(clsName, AggAsyncHttpResponseHandler.class.getName())) {
                    String targetSimpleClassName = clsName.substring(1 + clsName.lastIndexOf("."));
                    return targetSimpleClassName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getClass().getSimpleName();
    }
}
