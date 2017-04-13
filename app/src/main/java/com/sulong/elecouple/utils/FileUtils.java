package com.sulong.elecouple.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作类
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static File createTmpImageFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            dir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(dir, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            cacheDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    public static File createTmpVideoFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            dir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "video_" + timeStamp + "";
            File tmpFile = new File(dir, fileName + ".mp4");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            cacheDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "video_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".mp4");
            return tmpFile;
        }
    }
}
