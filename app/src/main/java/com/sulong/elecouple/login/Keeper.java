package com.sulong.elecouple.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.utils.ConstantUtils;

/**
 * <p>用户登陆数据读写{@link SharedPreferences}都用此类，并且只应该被{@link LoginManager}调用</p>
 * <p>每个数据项都封装单独的读写方法是为了避免某一项数据多处读写时都要引用key，
 * 万一key或者读取的默认值以后要改变就要修改多处，所以统一调用这里封装好的方法就行。
 * 同时也便于单元测试，将{@link SharedPreferences}与{@link LoginManager}隔开了</p>
 */
public class Keeper {

    Context mContext;

    public Keeper(Context context) {
        mContext = context.getApplicationContext();
    }

    SharedPreferences getUserPrefs() {
        return mContext.getSharedPreferences(ConstantUtils.LOGIN_PREF, Context.MODE_PRIVATE);
    }

    SharedPreferences getForeverUserPrefs() {
        return mContext.getSharedPreferences(ConstantUtils.PREF_NAME_FOREVER_USER_INFO, Context.MODE_PRIVATE);
    }

    // >>>>>>>>>>>> 读取用户信息

    public int getUserId() {
        return getUserPrefs().getInt(ConstantUtils.KEY_USER_ID, ConstantUtils.INVALID_ID);
    }

    public void setUserId(int id) {
        getUserPrefs().edit().putInt(ConstantUtils.KEY_USER_ID, id).apply();
    }

    public String getUserAvatar() {
        return getUserPrefs().getString(ConstantUtils.KEY_MEMBER_AVATAR, "");
    }

    public String getUserName() {
        return getUserPrefs().getString(ConstantUtils.KEY_USER_NAME, "");
    }

    public void setUserName(String version) {
        getUserPrefs().edit().putString(ConstantUtils.KEY_USER_NAME, version).apply();
    }

    public String getToken() {
        return getUserPrefs().getString(ConstantUtils.KEY_TOKEN, "");
    }

    public void setToken(String token) {
        getUserPrefs().edit().putString(ConstantUtils.KEY_TOKEN, token).apply();
    }

    public String getMemberToken() {
        return getUserPrefs().getString(ConstantUtils.KEY_MEMBER_TOKEN, "");
    }

    public void setMemberToken(String token) {
        getUserPrefs().edit().putString(ConstantUtils.KEY_MEMBER_TOKEN, token).apply();
    }

    public String getInvitationCode() {
        return getUserPrefs().getString(ConstantUtils.PREF_KEY_INVITATION_CODE, "");
    }

    public void setInvitationCode(String invitationCode) {
        getUserPrefs().edit().putString(ConstantUtils.PREF_KEY_INVITATION_CODE, invitationCode).apply();
    }

    public String getNickName() {
        return getUserPrefs().getString(ConstantUtils.KEY_NICKNAME, "");
    }

    public void setNickName(String nickName) {
        getUserPrefs().edit().putString(ConstantUtils.KEY_NICKNAME, nickName).apply();
    }

    public String getBindingPhone() {
        return getUserPrefs().getString(ConstantUtils.PREF_KEY_BINDING_PHONE, "");
    }

    // <<<<<<<<<<< 读取用户信息

    // >>>>>>>>>>>> 设置用户信息

    public void setBindingPhone(String bindingPhone) {
        getUserPrefs().edit().putString(ConstantUtils.PREF_KEY_BINDING_PHONE, bindingPhone).apply();
    }

    public LoginType getLoginType() {
        int loginTypeOrdinal = getUserPrefs().getInt(ConstantUtils.PREF_KEY_LOGIN_TYPE, -1);
        LoginType[] values = LoginType.values();
        if (loginTypeOrdinal < 0 || loginTypeOrdinal >= LoginType.values().length) {
            return null;
        }
        return values[loginTypeOrdinal];
    }

    public void setLoginType(LoginType loginType) {
        getUserPrefs().edit().putInt(ConstantUtils.PREF_KEY_LOGIN_TYPE, loginType.ordinal()).apply();
    }

    public String getRememberedLoginName() {
        return getForeverUserPrefs().getString(ConstantUtils.PREF_KEY_REMEMBERED_LOGIN_NAME, "");
    }

    public void setRememberedLoginName(String loginName) {
        getForeverUserPrefs().edit().putString(ConstantUtils.PREF_KEY_REMEMBERED_LOGIN_NAME, loginName).apply();
    }

    public String getEncryptedPassword() {
        return getForeverUserPrefs().getString(ConstantUtils.PREF_KEY_PASSWORD, "");
    }

    public void setEncryptedPassword(String encryptedPassword) {
        getForeverUserPrefs().edit().putString(ConstantUtils.PREF_KEY_PASSWORD, encryptedPassword).apply();
    }

    public String getEncryptedPasswordVersion() {
        return getForeverUserPrefs().getString(ConstantUtils.PREF_ENCRYPT_VERSION, "");
    }

    public void setEncryptedPasswordVersion(String version) {
        getForeverUserPrefs().edit().putString(ConstantUtils.PREF_ENCRYPT_VERSION, version).apply();
    }

    public boolean isRememberPassword() {
        String remember = getForeverUserPrefs().getString(ConstantUtils.PREF_KEY_REMEMBER, "0");
        return !"0".equals(remember);
    }

    public void setRememberPassword(boolean remember) {
        getForeverUserPrefs().edit().putString(ConstantUtils.PREF_KEY_REMEMBER, remember ? "1" : "0").apply();
    }

    public void clearUserLoginData() {
        getUserPrefs().edit().clear().apply();
    }

    public void setUserAvatarUrl(String avatarUrl) {
        getUserPrefs().edit().putString(ConstantUtils.KEY_MEMBER_AVATAR, avatarUrl).apply();
    }
    // <<<<<<<<<<< 设置用户信息
}