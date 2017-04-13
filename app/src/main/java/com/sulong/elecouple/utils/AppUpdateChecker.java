package com.sulong.elecouple.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.entity.SimpleResult1;
import com.sulong.elecouple.entity.UpdateInfo;
import com.sulong.elecouple.web.WebAPI;
import com.hk.lib.appupdate.AppUpdateManager;

import cz.msebera.android.httpclient.Header;

public class AppUpdateChecker {
    private static final int REQUEST_INTERVAL = 1000 * 60 * 10;

    private static boolean isNeedRequest() {
        Context context = LocationApplication.getInstance();
        long lastRequestTime = AppUpdateManager.getLastRequestTime(context);
        long now = System.currentTimeMillis();
        return now - lastRequestTime > REQUEST_INTERVAL;
    }

    /**
     * 检查应用是否有更新
     *
     * @param manual   true表示手动点击按钮检查更新，false表示非用户操作的检查更新
     * @param callback
     */
    public static void checkUpdate(final boolean manual, final Callback callback) {
        if (!manual && !isNeedRequest()) {
            Debug.li(getLogTag(), "使用缓存的应用更新信息");
            if (callback != null) {
                callback.onSuccess();
                callback.onFinish();
            }
            return;
        }
        WebAPI.getCheckUpdatePost(new AggAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Context context = LocationApplication.getInstance();
                final UpdateInfo updateInfo = JsonParser.getInstance().fromJson(responseBody, UpdateInfo.class);
                if (processSimpleResult(updateInfo, context, !manual) && updateInfo.data != null) {
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit()
                            .putString(ConstantUtils.KEY_SERVICE_TELEPHONE, updateInfo.data.service_telephone)
                            .apply();
                    AppUpdateManager.setLastRequestTime(context, System.currentTimeMillis());
                    AppUpdateManager.saveAppUpdateInfo(
                            context,
                            updateInfo.data.mobile_apk,
                            updateInfo.data.mobile_apk_version,
                            updateInfo.data.mobile_apk_force_version,
                            updateInfo.data.android_update_info
                    );
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    PersistUtils.setDefaultShareImageUrl(context, updateInfo.data.share_default_pic);
                    if (TextUtils.isEmpty(updateInfo.data.area_version)) {
                        return;
                    }
                    String area_version = PreferenceManager.getDefaultSharedPreferences(context).getString(ConstantUtils.KEY_AREA_VERSION, "");
                    if (!TextUtils.equals(area_version, updateInfo.data.area_version)) {
                        getAreaData(updateInfo.data.area_version);
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
    }

    private static void getAreaData(final String area_version) {
        WebAPI.getAreaData(new AggAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                final SimpleResult1 result = JsonParser.getInstance().fromJson(responseBody, SimpleResult1.class);
                if (processSimpleResult(result, LocationApplication.getInstance())) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                if (Utility.wirteJsonToFile(LocationApplication.getInstance(), new String(responseBody, "UTF-8"), ConstantUtils.FILE_ALL_AREA_JSON_NAME)) {
                                    PreferenceManager.getDefaultSharedPreferences(LocationApplication.getInstance()).edit().putString(ConstantUtils.KEY_AREA_VERSION, area_version).apply();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }.start();
                }
            }
        });
    }

    private static String getLogTag() {
        return AppUpdateChecker.class.getSimpleName();
    }

    public interface Callback {
        void onSuccess();

        void onFinish();
    }
}