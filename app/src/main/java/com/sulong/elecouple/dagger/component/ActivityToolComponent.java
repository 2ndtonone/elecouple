package com.sulong.elecouple.dagger.component;

import com.sulong.elecouple.dagger.module.ActivityToolModule;
import com.sulong.elecouple.dagger.scope.ActivityScope;
import com.sulong.elecouple.utils.SystemBarTintManager;

import dagger.Component;

/**
 * Created by ydh on 2016/8/3.
 */
@Component(modules = {ActivityToolModule.class})
@ActivityScope
public interface ActivityToolComponent {
    SystemBarTintManager systemBarTintManager();
}