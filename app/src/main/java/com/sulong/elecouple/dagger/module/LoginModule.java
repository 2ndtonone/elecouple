package com.sulong.elecouple.dagger.module;

import android.content.Context;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.scope.ActivityScope;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.mvp.model.impl.LoginModel;
import com.sulong.elecouple.mvp.model.interfaces.ILoginModel;
import com.sulong.elecouple.mvp.presenter.impl.LoginPresenter;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;
import com.sulong.elecouple.utils.JpushHelper;
import com.sulong.elecouple.web.WebClient;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
    public JpushHelper provideJpushHelper(Context context) {
        return new JpushHelper(context);
    }

    @Provides
    @ActivityScope
    public IWXAPI provideWxapi(Context context) {
        return WXAPIFactory.createWXAPI(context, context.getString(R.string.wechat_app_id));
    }

    @Provides
    @ActivityScope
    public ILoginModel provideLoginModel(WebClient client, JpushHelper jpushHelper, IWXAPI wxapi,
                                         LoginManager loginManager, EventBus eventBus) {
        return new LoginModel(client, jpushHelper, wxapi, loginManager, eventBus);
    }

    @Provides
    @ActivityScope
    public ILoginPresenter provideLoginPresenter(ILoginView loginView, ILoginModel loginModel, LoginManager loginManager) {
        return new LoginPresenter(loginView, loginModel, loginManager);
    }
}