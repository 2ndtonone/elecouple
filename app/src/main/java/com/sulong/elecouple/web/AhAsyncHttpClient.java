package com.sulong.elecouple.web;

import android.net.Uri;
import android.text.TextUtils;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.IResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

/**
 * Created by ydh on 6/24/15.
 */
public class AhAsyncHttpClient extends AsyncHttpClient {

    private static final String TAG = Debug.TAG;
    private static AhAsyncHttpClient instance;

    public static AhAsyncHttpClient getInstance() {
        if (instance == null) {
            instance = new AhAsyncHttpClient();
        }
        return instance;
    }

    /**
     * 发送一个post请求
     *
     * @param operator        请求动作
     * @param params          参数列表
     * @param responseHandler 返回结果回调
     * @return A Handle to an AsyncRequest which can be used to cancel a running request.
     */
    public RequestHandle post(String operator, RequestParams params, IResponseHandler responseHandler) {
        String url = ConstantUtils.getPhpUrl(operator);
        url += "&" + ConstantUtils.KEY_VERSION_NAME + "=" + Utility.getRawVersionName(LocationApplication.getInstance());
        Debug.fi(TAG, "POST: curl \"" + url + "\" -d \"" + params.toString() + "\"");
        Debug.i(TAG, "POST: " + (url.contains("?") ? url : url + "?") + "&" + params.toString());
        RequestHandle requestHandle = super.post(LocationApplication.getInstance(), url, params, responseHandler);

        // 给请求添加tag，用于activity、fragment、view销毁时取消请求
        Object tag = requestHandle.getTag();
        if (tag == null) {
            tag = operator;
            requestHandle.setTag(tag);
            Debug.fi(TAG, "POST tag : " + tag);
        }
        return requestHandle;
    }

    public String getUrlWithParam(String operator, RequestParams params) {
        String url = ConstantUtils.getPhpUrl(operator);
        url += "&" + ConstantUtils.KEY_VERSION_NAME + "=" + Utility.getRawVersionName(LocationApplication.getInstance());
        String urlWithParam = (url.contains("?") ? url : url + "?") + "&" + params.toString();
        return Uri.parse(urlWithParam).toString();
    }

    public RequestHandle post(String operator, String url, RequestParams params, IResponseHandler responseHandler) {
        url += "&" + ConstantUtils.KEY_VERSION_NAME + "=" + Utility.getRawVersionName(LocationApplication.getInstance());
        Debug.fi(TAG, "POST: curl \"" + url + "\" -d \"" + params.toString() + "\"");
        Debug.i(TAG, "POST: " + (url.contains("?") ? url : url + "?") + "&" + params.toString());
        RequestHandle requestHandle = super.post(LocationApplication.getInstance(), url, params, responseHandler);

        // 给请求添加tag，用于activity、fragment、view销毁时取消请求
        String tag = getInvokeClassName();
        if (!TextUtils.isEmpty(tag)) {
            Debug.fi(TAG, "POST tag : " + tag);
            requestHandle.setTag(tag);
        }

        return requestHandle;
    }

    /**
     * 发送一个get请求
     *
     * @param url                 请求地址
     * @param httpResponseHandler 返回结果回调
     * @return A Handle to an AsyncRequest which can be used to cancel a running request.
     */
    public RequestHandle get(String url, AsyncHttpResponseHandler httpResponseHandler) {
        Debug.fi(TAG, "GET:" + url);
        RequestHandle requestHandle = super.get(LocationApplication.getInstance(), url, httpResponseHandler);

        // 给请求添加tag，用于activity、fragment、view销毁时取消请求
        String tag = getInvokeClassName();
        if (!TextUtils.isEmpty(tag)) {
            Debug.fi(TAG, "GET tag : " + tag);
            requestHandle.setTag(tag);
        }

        return requestHandle;
    }

    private String getInvokeClassName() {
        try {
            StackTraceElement[] elements = Thread.currentThread()
                    .getStackTrace();
            for (int i = 2; i < elements.length; i++) {
                String clsName = elements[i].getClassName();
                if (!TextUtils.equals(clsName, AhAsyncHttpClient.class.getName())
                        && !TextUtils.equals(clsName, WebAPI.class.getName())) {
                    String targetSimpleClassName = clsName.substring(1 + clsName.lastIndexOf("."));
                    return targetSimpleClassName;
                }
            }
            // 约定期望的正常层级关系如下
            // [0]:dalvik.system.VMStack -> getThreadStackTrace()
            // [1]:java.lang.Thread -> getStackTrace()
            // [2]:本方法
            // [3]:本类的post或者get方法
            // [4]:WebAPI类的通用post或者get方法
            // [5]:WebAPI类的普通封装方法
            // [6]:外部类调用WebAPI类的地方
            return elements[6].getClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
