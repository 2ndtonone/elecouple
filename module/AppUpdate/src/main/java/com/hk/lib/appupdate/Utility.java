package com.hk.lib.appupdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by ydh on 2016/5/27.
 */

class Utility {

    public static String getLogTag() {
        return Utility.class.getSimpleName();
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static void installApk(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.w(getLogTag(), "file path cannot be null");
            return;
        }
        File apkFile = new File(filePath);
        if (!apkFile.exists()) {
            LogUtils.w(getLogTag(), "file does not exist");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int getVersionCode(Context context) {
        int verCode = 0;
        try {
            PackageInfo appInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            verCode = appInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCode;
    }
}