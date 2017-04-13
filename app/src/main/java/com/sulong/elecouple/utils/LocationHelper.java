package com.sulong.elecouple.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.LocationInfo;

/**
 * Created by ydh on 2016/9/30.
 */

public class LocationHelper {

    Context mContext;

    public LocationHelper(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public LocationInfo getLocationInfo() {
        SharedPreferences sp = getContext().getSharedPreferences(ConstantUtils.GPS_PREF, Context.MODE_PRIVATE);
        LocationInfo info = new LocationInfo();
        info.latitude = sp.getString(ConstantUtils.PREF_KEY_LATITUDE, "31.282443");
        info.longitude = sp.getString(ConstantUtils.PREF_KEY_LONGITUDE, "120.545792");
        info.province = sp.getString(ConstantUtils.PREF_KEY_GPS_PROVINCE, "");
        info.address = sp.getString(ConstantUtils.PREF_KEY_ADDRESS, "");
        info.selectedCityName = sp.getString(ConstantUtils.PREF_KEY_CITY_NAME, mContext.getString(R.string.default_city));
        info.gpsCityName = sp.getString(ConstantUtils.PREF_KEY_GPS_CITY_NAME, "");
        info.selectedDistrict = sp.getString(ConstantUtils.PREF_KEY_DISTRICT, "");
        info.gpsDistrict = sp.getString(ConstantUtils.PREF_KEY_GPS_DISTRICT, "");
        info.radius = sp.getString(ConstantUtils.PREF_KEY_RADIUS, "");
        return info;
    }

    Context getContext() {
        return mContext;
    }
}
