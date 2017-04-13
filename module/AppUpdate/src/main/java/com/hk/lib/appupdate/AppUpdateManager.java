package com.hk.lib.appupdate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

/**
 * 应用更新管理器
 * Created by ydh on 2016/5/27.
 */
public class AppUpdateManager {

    private static String getLogTag() {
        return AppUpdateManager.class.getSimpleName();
    }

    static int getDownloadId(Context context) {
        String path = getApkFilePath(context);
        String url = getApkDownloadUrl(context);
        if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(url)) {
            return FileDownloadUtils.generateId(url, path);
        }
        return 0;
    }

    static boolean deleteDownloadFileIfExisted(Context context) {
        String path = getApkFilePath(context);
        if (TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    public static void showAppUpdateDialogIfNeed(FragmentActivity activity, boolean silent) {
        int curVersionCode = Utility.getVersionCode(activity);
        int forceVersion = getForceUpdateVersion(activity);
        int normalVersion = getNormalUpdateVersion(activity);
        String url = getApkDownloadUrl(activity);

        StringBuilder logSb = new StringBuilder();
        logSb.append("-------doUpdateWork------" + " | ");
        logSb.append("curVersionCode : " + curVersionCode + " | ");
        logSb.append("forceVersion : " + forceVersion + " | ");
        logSb.append("normalVersion : " + normalVersion + " | ");

        if (curVersionCode < forceVersion) {
            // 显示强制更新对话框
            logSb.append("显示强制更新对话框" + " | ");
            showAppUpdateDialog(true, activity, url, forceVersion);
        } else if (curVersionCode < normalVersion) {
            // 显示普通更新对话框
            logSb.append("显示普通更新对话框" + " | ");
            showAppUpdateDialog(false, activity, url, normalVersion);
        } else {
            // 没有更新
            logSb.append("没有更新" + " | ");
            if (!silent) {
                Toast.makeText(activity, R.string.app_update_up_to_date, Toast.LENGTH_LONG).show();
            }
        }
        LogUtils.i(getLogTag(), logSb.toString());
    }

    private static void showAppUpdateDialog(boolean force, FragmentActivity activity, String downloadUrl, int expectVersionCode) {
        if (activity == null) {
            LogUtils.i(getLogTag(), "Activity has been destroyed, cannot show dialog");
            return;
        }
        String tag = "AppUpdateDialog";
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            AppUpdateDialogFragment.newInstance(force, downloadUrl, expectVersionCode)
                    .show(activity.getSupportFragmentManager(), tag);
        } else {
            LogUtils.i(getLogTag(), "Already show AppUpdateDialog");
        }
    }

    public static void downloadApk(AppUpdateDialogFragment dialogFragment, String downloadUrl, int expectVersionCode) {
        Context context = dialogFragment.getActivity();
        if (TextUtils.isEmpty(downloadUrl)) {
            LogUtils.i(getLogTag(), "download url cannot be empty");
            dialogFragment.dismiss();
            return;
        }
        if (!Utility.isExternalStorageAvailable()) {
            LogUtils.i(getLogTag(), "sdcard cannot be accessible, abort download");
            dialogFragment.dismiss();
            return;
        }
        if (isDownloadedCorrectApk(context, expectVersionCode)) {
            LogUtils.i(getLogTag(), "already download complete, install the latest apk now");
            Utility.installApk(context, getApkFilePath(context));
            return;
        }
        int downloadId = DownloadManager.getImpl().startDownload(downloadUrl, getApkFilePath(context), null);
        LogUtils.i(getLogTag(), "Start download " + downloadUrl + ",mDownloadId:" + downloadId);
    }

    /** 判断下载的文件是不是apk，是不是跟本应用包名一样，是不是版本号比本应用的高 */
    public static boolean isDownloadedCorrectApk(Context context, int remoteVersion) {
        boolean downloadFinishedAndCorrect = false;
        StringBuilder logSb = new StringBuilder();
        String path = getApkFilePath(context);
        if (!TextUtils.isEmpty(path)) {
            File apkFile = new File(path);
            if (apkFile.exists()) {
                PackageManager pm = context.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                if (packageInfo != null && packageInfo.packageName.equals(context.getPackageName())) {
                    downloadFinishedAndCorrect = packageInfo.versionCode >= remoteVersion;
                    logSb.append("downloaded apk file version code : " + packageInfo.versionCode + ", remoteVersion:" + remoteVersion).append(" | ");
                }
            }
        } else {
            logSb.append("SDCard is not available").append(" | ");
        }
        if (!downloadFinishedAndCorrect) {
            logSb.append("not download yet");
        }
        LogUtils.i(getLogTag(), logSb.toString());
        return downloadFinishedAndCorrect;
    }

    public static boolean isApkFileExisted(Context context) {
        String path = getApkFilePath(context);
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    public static void setLastRequestTime(Context context, long time) {
        PersistUtils.setLastRequestTime(context, time);
    }

    /** 保存应用更新信息 */
    public static void saveAppUpdateInfo(Context context, String url, int normalVersion, int forceVersion, String androidUpdateInfo) {
        PersistUtils.setApkDownloadUrl(context, url);
        PersistUtils.setNormalUpdateVersion(context, normalVersion);
        PersistUtils.setForceUpdateVersion(context, forceVersion);
        PersistUtils.setUpdateInfo(context, androidUpdateInfo);
    }

    /** 获取Apk下载路径 */
    public static String getApkFilePath(Context context) {
        if (context != null && Utility.isExternalStorageAvailable()) {
            String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String fileName = context.getPackageName() + ".apk";
            return dirPath + File.separator + fileName;
        } else {
            return "";
        }
    }

    /** 获取Apk下载地址 */
    public static String getApkDownloadUrl(Context context) {
        return PersistUtils.getApkDownloadUrl(context);
    }

    /** 获取强制更新的版本号 */
    public static int getForceUpdateVersion(Context context) {
        return PersistUtils.getForceUpdateVersion(context);
    }

    /** 获取普通更新的版本号 */
    public static int getNormalUpdateVersion(Context context) {
        return PersistUtils.getNormalUpdateVersion(context);
    }

    /** 获取上次请求更新的时间点 */
    public static long getLastRequestTime(Context context) {
        return PersistUtils.getLastRequestTime(context);
    }
}