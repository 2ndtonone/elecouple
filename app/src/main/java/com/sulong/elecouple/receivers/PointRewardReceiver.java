package com.sulong.elecouple.receivers;

/**
 * Created by Administrator on 2015/9/8.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sulong.elecouple.utils.Debug;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PointRewardReceiver extends BroadcastReceiver {

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        printExtras(context, intent);
        Debug.i(getLogTag(), "[PointRewardReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(intent.getExtras()));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
            Debug.i(getLogTag(), "[PointRewardReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String extras = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            String message = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
            Debug.i(getLogTag(), "[PointRewardReceiver] define extras: " + extras);
            Debug.i(getLogTag(), "[PointRewardReceiver] define messages: " + message);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Debug.i(getLogTag(), "[PointRewardReceiver] 接收到推送下来的通知");
            int notificationId = intent.getIntExtra(JPushInterface.EXTRA_NOTIFICATION_ID, 0);
            Debug.i(getLogTag(), "[PointRewardReceiver] 接收到推送下来的通知的ID: " + notificationId);
            String extras = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            Debug.i(getLogTag(), "[PointRewardReceiver] 接收到推送下来的通知" + extras);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Debug.i(getLogTag(), "[PointRewardReceiver] 用户点击打开了通知");

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Debug.i(getLogTag(), "[PointRewardReceiver] 用户收到到RICH PUSH CALLBACK: " + intent.getStringExtra(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Debug.i(getLogTag(), "[PointRewardReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Debug.i(getLogTag(), "[PointRewardReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void printExtras(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        StringBuilder logSb = new StringBuilder();
        logSb.append("Print jpush broadcast data" + "\n");
        logSb.append("Intent Action:" + intent.getAction() + "\n");
        logSb.append("[Registration Id:" + JPushInterface.getRegistrationID(context) + "]\n");
        Set<String> keySet = extras.keySet();
        for (String key : keySet) {
            logSb.append("[" + key + ":" + extras.get(key) + "]\n");
        }
        Debug.li(getLogTag(), logSb.toString());
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }
}