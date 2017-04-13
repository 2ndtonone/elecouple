package com.sulong.elecouple.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.utils.Debug;

/**
 * Created by ydh on 2015/10/23.
 */
public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onPause() {
        super.onPause();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onStop() {
        super.onStop();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
        LocationApplication.getInstance().getRefWatcher().watch(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Debug.i(getLogTag(), toString() + " | hasCode:" + hashCode());
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
}