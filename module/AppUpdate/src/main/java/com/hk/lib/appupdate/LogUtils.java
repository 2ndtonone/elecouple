package com.hk.lib.appupdate;

import android.util.Log;

/**
 * Created by ydh on 2016/5/30.
 */
public class LogUtils {

    private static boolean isLogEnabled = true;

    public static void enableLog(boolean enable) {
        isLogEnabled = enable;
    }

    public static void i(String tag, String msg) {
        if (isLogEnabled) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isLogEnabled) {
            Log.w(tag, msg);
        }
    }
}