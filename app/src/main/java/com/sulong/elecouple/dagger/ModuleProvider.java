package com.sulong.elecouple.dagger;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.sulong.elecouple.dagger.module.ActivityToolModule;
import com.sulong.elecouple.dagger.module.AppModule;
import com.sulong.elecouple.dagger.module.LoginModule;
import com.sulong.elecouple.mvp.view.ILoginView;

/**
 * Created by ydh on 2016/7/4.
 */
public class ModuleProvider {

    private static ModuleProvider sModuleProvider;

    public static ModuleProvider getInstance() {
        if (sModuleProvider == null) {
            sModuleProvider = new ModuleProvider();
        }
        return sModuleProvider;
    }

    @VisibleForTesting
    public static void setModuleProvider(ModuleProvider provider) {
        sModuleProvider = provider;
    }

    public AppModule provideAppModule(Application application) {
        return new AppModule(application);
    }

    public ActivityToolModule provideToolModule(Activity activity) {
        return new ActivityToolModule(activity);
    }

    public LoginModule provideLoginModule(ILoginView loginView) {
        return new LoginModule(loginView);
    }

}
