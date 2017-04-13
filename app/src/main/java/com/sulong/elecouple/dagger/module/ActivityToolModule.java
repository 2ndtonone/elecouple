package com.sulong.elecouple.dagger.module;

import android.app.Activity;

import com.sulong.elecouple.dagger.scope.ActivityScope;
import com.sulong.elecouple.utils.SystemBarTintManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ydh on 2016/8/3.
 */
@Module
public class ActivityToolModule {

    private Activity mActivity;

    public ActivityToolModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public SystemBarTintManager provideSystemBarTintManager() {
        return new SystemBarTintManager(mActivity);
    }

}