package com.sulong.elecouple.utils;

import android.app.Activity;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.ui.dialog.LoadingProgressDialog;
import com.umeng.social.UmengShareUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lixi on 2015/11/5.
 */
public class ShareUtils {

    public static void Share(final Activity activity, String title, String content, String targetUrl, String imageUrl) {
        SharePlatform(activity, null, title, content, targetUrl, imageUrl);
    }

    public static void SharePlatform(final Activity activity, final SHARE_MEDIA share_media, final String title, final String content, final String targetUrl, final String imageUrl) {
        if (share_media == null) {
            SharePlatforms(activity, null, title, content, targetUrl, imageUrl);
        }else {
            SharePlatforms(activity, new SHARE_MEDIA[]{share_media}, title, content, targetUrl, imageUrl);
        }
    }

    public static void SharePlatforms(final Activity activity, final SHARE_MEDIA[] share_media_list, final String title, final String content, final String targetUrl, final String imageUrl) {
        final LoadingProgressDialog loadingDialog = new LoadingProgressDialog(activity);
        loadingDialog.show();
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean isValidImage = false;
                // 先检查分享的图片是否有效，如果图片无效QQ、微博是分享不了的
                if (!StringUtils.isEmpty(imageUrl)) {
                    try {
                        OkHttpClient client = ComponentHolder.getAppComponent().okHttpClient();
                        Request request = new Request.Builder()
                                .url(imageUrl)
                                .head()
                                .build();
                        Call call = client.newCall(request);
                        Debug.li(getLogTag(), "测试分享的图片网址是否有效：" + imageUrl);
                        Response response = call.execute();
                        Debug.li(getLogTag(), "分享的图片网址请求的状态码：" + response.code() + "\n" + "图片地址：" + imageUrl);
                        isValidImage = response.code() == 200;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 再执行分享
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(isValidImage);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isValidImage) {
                        loadingDialog.dismiss();
                        ShareAction shareAction;
                        if (share_media_list != null) {
                            shareAction = new ShareAction(activity);
                            if (share_media_list.length == 1) {
                                shareAction.setPlatform(share_media_list[0]);
                            }else {
                                shareAction.setDisplayList(share_media_list);
                            }
                        } else {
                            shareAction = UmengShareUtils.createDefaultShareAction(activity);
                        }
                        UMWeb  web = new UMWeb(targetUrl);
                        web.setTitle(title);//标题
                        web.setDescription(!StringUtils.isEmpty(content) ? content : title);//描述

                        if (isValidImage) {
                            web.setThumb(new UMImage(activity, imageUrl));
                        } else { // 欲分享的图片无效，只能分享默认图片了
                            String defaultShareImage = PersistUtils.getDefaultShareImageUrl(activity);
                            Debug.li(getLogTag(), "测试分享的默认图片网址是否有效：" + defaultShareImage);
                            if (!StringUtils.isEmpty(defaultShareImage)) {
                                web.setThumb(new UMImage(activity, defaultShareImage));
                            } else {
                                web.setThumb(new UMImage(activity, R.drawable.ic_launcher));
                            }
                        }

                        shareAction.withMedia(web);
                        if (share_media_list != null && share_media_list.length == 1) {
                            shareAction.share();
                        }else {
                            shareAction.open();
                        }
                        Debug.li(getLogTag(), "title:" + title + "\n"
                                + "content:" + content + "\n"
                                + "url:" + targetUrl + "\n"
                                + "imageUrl:" + imageUrl + "\n"
                                + "activity:" + activity + "\n"
                                + "currentThread:" + Thread.currentThread() + "\n"
                        );
                    }
                });
    }

    public static void SharePlatform(final Activity activity, SHARE_MEDIA share_media, String title, String content, String targetUrl, File imgFile) {
        UMWeb  web = new UMWeb(targetUrl);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, imgFile));  //缩略图
        web.setDescription(content);//描述

        new ShareAction(activity)
                .setPlatform(share_media)
                .withMedia(web)
                .share();

        Debug.li(getLogTag(), "title:" + title + "\n"
                + "content:" + content + "\n"
                + "url:" + targetUrl + "\n"
                + "imgFile:" + imgFile + "\n");
    }

    private static String getLogTag() {
        return ShareUtils.class.getSimpleName();
    }
}
