package com.sulong.elecouple.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sulong.elecouple.entity.LocationInfo;

/**
 * SharedPreference辅助类
 * Created by ydh on 2016/6/7.
 */
public class PersistUtils {

    public static void setDefaultShareImageUrl(Context context, String url) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ConstantUtils.PREF_KEY_DEFAULT_SHARE_IMAGE, url)
                .apply();
    }

    public static String getDefaultShareImageUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ConstantUtils.PREF_KEY_DEFAULT_SHARE_IMAGE, "");
    }

    public static void markHasEnteredApp(Context context) {
        int curVersionCode = Utility.getVersionCode(context);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(ConstantUtils.PREF_KEY_VERSION_CODE, curVersionCode)
                .apply();
    }

    public static boolean isFirstEnterApp(Context context) {
        int versionCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(ConstantUtils.PREF_KEY_VERSION_CODE, 0);
        int curVersionCode = Utility.getVersionCode(context);
        return versionCode != curVersionCode;
    }

    public static void setLastLocateTime(Context context, long time) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(ConstantUtils.PREF_KEY_LAST_LOCATE_TIME, time)
                .apply();
    }

    public static boolean isNeedRelocate(Context context) {
        long lastLocateTime = PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(ConstantUtils.PREF_KEY_LAST_LOCATE_TIME, 0);
        long now = System.currentTimeMillis();
        long threshold = 5 * 60 * 1000; // 5分钟后重新定位
        long interval = now - lastLocateTime;
        String leftTimeText = Utility.formatMinuteAndSeconds(threshold - interval);
        Debug.li(getLogTag(), leftTimeText + "后可重新定位");
        return interval > threshold;
    }

    public static LocationInfo getLocationInfo(Context context) {
        LocationInfo info = new LocationInfo();
        SharedPreferences sp = context.getSharedPreferences(ConstantUtils.GPS_PREF, Context.MODE_PRIVATE);
        info.latitude = sp.getString(ConstantUtils.PREF_KEY_LATITUDE, "");
        info.longitude = sp.getString(ConstantUtils.PREF_KEY_LONGITUDE, "");
        info.province = sp.getString(ConstantUtils.PREF_KEY_GPS_PROVINCE, "");
        info.address = sp.getString(ConstantUtils.PREF_KEY_ADDRESS, "");
        info.selectedCityName = sp.getString(ConstantUtils.PREF_KEY_CITY_NAME, "");
        info.gpsCityName = sp.getString(ConstantUtils.PREF_KEY_GPS_CITY_NAME, "");
        info.selectedDistrict = sp.getString(ConstantUtils.PREF_KEY_DISTRICT, "");
        info.gpsDistrict = sp.getString(ConstantUtils.PREF_KEY_GPS_DISTRICT, "");
        info.radius = sp.getString(ConstantUtils.PREF_KEY_RADIUS, "");
        return info;
    }

    private static String getLogTag() {
        return PersistUtils.class.getSimpleName();
    }

}