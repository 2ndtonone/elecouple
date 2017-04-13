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

import com.sulong.elecouple.BuildConfig;
import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.ModuleProvider;
import com.sulong.elecouple.dagger.component.AppComponent;
import com.sulong.elecouple.dagger.component.DaggerAppComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.dagger.module.AppModule;
import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.eventbus.LocateFinishEvent;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.PersistUtils;
import com.sulong.elecouple.utils.Utility;
import com.sulong.elecouple.web.AhAsyncHttpClient;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
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

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class LocationApplication extends Application implements Thread.UncaughtExceptionHandler {
    private static LocationApplication instance;
    public LocationClient mLocationClient;
    boolean isLocating = false;
    LocationInfo mNewestLocationInfo;
    private RefWatcher mRefWatcher;
    private Bitmap snsBitmap;
    private String base64String;
    private SparseArray<String> mLocTypeDescMap;
    public BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocationClient.stop();
            isLocating = false;

            boolean isAcquiredLocation = false;
            // >>>>>>>>> 打印定位结果 >>>>>>>>>
            StringBuilder logSb = new StringBuilder();
            logSb.append("定位结果如下\n");
            int locType = bdLocation.getLocType();
            String locTypeDesc = mLocTypeDescMap.get(locType, null);
            if (TextUtils.isEmpty(locTypeDesc)) {
                if (locType >= 162 && locType <= 167) {
                    locTypeDesc = "请将错误码、imei和定位时间反馈至loc-bugs@baidu.com，以便我们跟进追查问题";
                } else if (locType >= 501 && locType <= 700) {
                    locTypeDesc = "key验证失败，请按照说明文档重新申请KEY";
                } else {
                    locTypeDesc = "不能定位，请记住这个返回值，并到百度LBS开放平台论坛Andriod定位SDK版块中进行交流";
                }
            }
            logSb.append(String.format("LocType : %1$d - %2$s\n", locType, locTypeDesc));
            logSb.append(String.format("Time : %1$s\n", bdLocation.getTime()));
            logSb.append(String.format("CoordinateType : %1$s\n", bdLocation.getCoorType()));
            logSb.append(String.format("Latitude : %1$s\n", bdLocation.getLatitude()));
            logSb.append(String.format("Longitude : %1$s\n", bdLocation.getLongitude()));
            logSb.append(String.format("Radius : %1$s\n", bdLocation.getRadius()));
            logSb.append(String.format("Address : %1$s\n", bdLocation.getAddrStr()));
            logSb.append(String.format("City : %1$s\n", bdLocation.getCity()));
            logSb.append(String.format("District : %1$s\n", bdLocation.getDistrict()));
            logSb.append(String.format("Street : %1$s\n", bdLocation.getStreet()));
            logSb.append(String.format("StreetNumber : %1$s\n", bdLocation.getStreetNumber()));
            logSb.append(String.format("LocationDescribe : %1$s\n", bdLocation.getLocationDescribe()));
            switch (bdLocation.getLocType()) {
                case BDLocation.TypeGpsLocation:
                    logSb.append(String.format("Speed : %1$skm/h\n", bdLocation.getSpeed())); // 单位：公里每小时
                    logSb.append(String.format("Satellite number : %1$d\n", bdLocation.getSatelliteNumber()));
                    logSb.append(String.format("Altitude : %1$sm\n", bdLocation.getAltitude())); // 单位：米
                    logSb.append(String.format("Direction : %1$s\n", bdLocation.getDirection()));
                    logSb.append(String.format("Description : %1$s\n", "gps定位成功"));
                    isAcquiredLocation = true;
                    break;
                case BDLocation.TypeNetWorkLocation:
                    logSb.append(String.format("Operators : %1$s\n", bdLocation.getOperators())); // 运营商信息
                    logSb.append(String.format("Description : %1$s\n", "网络定位成功"));
                    isAcquiredLocation = true;
                    break;
                case BDLocation.TypeOffLineLocation:
                    logSb.append(String.format("Description : %1$s\n", "离线定位成功，离线定位结果也是有效的"));
                    isAcquiredLocation = true;
                    break;
            }
            logSb.append("isAcquiredLocation:" + isAcquiredLocation);
            Debug.li(getLogTag(), logSb.toString());
            // <<<<<<<<< 打印定位结果 <<<<<<<<<

            SharedPreferences sp = getSharedPreferences(ConstantUtils.GPS_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (TextUtils.isEmpty(sp.getString(ConstantUtils.PREF_KEY_CITY_NAME, ""))) {
                editor.putString(ConstantUtils.PREF_KEY_CITY_NAME, bdLocation.getCity()).apply();
            }
            LocationInfo newestLocationInfo = null;
            if (isAcquiredLocation) {
                // 保存定位信息
                PersistUtils.setLastLocateTime(LocationApplication.this, System.currentTimeMillis());
                editor.putString(ConstantUtils.PREF_KEY_LATITUDE, String.valueOf(bdLocation.getLatitude()))
                        .putString(ConstantUtils.PREF_KEY_LONGITUDE, String.valueOf(bdLocation.getLongitude()))
                        .putString(ConstantUtils.PREF_KEY_GPS_CITY_NAME, bdLocation.getCity())
                        .putString(ConstantUtils.PREF_KEY_ADDRESS, bdLocation.getAddrStr())
                        .putString(ConstantUtils.PREF_KEY_LOCATION_DESCRIBE, bdLocation.getLocationDescribe())
                        .putString(ConstantUtils.PREF_KEY_GPS_DISTRICT, bdLocation.getDistrict())
                        .putString(ConstantUtils.PREF_KEY_RADIUS, String.valueOf(bdLocation.getRadius()))
                        .putString(ConstantUtils.PREF_KEY_GPS_PROVINCE, bdLocation.getProvince())
                        .apply();

                // 暂存最新的定位结果
                newestLocationInfo = new LocationInfo();
                newestLocationInfo.gpsCityName = bdLocation.getCity();
                newestLocationInfo.address = bdLocation.getAddrStr();
                newestLocationInfo.gpsDistrict = bdLocation.getDistrict();
                newestLocationInfo.province = bdLocation.getProvince();
                newestLocationInfo.latitude = String.valueOf(bdLocation.getLatitude());
                newestLocationInfo.longitude = String.valueOf(bdLocation.getLongitude());
                newestLocationInfo.radius = String.valueOf(bdLocation.getRadius());
            }
            mNewestLocationInfo = newestLocationInfo;
            Debug.li(getLogTag(), "newestLocationInfo:" + newestLocationInfo);

            // 发送通知
            EventBus.getDefault().post(new LocateFinishEvent(isAcquiredLocation, newestLocationInfo));
        }
    };

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

        // 初始化极光推送
        initJpushSDK();

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
        // 初始化百度地图
        initLbs();
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

    protected void initJpushSDK() {
        JPushInterface.setDebugMode(!TextUtils.equals(BuildConfig.BUILD_TYPE, "release"));    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
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

    public void startLocate() {
        isLocating = true;
        mLocationClient.start();
    }

    public boolean isLocating() {
        return isLocating;
    }

    public void initLbs() {
        SDKInitializer.initialize(getApplicationContext());
        mLocTypeDescMap = new SparseArray<>();
        mLocTypeDescMap.put(BDLocation.TypeGpsLocation, "GPS定位结果，GPS定位成功");
        mLocTypeDescMap.put(BDLocation.TypeCriteriaException, "无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位");
        mLocTypeDescMap.put(BDLocation.TypeNetWorkException, "网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位");
        mLocTypeDescMap.put(BDLocation.TypeCacheLocation, "定位缓存的结果");
        mLocTypeDescMap.put(BDLocation.TypeOffLineLocation, "离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果");
        mLocTypeDescMap.put(BDLocation.TypeOffLineLocationFail, "离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果");
        mLocTypeDescMap.put(BDLocation.TypeOffLineLocationNetworkFail, "网络连接失败时，查找本地离线定位时对应的返回结果");
        mLocTypeDescMap.put(BDLocation.TypeNetWorkLocation, "网络定位结果，网络定位定位成功");
        mLocTypeDescMap.put(162, "请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件");
        mLocTypeDescMap.put(BDLocation.TypeServerError, "服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位");
        mLocTypeDescMap.put(502, "key参数错误，请按照说明文档重新申请KEY");
        mLocTypeDescMap.put(505, "key不存在或者非法，请按照说明文档重新申请KEY");
        mLocTypeDescMap.put(601, "key服务被开发者自己禁用，请按照说明文档重新申请KEY");
        mLocTypeDescMap.put(602, "key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY");

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mBDLocationListener);

        // 配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setAddrType("all");
        option.setProdName(getPackageName());
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public LocationInfo getNewestLocationInfo() {
        return mNewestLocationInfo;
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