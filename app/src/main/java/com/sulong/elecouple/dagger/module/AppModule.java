package com.sulong.elecouple.dagger.module;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.sulong.elecouple.dagger.scope.ApplicationScope;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.utils.JsonParser;
import com.sulong.elecouple.web.WebClient;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;

/**
 * Application模块，全局共享
 * Created by ydh on 2016/6/24.
 */
@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationScope
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    public WebClient provideWebClient() {
        return new WebClient();
    }

    @Provides
    @ApplicationScope
    public LoginManager provideLoginManager() {
        return LoginManager.getInstance();
    }

    @Provides
    @ApplicationScope
    public EventBus provideDefaultEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @ApplicationScope
    public JsonParser provideJsonParser() {
        return new JsonParser();
    }

    @Provides
    @ApplicationScope
    public LocalBroadcastManager provideLocalBroadcastManager(Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    @Provides
    @ApplicationScope
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

}