package com.sulong.elecouple.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.views.AggCommonDialog;

import permissions.dispatcher.PermissionUtils;

/**
 * Created by lixi on 2015/8/19.
 */
public class CallUtils {

    public static void call(final Context context, final String phoneNo, final AggCommonDialog.SimpleOpClickListener listener) {
        final AggCommonDialog aggCommonDialog = new AggCommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.KEY_CONTENT_RES_STR, phoneNo);
        bundle.putInt(ConstantUtils.KEY_DLG_RIGHT_BTN_RES_ID, R.string.call);
        bundle.putBoolean(ConstantUtils.KEY_DLG_SHOWTITLE, false);
        aggCommonDialog.setArguments(bundle);
        aggCommonDialog.setmOpListener(new AggCommonDialog.SimpleOpClickListener() {
            @Override
            public void onRightClicked() {
                if (listener != null) {
                    listener.onRightClicked();
                }
                aggCommonDialog.dismiss();
            }
        });
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        aggCommonDialog.show(fragmentTransaction, "");
    }

    public static void goToCallPage(Context context, String phoneNo) {
        Intent intent;
        if (PermissionUtils.hasSelfPermissions(context, Manifest.permission.CALL_PHONE)) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        } else {
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToDialPage(Context context, String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}