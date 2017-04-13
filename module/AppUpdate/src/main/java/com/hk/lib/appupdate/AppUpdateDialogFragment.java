package com.hk.lib.appupdate;


import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

/**
 * 应用升级对话框
 * Created by ydh on 2016/5/27.
 */
public class AppUpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    private static class ViewHolder {
        TextView dialog_title;
        TextView dialog_content;
        Button dialog_left_btn;
        Button dialog_right_btn;
        View bottom_button_container;
        View bottom_button_line;
        ViewGroup content_view;
        NumberProgressBar number_progress_bar;
    }

    private ViewHolder mHolder = new ViewHolder();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mRemoteVersion = 0;
    private boolean isForce = false;
    private String mDownloadUrl = null;

    public static AppUpdateDialogFragment newInstance(boolean force, String downloadUrl, int remoteVersion) {
        AppUpdateDialogFragment fragment = new AppUpdateDialogFragment();
        fragment.setForce(force);
        fragment.setDownloadUrl(downloadUrl);
        fragment.setRemoteVersion(remoteVersion);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileDownloader.getImpl().bindService();
        FileDownloader.getImpl().addServiceConnectListener(mFileDownloadConnectListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        createContentView(dialog);
        configDialogSize(dialog);
        return dialog;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        bindData();
        DownloadManager.getImpl().addDownloadListener(mDownloadUrl, mFileDownloadSimpleListener);
    }

    @Override
    public void onDetach() {
        FileDownloader.getImpl().removeServiceConnectListener(mFileDownloadConnectListener);
        DownloadManager.getImpl().removeDownloadListener(mDownloadUrl, mFileDownloadSimpleListener);
        super.onDetach();
        if (isForce) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    protected <T extends View> T $(@IdRes int id) {
        if (getView() == null) {
            return null;
        } else {
            return (T) getView().findViewById(id);
        }
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    protected void configDialogSize(Dialog dialog) {
        configDialogSize(dialog, 5f / 6f);
    }

    protected void configDialogSize(Dialog dialog, float proportion) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics screenMetrics = new DisplayMetrics();
        dialogWindow.getWindowManager().getDefaultDisplay()
                .getMetrics(screenMetrics);
        if (proportion <= 0f) {
            proportion = 5f / 6f;
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏状态下以高度为基准
            lp.height = (int) (screenMetrics.heightPixels * proportion);
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            // 竖屏状态下以宽度为基准
            lp.width = (int) (screenMetrics.widthPixels * proportion);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        dialogWindow.setAttributes(lp);
    }

    public void show(FragmentManager manager) {
        super.show(manager, getLogTag());
    }

    public int show(FragmentTransaction transaction) {
        return super.show(transaction, getLogTag());
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public void setRemoteVersion(int version) {
        mRemoteVersion = version;
    }

    private void createContentView(Dialog dialog) {
        // create and set dialog content view
        View dialogView = LayoutInflater.from(getActivity()).inflate(
                R.layout.app_update_dialog,
                (ViewGroup) dialog.getWindow().getDecorView(),
                false);
        dialog.setContentView(dialogView);
        findViews(dialogView);
    }

    private void findViews(View dialogView) {
        mHolder.dialog_title = $(dialogView, R.id.dialog_title);
        mHolder.dialog_content = $(dialogView, R.id.dialog_content);
        mHolder.dialog_left_btn = $(dialogView, R.id.dialog_left_btn);
        mHolder.dialog_right_btn = $(dialogView, R.id.dialog_right_btn);
        mHolder.bottom_button_container = $(dialogView, R.id.bottom_button_container);
        mHolder.content_view = $(dialogView, R.id.content_view);
        mHolder.bottom_button_line = $(dialogView, R.id.bottom_button_line);
        mHolder.number_progress_bar = $(dialogView, R.id.number_progress_bar);
    }

    private void bindData() {
        // 底部左右按钮
        mHolder.dialog_left_btn.setOnClickListener(this);
        mHolder.dialog_right_btn.setOnClickListener(this);

        if (DownloadManager.getImpl().isReady()) {
            // 更新进度条
            long soFarBytes = FileDownloader.getImpl().getSoFar(getDownloadId());
            long totalBytes = FileDownloader.getImpl().getTotal(getDownloadId());
            updateProgressUi(soFarBytes, totalBytes);
        } else {
            LogUtils.i(getLogTag(), "Download service is not ready yet");
        }
        // 更新底部按钮显示
        updateBottomUi();
    }

    private int getDownloadId() {
        return AppUpdateManager.getDownloadId(getActivity());
    }

    public void updateBottomUi() {
        StringBuilder logSb = new StringBuilder();
        int status = FileDownloader.getImpl().getStatus(getDownloadId());
        long soFarBytes = FileDownloader.getImpl().getSoFar(getDownloadId());
        long totalBytes = FileDownloader.getImpl().getTotal(getDownloadId());

        boolean isDownloading = status == FileDownloadStatus.pending
                || status == FileDownloadStatus.started
                || status == FileDownloadStatus.connected
                || status == FileDownloadStatus.progress;
        boolean isPaused = status == FileDownloadStatus.paused || (soFarBytes > 0 && totalBytes > 0 && soFarBytes < totalBytes);
        boolean isDownloadCorrectly = AppUpdateManager.isDownloadedCorrectApk(getActivity(), mRemoteVersion);
        boolean isFileExist = AppUpdateManager.isApkFileExisted(getActivity());
        logSb.append("downloadId:" + getDownloadId()).append(" | ")
                .append("status:" + status).append(" | ")
                .append("isFileExist:" + isFileExist).append(" | ")
                .append("isDownloadCorrectly:" + isDownloadCorrectly).append(" | ");
        if (isDownloadCorrectly) { // 下载完成且正确，显示安装按钮
            updateProgressUi(1, 1);
            showOnlyProgressBar();
            showInstallButton();
            logSb.append("下载完成且正确，显示安装按钮").append(" | ");
        } else if (!isFileExist) {
            showOnlyMsg();
            showUpdateButton();
            logSb.append("文件不存在了，显示更新按钮").append(" | ");
        } else if (status == FileDownloadStatus.completed) { // 下载完成，但是下载的文件不对，显示下载错误按钮
            showOnlyProgressBar();
            showWrongApkButton();
            logSb.append("下载完成，但是下载的文件不对，显示下载错误按钮").append(" | ");
        } else if (isDownloading) { // 正在下载中，不显示任何操作按钮，只显示进度条
            showBottomButtons(false);
            showOnlyProgressBar();
            logSb.append("正在下载中，不显示任何操作按钮，只显示进度条").append(" | ");
        } else if (isPaused) { // 如果是暂停状态，显示进度条和更新按钮
            showOnlyProgressBar();
            showUpdateButton();
            logSb.append("如果是暂停状态，显示进度条和更新按钮").append(" | ");
        } else { // 其他情况，显示更新按钮
            showUpdateButton();
            logSb.append("其他情况，显示更新按钮").append(" | ");
        }
        LogUtils.i(getLogTag(), logSb.toString());
    }

    private void updateProgressUi(long soFarBytes, long totalBytes) {
        if (soFarBytes > 0 && totalBytes > 0) {
            int percent = (int) (100 * soFarBytes / totalBytes);
            mHolder.number_progress_bar.setProgress(percent);
        } else {
            mHolder.number_progress_bar.setProgress(0);
        }
    }

    private void showOnlyMsg() {
        if(! TextUtils.isEmpty(PersistUtils.getUpdateInfo(getContext()))){
            mHolder.dialog_content.setText(PersistUtils.getUpdateInfo(getContext()));
        }
        mHolder.dialog_content.setVisibility(View.VISIBLE);
        mHolder.number_progress_bar.setVisibility(View.GONE);
    }

    private void showOnlyProgressBar() {
        mHolder.dialog_content.setVisibility(View.GONE);
        mHolder.number_progress_bar.setVisibility(View.VISIBLE);
    }

    public void showBottomButtons(boolean shown) {
        mHolder.bottom_button_line.setVisibility(shown ? View.VISIBLE : View.GONE);
        mHolder.bottom_button_container.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    public void showWrongApkButton() {
        showButtonInternal(R.string.app_update_dialog_btn_wrong_apk);
    }

    public void showInstallButton() {
        showButtonInternal(R.string.app_update_dialog_btn_install);
    }

    public void showUpdateButton() {
        showButtonInternal(R.string.app_update_dialog_btn_update);
    }

    private void showButtonInternal(int textResId) {
        showBottomButtons(true);
        mHolder.dialog_left_btn.setVisibility(isForce ? View.GONE : View.VISIBLE);
        mHolder.dialog_right_btn.setVisibility(View.VISIBLE);
        mHolder.dialog_right_btn.setText(textResId);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_left_btn) { // 稍后更新
            dismiss();
        } else if (id == R.id.dialog_right_btn) { // 更新/安装
            AppUpdateManager.downloadApk(this, mDownloadUrl, mRemoteVersion);
        }
    }

    private FileDownloadSimpleListener mFileDownloadSimpleListener = new FileDownloadSimpleListener() {
        @Override
        public void started(BaseDownloadTask task) {
            super.started(task);
            showBottomButtons(false);
            mHolder.dialog_content.setVisibility(View.GONE);
            mHolder.number_progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            updateProgressUi(soFarBytes, totalBytes);
        }

        @Override
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            updateProgressUi(soFarBytes, totalBytes);
        }

        @Override
        public void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            updateProgressUi(soFarBytes, totalBytes);
        }

        @Override
        public void completed(BaseDownloadTask task) {
            super.completed(task);
            updateProgressUi(1, 1);
            updateBottomUi();
            if (AppUpdateManager.isDownloadedCorrectApk(getActivity(), mRemoteVersion)) {
                LogUtils.i(getLogTag(), "download correctly, install the latest apk now");
                Utility.installApk(getActivity(), AppUpdateManager.getApkFilePath(getActivity()));
                return;
            }
        }

        @Override
        public void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            updateBottomUi();
        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            updateBottomUi();
        }
    };

    private FileDownloadConnectListener mFileDownloadConnectListener = new FileDownloadConnectListener() {
        @Override
        public void connected() {
            LogUtils.i(getLogTag(), "File download service connected");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bindData();
                }
            });
        }

        @Override
        public void disconnected() {

        }
    };
}