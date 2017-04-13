package com.hk.lib.appupdate;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ydh on 2016/5/23.
 */
public class DownloadManager {
    private final static class HolderClass {
        private final static DownloadManager INSTANCE = new DownloadManager();
    }

    public static DownloadManager getImpl() {
        return HolderClass.INSTANCE;
    }

    // May be identify by id or url?
    private Map<String, List<FileDownloadSimpleListener>> mListenerList = new HashMap<>();

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public int startDownload(final String url, final String path, Object tag) {
        return FileDownloader.getImpl()
                .create(url)
                .setPath(path)
                .setTag(tag)
                .setListener(mGeneralListener)
                .setCallbackProgressTimes(100)
                .setMinIntervalUpdateSpeed(50)
                .start();
    }

    public void addDownloadListener(String url, FileDownloadSimpleListener listener) {
        List<FileDownloadSimpleListener> listeners = mListenerList.get(url);
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        mListenerList.put(url, listeners);
    }

    public boolean removeDownloadListener(String url, FileDownloadSimpleListener listener) {
        List<FileDownloadSimpleListener> listeners = mListenerList.get(url);
        if (listeners != null) {
            return listeners.remove(listener);
        }
        return false;
    }

    public BaseDownloadTask getDownloadTask(int id) {
        return FileDownloadList.getImpl().get(id);
    }

    private FileDownloadListener mGeneralListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).pending(task, soFarBytes, totalBytes);
                }
            }
        }

        @Override
        protected void started(BaseDownloadTask task) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).started(task);
                }
            }
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).connected(task, etag, isContinue, soFarBytes, totalBytes);
                }
            }
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).progress(task, soFarBytes, totalBytes);
                }
            }
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).blockComplete(task);
                }
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).completed(task);
                }
            }
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).paused(task, soFarBytes, totalBytes);
                }
            }
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).error(task, e);
                }
            }
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            List<FileDownloadSimpleListener> listeners = mListenerList.get(task.getUrl());
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).warn(task);
                }
            }
        }
    };
}