package com.sulong.elecouple.wrapper;

import com.sulong.elecouple.utils.Debug;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

/**
 * Created by ydh on 2016/5/24.
 */
public class FileDownloadSimpleListener extends FileDownloadListener {
    @Override
    public void started(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath());
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath()).append("\n")
                .append("etag:" + etag).append("\n")
                .append("isContinue:" + isContinue).append("\n")
                .append("soFarBytes:" + soFarBytes).append("\n")
                .append("totalBytes:" + totalBytes);
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath()).append("\n")
                .append("soFarBytes:" + soFarBytes).append("\n")
                .append("totalBytes:" + totalBytes);
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
    }

    @Override
    public void blockComplete(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath());
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void completed(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath());
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath()).append("\n")
                .append("soFarBytes:" + soFarBytes).append("\n")
                .append("totalBytes:" + totalBytes);
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void error(BaseDownloadTask task, Throwable e) {
        StringBuilder logSb = new StringBuilder();
        if (e != null) {
            e.printStackTrace();
            logSb.append("error:" + e.getMessage());
        }
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath());
        Debug.li(getLogTag(), logSb.toString());
    }

    @Override
    public void warn(BaseDownloadTask task) {
        StringBuilder logSb = new StringBuilder();
        logSb.append("id:" + task.getId()).append("\n")
                .append("url:" + task.getUrl()).append("\n")
                .append("path:" + task.getPath());
        Debug.li(getLogTag(), logSb.toString());
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }
}