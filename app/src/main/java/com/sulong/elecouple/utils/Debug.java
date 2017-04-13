package com.sulong.elecouple.utils;

import android.os.Environment;
import android.util.Log;

import com.hk.lib.appupdate.LogUtils;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.orhanobut.logger.Logger;
import com.umeng.social.UmengShareUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {
    public static final String TAG = "Debug";
    public static final int PARENT_STACK_LEVEL = 3;
    private static final int LOG_FILE_MAX_SIZE = 20 * 1024 * 1024; // 20M
    private static final boolean ENABLE_VERBOSE = true;
    private static final boolean ENABLE_DEBUG = true;
    private static final boolean ENABLE_INFO = true;
    private static final boolean ENABLE_WARN = true;
    private static final boolean ENABLE_ERROR = true;
    private static final boolean ENABLE_LOG_META = true;
    private static final boolean ENABLE_LOG_TRACE_INFO = false;
    private static final String DEFAULT_TAG = "DEBUG";
    public static boolean DEBUG_REGISTER = false;
    public static int DEBUG_NONE = -1;
    public static int DEBUG_INFO = 0;
    public static int DEBUG_WARN = 1;
    public static int DEBUG_ERROR = 2;
    /**
     * -1 不打印log 0 打印所有log 1 打印警告以上的log 2 打印错误的log
     */
    public static int DEBUG_LEVEL = DEBUG_INFO;
    /**
     * 是否写日志到文件
     */
    public static boolean DEBUG_FILE = true;
    /**
     * 根据调用栈关系动态调整STACK_LEVEL的值；
     * 如 0 为最底层，那么打印的将是 Debug.java _FUNC_；
     * 如 1 为直接调用者的信息；
     */
    public static int STACK_LEVEL = 2;
    private static boolean ENABLE_LOG = false;
    private static boolean ENABLE_TRACE = true;
    private static boolean ENABLE_ASSERT = true;
    private static boolean ENABLE_DEBUG_LOCK = true;

    public static void ASSERT_NOT_NULL(final Object obj) {
        if (ENABLE_ASSERT && obj == null) {
            LOG(DEFAULT_TAG, ">>> `NOT NULL` ASSERTION FAILED <<<", 0, 'e');
        }
    }

    public static void ASSERT_NULL(final Object obj) {
        if (ENABLE_ASSERT && obj != null) {
            LOG(DEFAULT_TAG, ">>> `NULL` ASSERTION FAILED <<<", 0, 'e');
        }
    }

    public static void ASSERT_RUN_ON_THREAD(final Thread thread) {
        if (ENABLE_ASSERT && thread != null && Thread.currentThread().getId() != thread.getId()) {
            LOG(DEFAULT_TAG, ">>> `RUN ON THREAD` ASSERTION FAILED <<<", 0, 'e');
        }
    }

    public static void ASSERT_TRUE(final boolean cond) {
        if (ENABLE_ASSERT && !cond) {
            LOG(DEFAULT_TAG, ">>> `TRUE` ASSERTION FAILED <<<", 0, 'e');
        }
    }

    public static void DEBUG(final String msg) {
        if (ENABLE_DEBUG) {
            LOG(DEFAULT_TAG, msg, 0, 'd');
        }
    }

    public static void DEBUG_LOCK(final String msg) {
        if (ENABLE_DEBUG_LOCK) {
            LOG(DEFAULT_TAG, "LOCK#" + msg, 0, 'v');
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG_LEVEL > DEBUG_NONE) {
            Log.e(tag, _FUNC_() + message);
        }
    }

    public static void enable(boolean enable) {
        UmengShareUtils.enableLog(enable);
        DEBUG_LEVEL = enable ? DEBUG_INFO : DEBUG_NONE;
        DEBUG_FILE = enable;
        enableM(enable);
        LogUtils.enableLog(enable);
        FileDownloadLog.NEED_LOG = enable;
    }

    public static void enable(boolean logcatEnable, boolean fileLogEnable) {
        UmengShareUtils.enableLog(logcatEnable);
        DEBUG_LEVEL = logcatEnable ? DEBUG_INFO : DEBUG_NONE;
        enableM(logcatEnable);
        DEBUG_FILE = fileLogEnable;
        LogUtils.enableLog(logcatEnable);
        FileDownloadLog.NEED_LOG = logcatEnable;
    }

    public static boolean isLogcatEnable() {
        return DEBUG_LEVEL >= DEBUG_INFO;
    }

    public static void enableM(boolean enable) {
        ENABLE_LOG = enable;
        ENABLE_TRACE = enable;
        ENABLE_ASSERT = enable;
        ENABLE_DEBUG_LOCK = enable;
        LogUtils.enableLog(enable);
    }

    public static void ERROR(final String msg) {
        if (ENABLE_ERROR) {
            LOG(DEFAULT_TAG, msg, 0, 'e');
        }
    }

    /**
     * 把日志写到文件
     *
     * @param tag
     * @param message
     */
    public static void f(String tag, String message) {
        if (!DEBUG_FILE) {
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + "/agglog.txt");
        if (file.exists() && file.length() > LOG_FILE_MAX_SIZE) {
            file.delete();
        }

        try {
            FileWriter fw = new FileWriter(file, true);
            String formatMsg = _TIME_() + "  " + tag + "  " + message + "\n";
            fw.write(formatMsg);
            fw.close();
        } catch (IOException e) {
        }
    }

    public static void fi(String tag, String s) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            Log.i(tag, _FUNC_(PARENT_STACK_LEVEL) + s);
        }
        f(tag, s);
    }

    public static void i(String tag, Object message) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            Log.i(tag, _FUNC_() + message.toString());
        }
    }

    public static boolean isEnabled() {
        return DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_ERROR;
    }

    public static boolean isEnaledFile() {
        return DEBUG_FILE;
    }

    public static void l(String tag, String message) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            int maxLogSize = 1000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.i(tag, _FUNC_() + message.substring(start, end));
            }
        }
    }

    public static void line() {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            StackTraceElement traceElement = new Exception().getStackTrace()[STACK_LEVEL - 1];

            StringBuilder sb = new StringBuilder();
            sb.append(traceElement.getMethodName());
            sb.append(":");
            sb.append(traceElement.getLineNumber());

            String className = traceElement.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);

            Log.i(className, sb.toString());
        }
    }

    public static void TRACE() {
        if (ENABLE_TRACE) {
            LOG(DEFAULT_TAG, "<<<<====", 0, 'v');
        }
    }

    public static void VERBOSE(final String msg) {
        if (ENABLE_VERBOSE) {
            LOG(DEFAULT_TAG, msg, 0, 'v');
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_ERROR) {
            Log.w(tag, _FUNC_() + message);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_ERROR) {
            Log.w(tag, msg, tr);
        }
    }

    public static void WARN(final String msg) {
        if (ENABLE_WARN) {
            LOG(DEFAULT_TAG, msg, 0, 'w');
        }
    }

    private static String _FUNC_() {
        StackTraceElement traceElement = new Exception().getStackTrace()[STACK_LEVEL];
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        String className = traceElement.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        sb.append(className);
        sb.append(":");
        sb.append(traceElement.getMethodName());
        sb.append(":");
        sb.append(traceElement.getLineNumber());
        sb.append("> ");
        return sb.toString();
    }

    private static String _FUNC_(int stack_level) {
        StackTraceElement traceElement = new Exception().getStackTrace()[stack_level];
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        String className = traceElement.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        sb.append(className);
        sb.append(":");
        sb.append(traceElement.getMethodName());
        sb.append(":");
        sb.append(traceElement.getLineNumber());
        sb.append("> ");
        return sb.toString();
    }

    private static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    private static void LOG(final String TAG, final String msg, final int traceLevel, final char logLevel) {
        if (!ENABLE_LOG) {
            return;
        }

        final String threadName = Thread.currentThread().getName();
        final int DEFAULT_TRACE_LEVEL = 4;
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[DEFAULT_TRACE_LEVEL + traceLevel];
        final String fullClassName = trace.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final String methodName = trace.getMethodName();
        final int lineNo = trace.getLineNumber();
        final String meta = ENABLE_LOG_META ? "[" + threadName + "]" + "<" + className + ":" + methodName + ":"
                + lineNo + "> " : "";
        final String traceInfo = ENABLE_LOG_TRACE_INFO ? " >>> at " + trace : "";
        switch (logLevel) {
            case 'v':
                Log.v(TAG, meta + msg + traceInfo);
                break;
            case 'd':
                Log.d(TAG, meta + msg + traceInfo);
                break;
            case 'i':
                Log.i(TAG, meta + msg + traceInfo);
                break;
            case 'w':
                Log.w(TAG, meta + msg + traceInfo);
                break;
            case 'e':
                Log.e(TAG, meta + msg + traceInfo);
                break;
        }
    }

    public static void i(Object message) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            Log.i(TAG, _FUNC_() + message.toString());
        }
    }

    public static void i(Object message, int stack_level) {
        if (DEBUG_LEVEL > DEBUG_NONE && DEBUG_LEVEL < DEBUG_WARN) {
            Log.i(TAG, _FUNC_(stack_level) + message.toString());
        }
    }


    public static void ld(String tag, String message) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).d(message);
    }

    public static void logTest() {
        throw new RuntimeException();
    }

    public static void li(String tag, String message) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).i(message);
    }

    public static void lw(String tag, String message) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).w(message);
    }

    public static void le(String tag, String message) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).e(message);
    }

    public static void le(String tag, Throwable throwable, String message) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).e(throwable, message);
    }

    public static void json(String tag, String json) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).json(json);
    }

    public static void json(String tag, String contentHeader, String json) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag).json(contentHeader, json);
    }

    public static void json(String tag, int methodCount, String contentHeader, String json) {
        if (!isEnabled()) {
            return;
        }
        Logger.t(tag, methodCount).json(contentHeader, json);
    }
}
