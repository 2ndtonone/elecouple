package com.sulong.elecouple.dagger.component;

import com.sulong.elecouple.dagger.module.LoginModule;
import com.sulong.elecouple.dagger.scope.ActivityScope;
import com.sulong.elecouple.ui.activity.LoginActivity;

import dagger.Component;

/**
 * Created by ydh on 2016/6/22.
 */
@Component(modules = {LoginModule.class}, dependencies = {AppComponent.class})
@ActivityScope
public interface LoginComponent {
    void inject(LoginActivity activity);
}