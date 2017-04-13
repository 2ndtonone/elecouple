package com.sulong.elecouple.utils;

import android.content.Context;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by ydh on 2016/6/22.
 */
public class JpushHelper {

    private Context mContext;

    public JpushHelper(Context context) {
        this.mContext = context;
    }

    public void setupJpush(String loginName) {
        Context context = mContext;
        if (context == null) {
            Debug.li(getLogTag(), "context is null, cannot setup jpush");
            return;
        }

        JPushInterface.resumePush(context);

        // prepare tags
        Set<String> tags = new HashSet<String>();
        tags.add("v" + Utility.getRawVersionName(context));
        tags.add(Locale.getDefault().toString());
        Set<String> formTags = new HashSet<String>();
        for (String tag : tags) {
            formTags.add(tag.replace(".", "_"));
            Debug.fi(getLogTag(), "jpush tag = " + tag.replace(".", "_"));
        }

        // prepare alias
        String alias = loginName.replace(".", "_");
        Debug.fi(getLogTag(), "jpush alias = " + alias);

        // set alias and tags
        JPushInterface.setAliasAndTags(context, alias, formTags, new TagAliasCallback() {
            @Override
            public void gotResult(int resultCode, String s, Set<String> set) {
                if (resultCode == 0) {
                    Debug.li(getLogTag(), "Register jpush alias and tags success!" + " code =" + resultCode);
                } else {
                    Debug.li(getLogTag(), "Register jpush alias and tags failed!" + " code =" + resultCode);
                }
            }
        });
    }

    public String getRegistrationID() {
        return JPushInterface.getRegistrationID(mContext);
    }

    private String getLogTag() {
        return getClass().getSimpleName();
    }
}