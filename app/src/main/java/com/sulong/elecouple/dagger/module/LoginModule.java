package com.sulong.elecouple.dagger.module;

import android.content.Context;

import com.sulong.elecouple.dagger.scope.ActivityScope;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.impl.LoginModel;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.mvp.presenter.impl.LoginPresenter;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;
import com.sulong.elecouple.web.WebClient;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Created by ydh on 2016/6/22.
 */
@Module
public class LoginModule {

    private ILoginView mLoginView;

    public LoginModule(ILoginView loginView) {
        this.mLoginView = loginView;
    }

    @Provides
    @ActivityScope
    public ILoginView provideLoginView() {
        return this.mLoginView;
    }


    @Provides
    @ActivityScope
    public ILoginModel provideLoginModel(WebClient client,
                                         LoginManager loginManager, EventBus eventBus) {
        return new LoginModel(client,loginManager, eventBus);
    }

    @Provides
    @ActivityScope
    public ILoginPresenter provideLoginPresenter(ILoginView loginView, ILoginModel loginModel, LoginManager loginManager) {
        return new LoginPresenter(loginView, loginModel, loginManager);
    }
}