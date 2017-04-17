package com.sulong.elecouple.login;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sulong.elecouple.LocationApplication;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.entity.AggUser;
import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.eventbus.LogoutEvent;
import com.sulong.elecouple.utils.Aes;
import com.sulong.elecouple.utils.StringUtils;
import com.sulong.elecouple.utils.Utility;

import de.greenrobot.event.EventBus;

/**
 * (2015) All Rights Reserved.
 * Created by ydh on 7/24/15.
 */
public class LoginManager {

    private static LoginManager instance;

    AggUser mUserData;
    Context mContext;
    Keeper mKeeper;
    EventBus mEventBus;

    LoginManager() {
        this(LocationApplication.getInstance());
    }

    LoginManager(Context context) {
        mContext = context.getApplicationContext();
        mKeeper = new Keeper(context);
        mEventBus = ComponentHolder.getAppComponent().defaultEventBus();

        // 读取用户数据到内存中缓存起来
        mUserData = readUserDataFromLocalStorage();
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    /** 从本地存储中读取用户数据 */
    LoginItem.Data readUserDataFromLocalStorage() {
        LoginItem.Data data = new LoginItem.Data();
        data.user_id = mKeeper.getUserId();
        data.member_avatar_url = mKeeper.getUserAvatar();
        data.nickname = mKeeper.getNickName();
        data.username = mKeeper.getUserName();
        data.token = mKeeper.getToken();
        data.member_token = mKeeper.getMemberToken();
        data.invitation = mKeeper.getInvitationCode();
        data.member_mobile = mKeeper.getBindingPhone();
        return data;
    }
    // >>>>>>>>>>> 读取用户信息

    // >>> 读取服务端返回的用户信息
    public int getUserId() {
        if (mUserData != null) {
            return mUserData.user_id;
        } else {
            return mKeeper.getUserId();
        }
    }

    public String getUserName() {
        if (mUserData != null) {
            return mUserData.username;
        } else {
            return mKeeper.getUserName();
        }
    }

    public void setUserName(String loginName) {
        if (mUserData != null) {
            mUserData.username = loginName;
        }
        mKeeper.setUserName(loginName);
    }

    public String getUserAvatar() {
        if (mUserData != null) {
            return mUserData.member_avatar_url;
        } else {
            return mKeeper.getUserAvatar();
        }
    }

    public void setUserAvatar(String avatarImageUrl) {
        if (mUserData != null) {
            mUserData.member_avatar_url = avatarImageUrl;
        }
        mKeeper.setUserAvatarUrl(avatarImageUrl);
    }

    public String getToken() {
        if (mUserData != null) {
            return mUserData.token;
        } else {
            return mKeeper.getToken();
        }
    }

    public String getMemberToken() {
        if (mUserData != null) {
            return mUserData.member_token;
        } else {
            return mKeeper.getMemberToken();
        }
    }

    public String getInvitationCode() {
        if (mUserData != null) {
            return mUserData.invitation;
        } else {
            return mKeeper.getInvitationCode();
        }
    }

    public void setInvitationCode(String invitationCode) {
        if (mUserData != null) {
            mUserData.invitation = invitationCode;
        }
        mKeeper.setInvitationCode(invitationCode);
    }
    // <<< 读取服务端返回的用户信息

    // >>> 读取非服务端返回的用户信息

    public void saveUserKey(String token) {
        if (mUserData != null) {
            mUserData.token = token;
        }
        mKeeper.setToken(token);

    }
    public String getNickName() {
        if (mUserData != null) {
            return mUserData.nickname;
        } else {
            return mKeeper.getNickName();
        }
    }

    public void setNickName(String nickName) {
        if (mUserData != null) {
            mUserData.nickname = nickName;
        }
        mKeeper.setNickName(nickName);
    }

    public String getBindingPhone() {
        if (mUserData != null) {
            return mUserData.member_mobile;
        } else {
            return mKeeper.getBindingPhone();
        }
    }


    public String getRememberedLoginName() {
        return mKeeper.getRememberedLoginName();
    }

    // <<< 读取非服务器返回的用户信息

    // <<<<<<<<<<< 读取用户信息

    // >>>>>>>>>>> 设置用户信息

    public void setRememberedLoginName(String loginName) {
        mKeeper.setRememberedLoginName(loginName);
    }

    public String getLoginPassword() {
        String encryptedPassword = mKeeper.getEncryptedPassword();
        String encryptVersion = mKeeper.getEncryptedPasswordVersion();
        String humanPassword = null;
        if (!StringUtils.isEmpty(encryptVersion)
                && !StringUtils.isEmpty(encryptedPassword)) {
            humanPassword = Aes.decrypt(encryptedPassword, Aes.KEY);
        }
        return humanPassword;
    }

    public boolean isRememberPassword() {
        return mKeeper.isRememberPassword();
    }

    public void setRememberPassword(boolean rememberPassword) {
        mKeeper.setRememberPassword(rememberPassword);
    }

    public boolean isLogin() {
        return !StringUtils.isEmpty(getToken());
    }

    public boolean isLoginByWeChat() {
        LoginType loginType = mKeeper.getLoginType();
        return loginType != null && loginType == LoginType.WECHAT && isLogin();
    }

    public void saveLoginPassword(String rawPassword) {
        String encryptPwd = Aes.encrypt(rawPassword, Aes.KEY);
        mKeeper.setEncryptedPassword(encryptPwd);
        mKeeper.setEncryptedPasswordVersion("v1");
    }

    public void setLoginType(LoginType loginType) {
        mKeeper.setLoginType(loginType);
    }

    public void saveUserLoginData(LoginItem.Data userLoginData) {
        this.mUserData = userLoginData;
        saveUserData(userLoginData);

    }

    private void saveUserData(AggUser userLoginData) {
        mKeeper.clearUserLoginData();
        mKeeper.setUserId(userLoginData.user_id);
        mKeeper.setUserName(userLoginData.username);
        mKeeper.setUserAvatarUrl(userLoginData.member_avatar_url);
        mKeeper.setToken(userLoginData.token);
        mKeeper.setMemberToken(userLoginData.member_token);
        mKeeper.setInvitationCode(userLoginData.invitation);
        mKeeper.setNickName(userLoginData.nickname);
        mKeeper.setBindingPhone(Utility.isPhoneNumber(userLoginData.member_mobile) ? userLoginData.member_mobile : "");
    }

    public void exitLogin() {
        exitUser();
    }

    private void exitUser() {
        // 清除用户登陆缓存的返回信息
        mUserData = null;
        mKeeper.clearUserLoginData();
        // 清理即时通讯相关的通知
//        IMNotificationManager.getInstance(mContext).cancelNewMsgNotification();
        // 注销环信登陆
//        HXSDKHelper.getInstance().logout();
        // 清除WebView的cookie
        clearCookie();
        // 发送用户注销事件
        mEventBus.post(new LogoutEvent());
    }

    void clearCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(null);
                cookieManager.removeSessionCookies(null);
            } else {
                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(LocationApplication.getInstance());
                cookieSyncMngr.startSync();
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                cookieSyncMngr.stopSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <<<<<<<<<<< 设置用户信息


}