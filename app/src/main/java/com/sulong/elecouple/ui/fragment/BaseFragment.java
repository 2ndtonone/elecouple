package com.sulong.elecouple.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.R;
import com.sulong.elecouple.mvp.view.IBaseViewWithLoading;
import com.sulong.elecouple.mvp.view.IBaseViewWithWebRequest;
import com.sulong.elecouple.ui.activity.LoginActivity;
import com.sulong.elecouple.ui.dialog.LoadingProgressDialog;
import com.sulong.elecouple.ui.views.CustomToast;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.web.AhAsyncHttpClient;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseFragment extends Fragment
        implements IBaseViewWithWebRequest, IBaseViewWithLoading {

    protected boolean isActivityCreated = false;
    protected boolean isDetached = false;
    protected LoadingProgressDialog mLoadingProgressDialog;

    @Override
    public void onAttach(Context context) {
        isDetached = false;
        super.onAttach(context);
        initLoadingDialogIfNeed();
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Debug.fi(getLogTag(), toString());
        isActivityCreated = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        Debug.fi(getLogTag(), toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Debug.fi(getLogTag(), toString());
        AhAsyncHttpClient.getInstance().cancelRequestsByTAG(getClass().getName(), true);
        RefWatcher refWatcher = LocationApplication.getInstance().getRefWatcher();
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    @Override
    public void onDetach() {
        isDetached = true;
        super.onDetach();
        Debug.fi(getLogTag(), toString());
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

    protected void initLoadingDialogIfNeed() {
        if (enableLoadingView() && getActivity() != null) {
            mLoadingProgressDialog = new LoadingProgressDialog(getActivity());
        }
    }

    protected boolean isActivityCreated() {
        return isActivityCreated;
    }

    protected boolean enableLoadingView() {
        return true;
    }

    /**
     * @param clazz  登录完成，要跳转的页面（Activity）
     * @param bundle 需要传递的参数
     */
    protected void goToLoginPageInBase(Class clazz, Bundle bundle) {
        if (getActivity() == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        if (clazz != null) {
            intent.putExtra("classStr", clazz);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 将final的getString封装一层，以便mockito来mock
     *
     * @param resId 字符串资源id
     * @return 获取的字符串
     */
    public String getStringById(@StringRes int resId) {
        Debug.li(getLogTag(), "isDetached : " + isDetached() + "\n"
                + "!isVisible() : " + !isVisible());
        if (isDetached) {
            return "";
        }
        return getString(resId);
    }

    /**
     * 将final的getString封装一层，以便mockito来mock
     *
     * @param resId  字符串资源id
     * @param params 格式化参数
     * @return 获取的字符串
     */
    public String getStringById(@StringRes int resId, Object... params) {
        Debug.li(getLogTag(), "isDetached : " + isDetached() + "\n"
                + "!isVisible() : " + !isVisible());
        if (isDetached) {
            return "";
        }
        return getString(resId, params);
    }

    // >>>>>>>> View Interface

    @Override
    public void showNoNetworkError() {
        CustomToast.showToast(R.string.no_network);
    }

    @Override
    public void showWrongResponse(int code, String message) {
        if (code == ConstantUtils.STATUS_AUTH_FAIL) {
            goToLoginPageInBase(null, null);
        }
        if (!TextUtils.isEmpty(message)) {
            CustomToast.showToast(message);
        }
    }

    @Override
    public void showLoadingView() {
        if (!isDetached
                && enableLoadingView()
                && mLoadingProgressDialog != null
                && !mLoadingProgressDialog.isShowing()) {
            mLoadingProgressDialog.show();
        }
    }

    @Override
    public void hideLoadingView() {
        if (!isDetached
                && enableLoadingView()
                && mLoadingProgressDialog != null
                && mLoadingProgressDialog.isShowing()) {
            mLoadingProgressDialog.dismiss();
        }
    }

    // <<<<<<<< View Interface
}