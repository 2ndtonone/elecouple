package com.sulong.elecouple.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.dialog.CommonDialogFragment;
import com.sulong.elecouple.ui.views.CustomToast;
import com.sulong.elecouple.utils.BaiduSDKInitializer;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.PersistUtils;
import com.sulong.elecouple.utils.Utility;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DELAY_MILLIS = 2500;

    private Handler mHandler = new Handler();
    private FrameLayout mGifViewContainer;
    private WebView mGifView;
    private Runnable checkLocateSuccessTask = new Runnable() {
        int currentSecond = 0;

        @Override
        public void run() {
            if (currentSecond >= 10) {
                CustomToast.makeText(getApplicationContext(), R.string.gps_fail, Toast.LENGTH_LONG).show();
                goToNextPage(false);
            } else if (isLocateSuccess()) {
                goToNextPage(true);
            } else {
                currentSecond = currentSecond + 1;
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppUpdateChecker.checkUpdate(true, null);
        setContentView(R.layout.activity_splash);
        loadGif();
        SplashActivityPermissionsDispatcher.startLocateWithCheck(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseGif();
        mHandler.removeCallbacks(checkLocateSuccessTask);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    })
    public void startLocate() {
        BaiduSDKInitializer.initialize(getApplicationContext());
        Utility.startGetLocation();
        mHandler.post(checkLocateSuccessTask);
    }

    @OnPermissionDenied({
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    })
    public void onLocatePermissionDenied() {
        String permissionName = getString(R.string.permission_locate) +
                getString(R.string.or) +
                getString(R.string.permission_access_external_storage);
        String toastText = getString(R.string.permission_deny_temporarily, permissionName, permissionName);
        showMessageDialog(toastText);
    }

    @OnNeverAskAgain({
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    })
    public void showNeverAskAgainForLocate() {
        String permissionName = getString(R.string.permission_locate) +
                getString(R.string.or) +
                getString(R.string.permission_access_external_storage);
        String toastText = getString(R.string.permission_deny_never_ask, permissionName, permissionName);
        showMessageDialog(toastText);
    }

    private void showMessageDialog(String contentText) {
        new CommonDialogFragment.Builder()
                .showTitle(false)
                .setContentText(contentText)
                .showSingleButton(true)
                .setSingleButtonText(R.string.btn_ok)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (PermissionUtils.hasSelfPermissions(SplashActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                            startLocate();
                        } else {
                            goToNextPage(true);
                        }
                    }
                })
                .create()
                .show(getSupportFragmentManager());
    }

    private void loadGif() {
        mGifViewContainer = (FrameLayout) findViewById(R.id.gif_view_container);
        mGifView = new WebView(getApplicationContext());
        mGifViewContainer.addView(mGifView);
        mGifView.setBackgroundColor(Color.TRANSPARENT);
        mGifView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            mGifView.setScrollBarSize(0);
        }
        mGifView.clearCache(false);
        String data = "<html><body><head><style>*{ margin:0; padding: 0;}</style></head><img src=\""
                + "logo_anim.gif" +
                "\" width=\"100%\" loop=\"false\"/></body></html>";
        mGifView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
        mGifView.getSettings().setBuiltInZoomControls(false);
        mGifView.getSettings().setDisplayZoomControls(false);
        mGifView.getSettings().setSupportZoom(false);
        mGifView.getSettings().setDomStorageEnabled(false);
    }

    private void releaseGif() {
        mGifView.getSettings().setBuiltInZoomControls(true);
        mGifView.getSettings().setDisplayZoomControls(true);
        mGifView.getSettings().setSupportZoom(true);
        mGifViewContainer.removeAllViews();
        mGifView.removeAllViews();
        mGifView.destroy();
    }

    private void goToNextPage(boolean delay) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (PersistUtils.isFirstEnterApp(getApplicationContext())) {
                    goGuide();
                } else {
                    goToLogin();
                }
            }
        };
        if (delay) {
            mHandler.postDelayed(task, SPLASH_DELAY_MILLIS);
        } else {
            task.run();
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, StartActivit.class);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, NewGuideActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isLocateSuccess() {
        String gpsCityName = getSharedPreferences(ConstantUtils.GPS_PREF, MODE_PRIVATE)
                .getString(ConstantUtils.PREF_KEY_GPS_CITY_NAME, "");
        return !TextUtils.isEmpty(gpsCityName) && !gpsCityName.equals("null");
    }
}