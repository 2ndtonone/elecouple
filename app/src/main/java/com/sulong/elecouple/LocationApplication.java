package com.sulong.elecouple;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.SparseArray;

import com.sulong.elecouple.dagger.ModuleProvider;
import com.sulong.elecouple.dagger.component.AppComponent;
import com.sulong.elecouple.dagger.component.DaggerAppComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.dagger.module.AppModule;
import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.Utility;
import com.sulong.elecouple.web.AhAsyncHttpClient;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.social.UmengShareUtils;
import com.umeng.socialize.UMShareAPI;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LocationApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static LocationApplication instance;
    boolean isLocating = false;
    private RefWatcher mRefWatcher;
    private Bitmap snsBitmap;
    private String base64String;
    private SparseArray<String> mLocTypeDescMap;

    public static LocationApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 下载工具
        FileDownloadLog.NEED_LOG = true;
        try {
            FileDownloader.init(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化Logger打印工具
        Logger.init(getString(R.string.app_name)).hideThreadInfo().methodOffset(1);
        // 防止重复调用onCreate里的初始化代码
        if (enableDetectMultipleRun() && Utility.isApplicationRunningInOtherProcess(this)) {
            Debug.li(getLogTag(), "onCreate invoked by other process, avoid to init all tools settings again");
            return;
        }
        printLog();

        // 给外部引用自己
        instance = this;

        // 正式版拦截未捕获的异常
        if (!BuildConfig.BUILD_TYPE.equals("debug")) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
        // 控制async-http的日志打印
        AhAsyncHttpClient.getInstance().setLoggingEnabled(BuildConfig.PRINT_LOG);
        // 控制自定义的的日志打印
        Debug.enable(BuildConfig.PRINT_LOG, true);
        // 友盟分享初始化
        initUmengSDK();

        // 初始化内存泄露检测工具
        initLeakCanary();
        // 初始化图片加载工具
        initImageLoader();
        // 创建桌面快捷方式
        createAppShortcut();

        // 创建全局的Dagger Component
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(provideAppModule())
                .build();
        ComponentHolder.setAppComponent(appComponent);
    }

    protected AppModule provideAppModule() {
        return getModuleProvider().provideAppModule(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void printLog() {
        StringBuilder logSb = new StringBuilder();
        logSb.append("onCreate()").append("\n");
        logSb.append("BuildConfig.SERVER_TYPE:" + BuildConfig.SERVER_TYPE).append("\n");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            logSb.append("CPU_ABI:" + Build.CPU_ABI).append("\n");
        } else {
            String[] abis = Build.SUPPORTED_ABIS;
            logSb.append("SUPPORTED_ABIS:");
            for (String abi : abis) {
                logSb.append(abi + "|");
            }
            logSb.deleteCharAt(logSb.length() - 1).append("\n");
        }
        Debug.li(getLogTag(), logSb.toString());
    }

    protected boolean enableDetectMultipleRun() {
        return true;
    }

    protected void initUmengSDK() {
        UmengShareUtils.initialize(this);
        UMShareAPI.get(this);
    }

    protected void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.vendor_details_default)
                .showImageForEmptyUri(R.drawable.vendor_details_default)
                .showImageOnFail(R.drawable.vendor_details_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        int memoryCacheSize = Utility.calculateMemoryCacheSize(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(memoryCacheSize))
                .memoryCacheSize(memoryCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
        L.writeLogs(false);
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public ModuleProvider getModuleProvider() {
        return ModuleProvider.getInstance();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        exit();
    }

    /**
     * Try to send a report, if an error occurs stores a report file for a later
     * attempt.
     *
     * @param e The Throwable to be reported. If null the report will contain
     *          a new Exception("Report requested by developer").
     */
    void handleException(Throwable e) {
        if (e == null) {
            e = new Exception("Report requested by developer");
        }

        MobclickAgent.reportError(this, e);

        // Build stack trace
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        final String causeString = result.toString();
        printWriter.close();
        Debug.fi("EXCEPTION", causeString);
        e.printStackTrace();
    }

    public Bitmap getSnsBitmap() {
        return snsBitmap;
    }

    public void setSnsBitmap(Bitmap snsBitmap) {
        if (this.snsBitmap != null) {
            this.snsBitmap.recycle();
        }
        this.snsBitmap = snsBitmap;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public void exit() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ConstantUtils.ACTION_CLOSE_ALL_PAGE));
        MobclickAgent.onKillProcess(this);
        System.exit(0);
    }

    private void createAppShortcut() {
        if (Utility.isMIUI()) {
            return;
        }
        final String PREF_NAME = "shortcut";
        final String PREF_KEY_HAS_CREATED_SHORTCUT = "has_created_shortcut";
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean hasCreatedShortcut = prefs.getBoolean(PREF_KEY_HAS_CREATED_SHORTCUT, false);
        if (!hasCreatedShortcut) {
            Utility.createAppShortcut();
            prefs.edit().putBoolean(PREF_KEY_HAS_CREATED_SHORTCUT, true).apply();
        }
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    public AppComponent getAppComponent() {
        return ComponentHolder.getAppComponent();
    }
}