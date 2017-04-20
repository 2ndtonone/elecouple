package com.sulong.elecouple.web;

import com.sulong.elecouple.utils.AggAsyncHttpResponseHandler;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Utility;
import com.loopj.android.http.IResponseHandler;
import com.loopj.android.http.RequestHandle;

/**
 *
 * Created by ydh on 7/24/15.
 */
public class WebAPI {

    public static RequestHandle get(String operator, WebParam params, IResponseHandler httpResponseHandler) {
        String url = ConstantUtils.getPhpUrl(operator);
        url = Utility.addParamToUrl(url, params);
        return AhAsyncHttpClient.getInstance().get(url, httpResponseHandler);
    }

    public static RequestHandle post(String operator, WebParam params, IResponseHandler httpResponseHandler) {
        return AhAsyncHttpClient.getInstance().post(operator, params, httpResponseHandler);
    }

    public static void getCheckUpdatePost(IResponseHandler handler) {
        String operator = "";
        WebParam params = new WebParam();
        get(operator, params, handler);
    }

    public static void getAreaData(IResponseHandler handler) {

    }

    public static void getSearchList(IResponseHandler handler) {
        String operator = "";
        WebParam params = new WebParam();
        get(operator, params, handler);

    }

    public static void getFindTabList(IResponseHandler handler) {
        String operator = "";
        WebParam params = new WebParam();
        get(operator, params, handler);
    }
}