package com.umeng.social;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import static com.umeng.socialize.PlatformConfig.*;

/**
 * Created by ydh on 2016/5/12.
 */
public class UmengShareUtils {

    public static void initialize(Context context) {
        //微信 appid appsecret
        setWeixin(context.getString(R.string.wechat_app_id), context.getString(R.string.wechat_app_secret));
        //新浪微博 appkey appsecret
        setSinaWeibo(context.getString(R.string.sina_weibo_app_key), context.getString(R.string.sina_weibo_app_secret), context.getString(R.string.sina_weibo_app_redirect_url));
        // QQ和Qzone appid appkey
        setQQZone(context.getString(R.string.qzone_app_id), context.getString(R.string.qzone_app_key));

        Log.LOG = BuildConfig.DEBUG;
//        Config.IsToastTip = false;
    }

    public static void enableLog(boolean enable) {
        Log.LOG = enable;
    }

    public static ShareAction createDefaultShareAction(final Activity activity) {
        SHARE_MEDIA[] displayList;

        //Umeng sdk V5.1.2 微博（未安装客户端）分享在Nexus 6P 6.0.1 上闪退，所以屏蔽。（戴文源）
        displayList = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE
        };
        return new ShareAction(activity).setDisplayList(displayList);
    }
}