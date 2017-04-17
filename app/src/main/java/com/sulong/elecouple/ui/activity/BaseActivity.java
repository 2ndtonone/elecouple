package com.sulong.elecouple.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.ModuleProvider;
import com.sulong.elecouple.dagger.component.ActivityToolComponent;
import com.sulong.elecouple.dagger.component.AppComponent;
import com.sulong.elecouple.dagger.component.DaggerActivityToolComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.view.IBaseViewWithLoading;
import com.sulong.elecouple.mvp.view.IBaseViewWithLogin;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;
import com.sulong.elecouple.ui.dialog.LoadingProgressDialog;
import com.sulong.elecouple.ui.views.CustomToast;
import com.sulong.elecouple.ui.views.LoginConflictDialogFragment;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.StringUtils;
import com.sulong.elecouple.utils.SystemBarTintManager;
import com.sulong.elecouple.utils.Utility;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends RxFragmentActivity
        implements IBaseViewWithWebRequest, IBaseViewWithLoading, IBaseViewWithLogin {

    protected static List<String> sForegroundActivityNameList = new ArrayList<>();
    protected SystemBarTintManager mBarTintManager;
    protected LoginManager mLoginManager;
    protected LocalBroadcastManager mLocalBroadcastManager;
    protected BroadcastReceiver mLocalReceiver;
    protected boolean isDestroyed = false;
    protected boolean isResumed = false;
    protected LoadingProgressDialog mLoadingProgressDialog;
    private TextView tv_back;
    private TextView tv_title;
    private ImageView iv_back;
    private ImageView iv_more;

    /**
     * <p>获取正在前台运行的activity名称 </p>
     * <p>由于ActivityManager.getRunningTasks()在5.0后被弃用，所以只能通过自定义的方式判断前台activity</p>
     *
     * @return
     */
    public static String getForegroundActivityName() {
        String activityName = null;
        if (!sForegroundActivityNameList.isEmpty()) {
            activityName = sForegroundActivityNameList.get(sForegroundActivityNameList.size() - 1);
            Debug.li(BaseActivity.class.getSimpleName(), "getForegroundActivityName:" + activityName);
        }
        return activityName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        instantiateMemberVariables();
        injectDependencies();
        registerLocalReceiver();
        initStatusBarColor();
        initView();
    }

    private void initView() {
        tv_back = $(R.id.tv_back);
        iv_back = $(R.id.iv_back);
        tv_title = $(R.id.tv_title);
        iv_more = $(R.id.iv_more);
        if(iv_more!=null){
            iv_more.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        sForegroundActivityNameList.add(getClass().getName());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        setBtnBackClickListener();
        addStatusBarPadding();
        initLoadingDialogIfNeed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        isResumed = true;
        MobclickAgent.onResume(this);
        showLoginConflictDialogIfNeed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        isResumed = false;
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        sForegroundActivityNameList.remove(getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        isDestroyed = true;
        unregisterLocalReceiver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Debug.li(getLogTag(), "requestCode:" + requestCode + "\n" +
                "resultCode:" + resultCode + "\n" +
                "mCanTakeData:" + data + "\n");
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    protected void setBtnBackClickListener() {
        if (tv_back != null) {
            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (iv_back != null) {
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    protected void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    protected void showLoginConflictDialogIfNeed() {
        if (!isNeedLogin()) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        LoginConflictDialogFragment dialog = (LoginConflictDialogFragment) fm.findFragmentByTag(LoginConflictDialogFragment.TAG);
        if (!mLoginManager.isLogin()) {
            if (dialog == null) {
                dialog = new LoginConflictDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LoginConflictDialogFragment.LOGIN_TYPE, LoginConflictDialogFragment.TYPE_USER_LOGIN);
                dialog.setArguments(bundle);
                fm.beginTransaction()
                        .add(dialog, LoginConflictDialogFragment.TAG)
                        .commitAllowingStateLoss();
            }
        } else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    protected void initStatusBarColor() {
        mBarTintManager.setStatusBarTintEnabled(true);
        mBarTintManager.setStatusBarTintColor(getStatusBarColor());
        if (Utility.isMIUI()) {
            SystemBarTintManager.setMiuiStatusBarDarkMode(this, true);
        } else if (Utility.isFlyme()) {
            SystemBarTintManager.setMeizuStatusBarDarkMode(this, true);
        }
    }

    public int getStatusBarColor() {
        return getResources().getColor(R.color.main_red);
    }

    public int getStatusBarHeight() {
        return mBarTintManager.getConfig().getStatusBarHeight();
    }

    public void setTintResource(int resource) {
        mBarTintManager.setStatusBarTintResource(resource);
    }

    public void setTintColor(int color) {
        mBarTintManager.setStatusBarTintColor(color);
    }

    protected void addStatusBarPadding() {
        if (mBarTintManager != null
                && mBarTintManager.isStatusBarAvailable()
                && mBarTintManager.isStatusBarTintEnabled()) {
            View contentView = $(android.R.id.content);
            if (contentView != null) {
                int oldPaddingTop = contentView.getPaddingTop();
                int newPaddingTop = oldPaddingTop + getStatusBarHeight();
                contentView.setPadding(
                        contentView.getPaddingLeft(),
                        newPaddingTop,
                        contentView.getPaddingRight(),
                        contentView.getPaddingBottom()
                );
            }
        }
    }

    protected void initLoadingDialogIfNeed() {
        if (enableLoadingView()) {
            mLoadingProgressDialog = new LoadingProgressDialog(this);
        }
    }

    /** 需要登录才能进的页面，用于检测到登录冲突时判断是否要关闭本页面 */
    protected boolean isNeedLogin() {
        return false;
    }

    protected void injectDependencies() {
        ActivityToolComponent toolComponent = DaggerActivityToolComponent.builder()
                .activityToolModule(ModuleProvider.getInstance().provideToolModule(this))
                .build();
        mBarTintManager = toolComponent.systemBarTintManager();
        AppComponent appComponent = ComponentHolder.getAppComponent();
        mLoginManager = appComponent.loginManager();
        mLocalBroadcastManager = appComponent.localBroadcastManager();
    }

    protected void registerLocalReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_CLOSE_PAGE_WHICH_NEED_LOGIN);
        intentFilter.addAction(ConstantUtils.ACTION_LOGIN_CONFLICT);
        intentFilter.addAction(ConstantUtils.ACTION_CLOSE_ALL_PAGE);
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);
    }

    protected void unregisterLocalReceiver() {
        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
    }

    protected void instantiateMemberVariables() {
        mLocalReceiver = new LocalBroadcastReceiver();
    }

    public String getLogTag() {
        return getClass().getSimpleName();
    }

    protected boolean enableLoadingView() {
        return true;
    }

    /**
     * @param clazz  登录完成，要跳转的页面（Activity）
     * @param bundle 需要传递的参数
     */
    @Override
    public void goToUserLoginPage(Class clazz, Bundle bundle) {
        if (isDestroyed) {
            return;
        }
        Intent intent = new Intent(this, LoginActivity.class);
        if (clazz != null) {
            intent.putExtra("classStr", clazz);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    // >>>>>>>> View Interface

    @Override
    public void showNoNetworkError() {
        CustomToast.showToast(R.string.no_network);
    }

    @Override
    public void showWrongResponse(int code, String message) {
        if (code == ConstantUtils.STATUS_AUTH_FAIL) {
            goToUserLoginPage(null, null);
        }
        if (!StringUtils.isEmpty(message)) {
            CustomToast.showToast(message);
        }
    }

    @Override
    public void showLoadingView() {
        if (!isDestroyed
                && enableLoadingView()
                && mLoadingProgressDialog != null
                && !mLoadingProgressDialog.isShowing()) {
            mLoadingProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingView() {
        if (!isDestroyed
                && enableLoadingView()
                && mLoadingProgressDialog != null
                && mLoadingProgressDialog.isShowing()) {
            mLoadingProgressDialog.dismiss();
        }
    }

    protected class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(ConstantUtils.ACTION_LOGIN_CONFLICT)) {
                showLoginConflictDialogIfNeed();
            } else if (action.equals(ConstantUtils.ACTION_CLOSE_PAGE_WHICH_NEED_LOGIN)) {
                if (isNeedLogin()) {
                    finish();
                }
            } else if (action.equals(ConstantUtils.ACTION_CLOSE_ALL_PAGE)) {
                finish();
            }
        }
    }

    // <<<<<<<< View Interface
}