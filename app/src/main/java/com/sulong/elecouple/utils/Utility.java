package com.sulong.elecouple.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sulong.elecouple.ui.activity.SplashActivity;
import com.google.gson.Gson;
import com.sulong.elecouple.BuildConfig;
import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.AllAreaDataItem;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.ui.activity.LoginActivity;
//import com.ah.ui.activity.SplashActivity;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Utility {
    private static final String THIS_DIR = "AGG";
    private static Gson gson = new Gson();

    public static String getLogTag() {
        return Utility.class.getSimpleName();
    }

    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 格式化浮点数
     *
     * @param doubleString 浮点数字符串
     * @param accuracy     保留几位小数
     * @return 格式化后的字符串
     */
    public static String formatDouble(String doubleString, int accuracy) {
        double value = 0f;
        try {
            value = Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatDouble(value, accuracy);
    }

    /**
     * 格式化浮点数
     *
     * @param doubleValue 浮点数值
     * @param accuracy    保留几位小数
     * @return 格式化后的字符串
     */
    public static String formatDouble(double doubleValue, int accuracy) {
        String format = "0";
        if (accuracy > 0) {
            StringBuilder decimalPlaceHolder = new StringBuilder();
            for (int i = 1; i <= accuracy; i++) {
                decimalPlaceHolder.append("0");
            }
            format = "0." + decimalPlaceHolder.toString();
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        String formatString = decimalFormat.format(doubleValue);
        return formatString;
    }

    /**
     * 字符串转整型
     *
     * @param string
     * @return
     */
    public static Integer formatInteger(String string) {
        int value = 0;
        try {
            value = Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return value;
    }


    public static String formatPrice(double doubleValue) {
        return formatDouble(doubleValue, 2);
    }

    public static String formatPrice(String value) {
        return formatDouble(value, 2);
    }

    public static boolean restrictPasswordInput(Context context, Editable s) {
        String inputString = s.toString();
        if (inputString.endsWith(" ")) {
            s.delete(inputString.length() - 1, inputString.length());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 限制价格输入
     *
     * @param s
     * @return true表示对Editable处理过了
     */
    public static boolean restrictPriceInput(Editable s) {
        String inputString = s.toString().trim();
        // 限制第一个数字不能输入0，只能输入0.
        if (inputString.startsWith("0") && inputString.length() >= 2) {
            if ('.' != inputString.charAt(1)) {
                s.delete(0, 1);
                return true;
            }
        }
        // 限制小数点后只能输入两位
        if (inputString.contains(".")
                && inputString.length() - 1 - inputString.indexOf(".") > 2) {
            s.delete(inputString.length() - 1, inputString.length());
            return true;
        }
        // 限制小数点前只能输入七位数
        if (!inputString.contains(".") && inputString.length() > 7) {
            s.delete(inputString.length() - 1, inputString.length());
            return true;
        }
        return false;
    }

    public static Gson getGson() {
        return gson;
    }

    public static String getStrFromByte(byte[] responseBody) {
        String response = null;
        if (responseBody != null) {
            try {
                response = new String(responseBody, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static boolean isNetworkConnected(Context context) {
        boolean result = false;
        int ansPermission = context
                .checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermission = context
                .checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        if (ansPermission == PackageManager.PERMISSION_GRANTED
                && internetPermission == PackageManager.PERMISSION_GRANTED) {
            if (context != null) {
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    int type = networkInfo.getType();
                    switch (type) {
                        case ConnectivityManager.TYPE_MOBILE:
                        case ConnectivityManager.TYPE_WIFI:
                            if (networkInfo.isAvailable()
                                    && networkInfo.isConnected()) {
                                result = true;
                            }
                            break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isGpsOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = false;
        boolean network = false;
        int locatePermission = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int internetPermission = context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        if (locatePermission == PackageManager.PERMISSION_GRANTED) {
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            try {
                gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (internetPermission == PackageManager.PERMISSION_GRANTED) {
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            try {
                network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gps || network;
    }

    public static void setGPS(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    public static void goToApplicationInfoPage(Context context, String packageName) {
        boolean isActivityContext = context instanceof Activity;
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            if (isActivityContext) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            if (isActivityContext) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    public static int getTargetSDKVersion(Context context) {
        int targetSdkVersion = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int targetSdkVersion = getTargetSDKVersion(context);
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    public static String getStoragePath(Context c, String path) {
        return getStoragePath(c) + File.separator + path;
    }

    public static String getStoragePath(Context c) {
        String state = Environment.getExternalStorageState();
        File filesDir;
        Debug.i("ext state =" + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = c.getExternalFilesDir(THIS_DIR);
        } else {
            // Load another directory, probably local memory
            filesDir = c.getFilesDir();
        }

        // Bug will happen on mi3
        if (filesDir == null) {
            filesDir = c.getFilesDir();
        }

        if (filesDir == null) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + THIS_DIR;
        }

        String path = filesDir.getPath();

        Debug.i("getStoragePath:" + path);

        return path;
    }

    /**
     * 获取指定城市有哪些城区
     *
     * @param city 城市名
     * @return 城区
     */
    public static List<String> getDistricts(Context context, String city) {
        if (TextUtils.isEmpty(city)) {
            return null;
        }
        AllAreaDataItem allAreaDataItem = Utility.readCityDataFromFile(context);
        if (allAreaDataItem == null || allAreaDataItem.data == null || allAreaDataItem.data.city_district == null) {
            return null;
        }
        for (AllAreaDataItem.CityDistrict cityDistrict : allAreaDataItem.data.city_district) {
            if (TextUtils.equals(cityDistrict.area_name, city)) {
                List<String> districtList = new ArrayList<>();
                for (AllAreaDataItem.SubArea sub_area : cityDistrict.sub_area) {
                    districtList.add(sub_area.area_name);
                }
                return districtList;
            }
        }
        return null;
    }

    public static AllAreaDataItem readCityDataFromFile(Context context) {
        String fileVersion = PreferenceManager.getDefaultSharedPreferences(context).getString(ConstantUtils.KEY_AREA_VERSION, "");
        String citiesJsonData;
        if (TextUtils.isEmpty(fileVersion)) {
            citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
        } else {
            File file = new File(context.getFilesDir() + File.separator + ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            Log.d("rqy", "CityListActivity--" + file.getAbsolutePath() + "--" + file.exists());
            if (file.exists()) {
                citiesJsonData = Utility.readStringFromApplicationFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            } else {
                citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            }
        }
        if (TextUtils.isEmpty(citiesJsonData)) {
            return null;
        }
        AllAreaDataItem allAreaDateItem = JsonParser.getInstance().fromJson(citiesJsonData, AllAreaDataItem.class);
        return allAreaDateItem;
    }

    public static String readStringFromApplicationFile(Context context, String fileName) {
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readStringFromAssetFile(Context context, String assetPath) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(assetPath), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean wirteJsonToFile(Context context, String jsonData, String filePath) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonData.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String formatTime(Context context, long time) {
        long diff = System.currentTimeMillis() - time;
        String postTimeStr;
        if (diff < 60 * 1000) {
            postTimeStr = context.getString(R.string.post_create_time_just_now);
        } else if (diff < 60 * 60 * 1000) {
            long minute = diff / (60 * 1000);
            postTimeStr = context.getString(R.string.post_create_time_minute, minute + "");
        } else if (diff < 24 * 60 * 60 * 1000) {
            long hour = diff / (60 * 60 * 1000);
            postTimeStr = context.getString(R.string.post_create_time_hour, hour + "");
        } else {
            String format = "M-dd ah:mm";
            if (diff < 24 * 60 * 60 * 1000) { // 今天
                format = "ah:mm";
            } else if (diff < 2 * 24 * 60 * 60 * 1000) { // 昨天
                format = context.getString(R.string.yesterday_time);
            } else if (diff < 7 * 24 * 60 * 60 * 1000) {
                format = "E ah:mm";
            }
            postTimeStr = new SimpleDateFormat(format).format(new Date(time));
        }
        return postTimeStr;
    }

    public static String formatTime(long time, String format) {
        //php时间戳10位
        if (time < 9999999999.00) {
            return new SimpleDateFormat(format, Locale.CHINA).format(new Date(time * 1000));
        }
        return new SimpleDateFormat(format, Locale.CHINA).format(new Date(time));
    }

    public static String formatBigNumber(Context context, long countNumber) {
        if (countNumber < 10000) {
            return countNumber + "";
        } else if (countNumber < 10000 * 10000) {
            double tenThousand = 1.0 * countNumber / 10000;
            return context.getString(R.string.post_number_ten_thousand, tenThousand);
        } else {
            double hundredMillion = 1.0 * countNumber / 10000 / 10000;
            return context.getString(R.string.post_number_hundred_million, hundredMillion);
        }
    }

    public static void loadImg(Context context, String url, ImageView view, int defaultImg) {
        loadImg(context, url, defaultImg, view);
    }

    public static void loadImg(Context context, String url, ImageView view, int defaultImg, Callback callback) {
        loadImg(context, url, defaultImg, view, callback);
    }

    public static void loadImg(Context context, String url, ImageView view) {
        loadImg(context, url, R.drawable.default_label_square, view);
    }

    public static void loadImg(Context context, String url, ImageView view, Callback callback) {
        loadImg(context, url, R.drawable.default_label_square, view, callback);
    }

    public static void loadImg(Context context, String url, int placeholder, ImageView view) {
        loadImg(context, url, placeholder, view, null);
    }

    public static void loadImg(Context context, String url, int placeholder, ImageView view, Callback callback) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            Picasso.with(context).load(url).placeholder(placeholder).fit().config(Bitmap.Config.RGB_565).into(view, callback);
        } else {
            view.setImageResource(placeholder);
        }
    }

    public static Bitmap loadImg(Context context, String url) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            try {
                bitmap = Picasso.with(context).load(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void loadImg(ImageView imageView, String url) {
        loadImg(imageView, url, R.drawable.default_label);
    }

    public static void loadImg(ImageView imageView, String url, int loadingImage) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            DisplayImageOptions options = getDefaultDisplayImageOptionsBuilder()
                    .showImageOnLoading(loadingImage)
                    .showImageForEmptyUri(loadingImage)
                    .showImageOnFail(loadingImage)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(url, imageView, options);
        } else {
            imageView.setImageResource(loadingImage);
        }
    }

    public static void loadImg(ImageView imageView, String url, Drawable loadingImage) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            DisplayImageOptions options = getDefaultDisplayImageOptionsBuilder()
                    .showImageOnLoading(loadingImage)
                    .showImageForEmptyUri(loadingImage)
                    .showImageOnFail(loadingImage)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(url, imageView, options);
        } else {
            imageView.setImageDrawable(loadingImage);
        }
    }

    public static void loadImgForFile(ImageView imageView, String filePathWithNoPrefix) {
        if (!TextUtils.isEmpty(filePathWithNoPrefix)
                && !TextUtils.isEmpty(filePathWithNoPrefix.trim())) {
            loadImg(imageView, "file://" + filePathWithNoPrefix);
        } else {
            imageView.setImageResource(R.drawable.default_label);
        }
    }

    public static DisplayImageOptions.Builder getDefaultDisplayImageOptionsBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions
                .Builder()
                .showImageOnLoading(R.drawable.default_label_square)
                .showImageForEmptyUri(R.drawable.default_label_square)
                .showImageOnFail(R.drawable.default_label_square)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565);
        return builder;
    }

    public static Bitmap resizeImage(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;

        Debug.i("outWidth=" + outWidth + ",outHeight" + outHeight);
        if (outWidth != 0 && outHeight != 0 && reqWidth != 0 && reqHeight != 0) {

            int inSampleSize = 1;
            if (outHeight > reqHeight || outWidth > reqWidth) {
                final int heightRatio = Math.round((float) outHeight / (float) reqHeight);
                final int widthRatio = Math.round((float) outWidth / (float) reqWidth);
                Debug.i("widthRatio=" + widthRatio + ",heightRatio" + heightRatio);
                inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
                options.inSampleSize = inSampleSize;
            }

            Debug.i("options.inSampleSize=" + options.inSampleSize);
        }

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    public static void savePic(Bitmap bitmap, String name, Context context, Bitmap.CompressFormat ex) {
        name = name + getEx(ex);
        File tempFile = new File(getStoragePath(context) + "/" + name);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        FileOutputStream fOut = null;
        try {
            tempFile.createNewFile();
            fOut = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.compress(ex, 80, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveImgToGallery(Context context, String imgName) {
        String filePath = Utility.getStoragePath(context) + "/" + imgName + ".png";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (!sdCardExist)
            return false;
        try {
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", "image/png");
            values.put("_data", filePath);
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{new File(filePath).getParentFile().getAbsolutePath()}, null, null);
        return true;
    }

    public static String getEx(Bitmap.CompressFormat ex) {
        String exd = null;
        if (ex == Bitmap.CompressFormat.WEBP) {
            exd = ".webp";
        } else if (ex == Bitmap.CompressFormat.PNG) {
            exd = ".png";
        } else if (ex == Bitmap.CompressFormat.JPEG) {
            exd = ".jpg";
        }

        return exd;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Debug.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass()
                        .getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Debug.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Debug.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    public static String getVersionName(Context mContext) {
        String version = "";
        PackageInfo pInfo;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = pInfo.versionName + "(" + pInfo.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (version != null && !version.contains("v")) {
            version = "v" + version;
        }
        return version;
    }

    public static final int getVersionCode(Context context) {
        int verCode = 0;
        try {
            PackageInfo appInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            verCode = appInfo.versionCode;
        } catch (Exception e) {
        }
        return verCode;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

    /**
     * 距离数值转换为字符串
     *
     * @param distance 距离，单位米
     * @return 距离字符串
     */
    public static String formatDistance(long distance) {
        String result = null;
        if (distance < 100) {
            result = "<100m";
        } else if (distance < 500) {
            result = "<500m";
        } else {
            result = String.format("%.1fkm", distance * 1.0 / 1000);
        }
        return result;
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getRawVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            memoryClass = am.getLargeMemoryClass();
        }
        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass / 7;
    }

    public static void createAppShortcut() {
        Context appContext = LocationApplication.getInstance();
        //创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appContext.getString(R.string.app_name));
        //快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(appContext, R.drawable.ic_launcher);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        //点击快捷图片，运行的程序主入口
        Intent launcherIntent = new Intent(appContext, SplashActivity.class);
        launcherIntent.setAction(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        //发送广播
        appContext.sendBroadcast(shortcutIntent);
    }

    public static boolean isMIUI() {
        final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    public static boolean isFlyme() {
        if ("Meizu".equalsIgnoreCase(Build.MANUFACTURER)
                || "Meizu".equalsIgnoreCase(Build.BRAND)) {
            return true;
        }
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static int parseInt(String intString, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(intString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static long parseLong(String longString, long defaultValue) {
        long value = defaultValue;
        try {
            value = Long.parseLong(longString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static float parseFloat(String floatString, float defaultValue) {
        float value = defaultValue;
        try {
            value = Float.parseFloat(floatString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static double parseDouble(String floatString, double defaultValue) {
        double value = defaultValue;
        try {
            value = Double.parseDouble(floatString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String formatInterval(final long intervalInMills) {
        final long hr = TimeUnit.MILLISECONDS.toHours(intervalInMills);
        final long min = TimeUnit.MILLISECONDS.toMinutes(intervalInMills - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(intervalInMills - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(intervalInMills - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }

    public static String formatMinuteAndSeconds(final long intervalInMills) {
        final long min = TimeUnit.MILLISECONDS.toMinutes(intervalInMills);
        final long sec = TimeUnit.MILLISECONDS.toSeconds(intervalInMills - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Check if user has logined.
     *
     * @param context
     * @return true is logined.
     */
    public static boolean checkLogin(Context context) {
        if (!LoginManager.getInstance().isLogin()) {
            Intent loginIntent = new Intent();
            loginIntent.setClass(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return false;
        } else {
            return true;
        }

    }

    public static String getRadius(Context context) {
        SharedPreferences locationSp = context.getSharedPreferences(ConstantUtils.GPS_PREF, 0);
        String radius = locationSp.getString(ConstantUtils.PREF_KEY_RADIUS, ConstantUtils.DEFAULT_RADIUS);
        return radius;
    }

    public static String getLat(Context context) {
        SharedPreferences locationSp = context.getSharedPreferences(ConstantUtils.GPS_PREF, 0);
        String lat = locationSp.getString(ConstantUtils.PREF_KEY_LATITUDE, ConstantUtils.DEFAULT_LAT);
        return lat;
    }

    public static String getLong(Context context) {
        SharedPreferences locationSp = context.getSharedPreferences(ConstantUtils.GPS_PREF, 0);
        String lng = locationSp.getString(ConstantUtils.PREF_KEY_LONGITUDE, ConstantUtils.DEFAULT_LNG);
        return lng;
    }

    public static String getGpsParam(Context context, String key) {
        SharedPreferences locationSp = context.getSharedPreferences(ConstantUtils.GPS_PREF, 0);
        String out = locationSp.getString(key, "");
        return out;
    }

    public static String addParamToUrl(String url, RequestParams webParam) {
        try {
            if (!TextUtils.isEmpty(url)) {
                String urlWithNoParam = url;
                // 去除重复参数的基本参数
                int paramStartIndex = url.indexOf("?");
                if (paramStartIndex > -1) {
                    urlWithNoParam = url.substring(0, paramStartIndex);
                    String paramSuffix = url.substring(paramStartIndex + 1);
                    String[] params = paramSuffix.split("&");
                    for (String param : params) {
                        String[] kv = param.split("=");
                        String key = kv[0];
                        String value = "";
                        if (kv.length == 2) {
                            value = kv[1];
                        }
                        if (!TextUtils.isEmpty(value)) {
                            webParam.put(key, value);
                        }
                    }
                }
                urlWithNoParam += "?" + webParam.toString();
                return urlWithNoParam;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String filterPhoneNumber(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            phone = phone.replaceAll(" ", "");
            phone = phone.replaceAll("-", "");
            phone = phone.replaceAll("\\+86", "");
        }
        return phone;
    }

    public static boolean isPhoneNumber(String mobiles) {
        return !StringUtils.isEmpty(mobiles) && mobiles.matches("[1]\\d{10}");
    }

    public static boolean isApplicationRunningInOtherProcess(Context context) {
        int pid = android.os.Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        if (l != null) {
            Iterator i = l.iterator();
            while (i.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
                try {
                    if (info.pid == pid) {
                        processName = info.processName;
                    }
                } catch (Exception e) {
                }
            }
        }
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
        if (processName == null || !processName.equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
            Debug.e(getLogTag(), "enter the service process!");
            // 则此application::onCreate 是被service 调用的
            return true;
        }
        return false;
    }

    public static String getVideoFileThumbnail(Context context, String videoFilePath) {
        StringBuilder logSb = new StringBuilder();
        String thumbPath = null;
        // 从数据库中查找
        Uri videoUri = MediaStore.Video.Thumbnails.getContentUri("external");
        String[] projection = new String[]{
                MediaStore.Video.Media._ID, // 0
                MediaStore.Video.Media.DATA, // 1
        };
        String selection = MediaStore.Video.Thumbnails.VIDEO_ID + " In ( select _id from video where " + MediaStore.Video.Media.DATA + " = ?)";
        String[] selectionArgs = new String[]{videoFilePath};
        Cursor c = context.getContentResolver().query(videoUri, projection, selection, selectionArgs, null);
        if (c != null && c.moveToFirst()) {
            thumbPath = c.getString(1);
            c.close();
            logSb.append("getVideoFileThumbnailPath:").append(thumbPath).append("\n");
        } else {
            logSb.append("getVideoFileThumbnail failed for ").append(videoFilePath).append("\n");
        }
        Debug.li(getLogTag(), logSb.toString());
        return thumbPath;
    }

    public static boolean generateVideoFileThumbnail(String videoPath, String thumbPath) {
        File thumbFile = new File(thumbPath);
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        StringBuilder logSb = new StringBuilder();
        logSb.append("thumbPath:").append(thumbPath).append("\n");
        logSb.append("thumbFile:").append(thumbFile).append("\n");
        logSb.append("thumbFile.getParentFile:").append(thumbFile.getParentFile()).append("\n");
        try {
            if (!thumbFile.getParentFile().exists()) {
                thumbFile.getParentFile().mkdirs();
            }
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
            fos = new FileOutputStream(thumbFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
            Debug.li(getLogTag(), logSb.toString());
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
                return true;
            } else {
                return false;
            }
        }
    }

    public static Bitmap getCircleBitmap(Bitmap bmp, int destWidth, int destHeight) {
        // 切割成圆形
        Bitmap bitmap = Bitmap.createScaledBitmap(bmp, destWidth, destHeight, false);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.TRANSPARENT);
        paint.setColor(0xff424242);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static View getItemViewByPosition(ListView listView, int pos) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos >= firstListItemPosition && pos <= lastListItemPosition) {
            return listView.getChildAt(pos - firstListItemPosition);
        } else {
            return null;
        }
    }

    public static void shareToWhere(Activity context, String platform, String title, String content, String imgUrl, String shareUrl) {
        Debug.l("Share", "share... platform " + platform + " title " + title + " content " + content + " imgUrl " + imgUrl + " shareUrl " + shareUrl);
        switch (platform) {
            case "微信":
                ShareUtils.SharePlatform(context, SHARE_MEDIA.WEIXIN, title, content, shareUrl, imgUrl);
                break;
            case "朋友圈":
                ShareUtils.SharePlatform(context, SHARE_MEDIA.WEIXIN_CIRCLE, title, content, shareUrl, imgUrl);
                break;
            case "QQ":
                ShareUtils.SharePlatform(context, SHARE_MEDIA.QQ, title, content, shareUrl, imgUrl);
                break;
            case "QQ空间":
                ShareUtils.SharePlatform(context, SHARE_MEDIA.QZONE, title, content, shareUrl, imgUrl);
                break;
            case "微博":
                ShareUtils.SharePlatform(context, SHARE_MEDIA.SINA, title, content, shareUrl, imgUrl);
                break;
            default:
                break;
        }
    }

    public static void appendIconInTextview(Context context, TextView textView, String content, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，[smile]是需要被替代的文本
        SpannableString spannable = new SpannableString(content + "  [smile]");
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        spannable.setSpan(span, content.length() + 2, content.length() + "  [smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    public static void appendIconInTextviewWithSpan(Context context, TextView textView, SpannableStringBuilder content, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        content.setSpan(span, content.length()-1, content.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    public static String getGoodsSpec(Map<String, String> goods_spec) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : goods_spec.entrySet()) {
            sb.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append("  ");
        }
        return sb.toString();
    }

}