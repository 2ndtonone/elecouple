package com.hk.lib.appupdate;

import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

/**
 * Created by ydh on 2016/5/24.
 */
public class FileDownloadSimpleListener extends FileDownloadListener {
    @Override
    public void started(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath());
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath()).append(" | ")
                .append("etag:" + etag).append(" | ")
                .append("isContinue:" + isContinue).append(" | ")
                .append("soFarBytes:" + soFarBytes).append(" | ")
                .append("totalBytes:" + totalBytes);
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath()).append(" | ")
                .append("soFarBytes:" + soFarBytes).append(" | ")
                .append("totalBytes:" + totalBytes);
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
    }

    @Override
    public void blockComplete(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath());
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void completed(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath());
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath()).append(" | ")
                .append("soFarBytes:" + soFarBytes).append(" | ")
                .append("totalBytes:" + totalBytes);
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void error(BaseDownloadTask task, Throwable e) {
        StringBuilder logSb = new StringBuilder();
        if (e != null) {
            e.printStackTrace();
            logSb.append("error:" + e.getMessage());
        }
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath());
        LogUtils.i(getLogTag(), logSb.toString());
    }

    @Override
    public void warn(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append(" | ")
                .append("url:" + task.getUrl()).append(" | ")
                .append("path:" + task.getPath());
        LogUtils.i(getLogTag(), logSb.toString());
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }
}