package com.sulong.elecouple.utils;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by ydh on 2016/5/9.
 */
public class BaiduSDKInitializer {

    private static boolean isInitialized = false;

    public static void initialize(String var0, Context var1) {
        if (!isInitialized) {
            SDKInitializer.initialize(var0, var1);
            isInitialized = true;
        }
    }

    public static void initialize(Context var0) {
        if (!isInitialized) {
            SDKInitializer.initialize(var0);
            isInitialized = true;
        }
    }
}