package com.sulong.elecouple.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class BrowserHelper {
    public static void choiceBrowserToVisitUrl(String url, Context context) {
        boolean existUC = false, existOpera = false, existQQ = false, existDolphin = false, existSkyfire = false, existSteel = false, existGoogle = false;
        String ucPath = "", operaPath = "", qqPath = "", dolphinPath = "", skyfirePath = "", steelPath = "", googlePath = "";
        PackageManager packageMgr = context.getPackageManager();
        List<PackageInfo> list = packageMgr.getInstalledPackages(0);
        for (int i = 0; i < list.size(); i++) {
            PackageInfo info = list.get(i);
            String temp = info.packageName;
            if (temp.equals("com.uc.browser")) {
                // 存在UC
                ucPath = temp;
                existUC = true;
            } else if (temp.equals("com.tencent.mtt")) {
                // 存在QQ
                qqPath = temp;
                existQQ = true;
            } else if (temp.equals("com.opera.mini.android")) {
                // 存在Opera
                operaPath = temp;
                existOpera = true;
            } else if (temp.equals("mobi.mgeek.TunnyBrowser")) {
                dolphinPath = temp;
                existDolphin = true;
            } else if (temp.equals("com.skyfire.browser")) {
                skyfirePath = temp;
                existSkyfire = true;
            } else if (temp.equals("com.kolbysoft.steel")) {
                steelPath = temp;
                existSteel = true;
            } else if (temp.equals("com.android.browser")) {
                // 存在GoogleBroser
                googlePath = temp;
                existGoogle = true;
            }
        }
        if (existUC) {
            gotoUrl(ucPath, url, packageMgr, context);
        } else if (existOpera) {
            gotoUrl(operaPath, url, packageMgr, context);
        } else if (existQQ) {
            gotoUrl(qqPath, url, packageMgr, context);
        } else if (existDolphin) {
            gotoUrl(dolphinPath, url, packageMgr, context);
        } else if (existSkyfire) {
            gotoUrl(skyfirePath, url, packageMgr, context);
        } else if (existSteel) {
            gotoUrl(steelPath, url, packageMgr, context);
        } else if (existGoogle) {
            gotoUrl(googlePath, url, packageMgr, context);
        } else {
            doDefault(context, url);
        }
    }

    public static void doDefault(Context context, String visitUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void gotoUrl(String packageName, String url,
                               PackageManager packageMgr, Context context) {
        try {
            Intent intent;
            intent = packageMgr.getLaunchIntentForPackage(packageName);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            // 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
            e.printStackTrace();
        }
    }

    /** 直接启动UC，用于验证测试。 */
    private void showUCBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /** 直接启动QQ，用于验证测试。 */
    private void showQQBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /** 直接启动Opera，用于验证测试。 */
    private void showOperaBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.opera.mini.android",
                "com.opera.mini.android.Browser");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /** 直接启动Dolphin Browser，用于验证测试。 */
    private void showDolphinBrowser(Context context, String visitUrl) {
        // 方法一：
        // Intent intent = new Intent();
        // intent.setClassName("mobi.mgeek.TunnyBrowser",
        // "mobi.mgeek.TunnyBrowser.BrowserActivity");
        // intent.setAction(Intent.ACTION_VIEW);
        // intent.addCategory(Intent.CATEGORY_DEFAULT);
        // intent.initListViewData(Uri.parse(visitUrl));
        // startActivity(intent);
        // 方法二：
        gotoUrl("mobi.mgeek.TunnyBrowser", visitUrl, context.getPackageManager(), context);
    }

    /** 直接启动Skyfire Browser，用于验证测试。 */
    private void showSkyfireBrowser(Context context, String visitUrl) {
        // 方法一：
        Intent intent = new Intent();
        intent.setClassName("com.skyfire.browser",
                "com.skyfire.browser.core.Main");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
        // 方法二：
        // gotoUrl("com.skyfire.browser", visitUrl, getPackageManager());

    }

    /** 直接启动Steel Browser，用于验证测试。 */
    private void showSteelBrowser(Context context, String visitUrl) {
        // 方法一：
        // Intent intent = new Intent();
        // intent.setClassName("com.kolbysoft.steel",
        // "com.kolbysoft.steel.Steel");
        // intent.setAction(Intent.ACTION_VIEW);
        // intent.addCategory(Intent.CATEGORY_DEFAULT);
        // intent.initListViewData(Uri.parse(visitUrl));
        // startActivity(intent);
        // 方法二：
        gotoUrl("com.kolbysoft.steel", visitUrl, context.getPackageManager(), context);
    }
}
