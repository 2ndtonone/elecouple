package com.sulong.elecouple.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Map.Entry;

public class SharePreUtil {
    private static String CONFIG = "config";
    private static SharedPreferences sharedPreferences;

    public static void saveStringData(Context context, String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getStringData(Context context, String key, String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    public static void showAllStringData(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        Map<String, String> all = (Map<String, String>) sharedPreferences.getAll();
        for (Entry<String, String> entry : all.entrySet()) {
            System.out.println(entry.getKey() + ":::" + entry.getValue());
        }
    }

    public static void deleteStringData(Context context, String key) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().remove(key).commit();
    }

}
