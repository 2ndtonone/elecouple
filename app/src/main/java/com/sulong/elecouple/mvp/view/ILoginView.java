package com.sulong.elecouple.mvp.view;

import com.sulong.elecouple.enums.LoginType;

/**
 * Created by ydh on 2016/6/21.
 */
public interface ILoginView extends IBaseViewWithWebRequest, IBaseViewWithLoading {
    void showLoadingView();

    void hideLoadingView();

    void showPassword(boolean shown);

    void showWrongInputLoginName();

    void showWrongInputPassword();

    void fillLoginFieldsIfNeed(String loginName, String loginPassword, boolean rememberPassword);

    void exit();

    void showWeChatNotInstalled();

    void showLoginFailed(LoginType type);

    void enableSendVerifyCodeButtons(boolean enabled);

    void updateLoginButtonUi();

    void showInputPhoneHasNotRegistered();

    void showVerifyCodeSentMessage(boolean isSmsCode);

    void showTimeCountDownView(boolean shown);

    void markInputPhoneRegistrationState(boolean registered);

}