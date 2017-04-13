package com.sulong.elecouple.dagger.component;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.sulong.elecouple.dagger.module.AppModule;
import com.sulong.elecouple.dagger.scope.ApplicationScope;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.utils.JsonParser;
import com.sulong.elecouple.web.WebClient;

import dagger.Component;
import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;

/**
 * Created by ydh on 2016/6/24.
 */
@Component(modules = {AppModule.class})
@ApplicationScope
public interface AppComponent {
    Context applicationContext();

    WebClient webClient();

    LoginManager loginManager();

    EventBus defaultEventBus();

    JsonParser jsonParser();

    LocalBroadcastManager localBroadcastManager();

    OkHttpClient okHttpClient();
}