package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.R;


/**
 * Created by Herbert Dai on 9/5/14.
 */
public class CustomToast extends Toast {

    private static CustomToast sLastToast;
    private static Handler mHandler;
    private static Runnable mCancelToastTask = new Runnable() {
        @Override
        public void run() {
            if (sLastToast != null) {
                sLastToast.cancel();
                sLastToast = null;
            }
        }
    };

    public CustomToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, String info, int duration) {
        return makeText(context, info, duration, Gravity.CENTER);
    }

    public static Toast makeText(Context context, String info, int duration, int gravity) {
        if (sLastToast != null) {
            sLastToast.cancel();
        }
        Context appContext = LocationApplication.getInstance();
        CustomToast toast = new CustomToast(appContext);
        ViewGroup toastLayout = (ViewGroup) LayoutInflater.from(appContext).inflate(R.layout.custom_toast, null);
        TextView contentTxt = ((TextView) toastLayout.getChildAt(0));
        contentTxt.setText(info);
        contentTxt.setGravity(gravity);
        toast.setView(toastLayout);
        toast.setDuration(duration);
        toast.setGravity(gravity, 0, 0);
        sLastToast = toast;
        return toast;
    }

    public static Toast makeText(Context context, int resId, int duration) {
        return makeText(context, resId, duration, Gravity.CENTER);
    }

    public static Toast makeText(Context context, int resId, int duration, int gravity) {
        return makeText(context, LocationApplication.getInstance().getString(resId), duration, gravity);
    }

    public static void showToast(String msg) {
        makeText(null, msg, Toast.LENGTH_SHORT, Gravity.CENTER).show();
    }

    public static void showToast(int resId) {
        makeText(null, LocationApplication.getInstance().getString(resId), Toast.LENGTH_SHORT, Gravity.CENTER).show();
    }

    @Override
    public void show() {
        super.show();
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.removeCallbacks(mCancelToastTask);
        mHandler.postDelayed(mCancelToastTask, 3000);
    }
}