package com.sulong.elecouple.dagger.component.holder;

import com.sulong.elecouple.dagger.component.AppComponent;

/**
 * Created by ydh on 2016/7/26.
 */
public class ComponentHolder {

    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    public static void setAppComponent(AppComponent component) {
        sAppComponent = component;
    }
}