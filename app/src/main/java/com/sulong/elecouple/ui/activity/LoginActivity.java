package com.sulong.elecouple.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.dagger.ModuleProvider;
import com.sulong.elecouple.dagger.component.DaggerLoginComponent;
import com.sulong.elecouple.dagger.component.holder.ComponentHolder;
import com.sulong.elecouple.entity.LocationInfo;
import com.sulong.elecouple.enums.LoginType;
import com.sulong.elecouple.eventbus.RegisterSuccessEvent;
import com.sulong.elecouple.eventbus.WXAuthResultEvent;
import com.sulong.elecouple.mvp.presenter.interfaces.ILoginPresenter;
import com.sulong.elecouple.mvp.view.ILoginView;
import com.sulong.elecouple.ui.views.CustomToast;
import com.sulong.elecouple.ui.views.TimeCountDownTextView;
import com.sulong.elecouple.utils.Debug;
import com.sulong.elecouple.utils.JpushHelper;
import com.sulong.elecouple.utils.PersistUtils;
import com.sulong.elecouple.utils.StringUtils;
import com.sulong.elecouple.utils.Utility;
import com.sulong.elecouple.wrapper.SimpleTextWatcher;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity implements ILoginView {

    public static final String EXTRA_LOGIN_NAME = "EXTRA_LOGIN_NAME";
    public static final String EXTRA_LOGIN_PASSWORD = "EXTRA_LOGIN_PASSWORD";

    static final int REQUEST_CODE_MODIFY_PASSWORD = 1;
    static final int REQUEST_CODE_RESET_PASSWORD = 2;
    static final int REQUEST_CODE_BIND_PHONE = 3;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_forget_password)
    TextView btn_forget_password;
    @BindView(R.id.remain_time)
    TimeCountDownTextView remain_time;
    @BindView(R.id.tab_account_login)
    View tab_account_login;
    @BindView(R.id.tab_phone_login)
    View tab_phone_login;
    @BindView(R.id.account_login_container)
    View account_login_container;
    @BindView(R.id.phone_login_container)
    View phone_login_container;
    @BindView(R.id.btn_clear_login_account)
    View btn_clear_login_account;
    @BindView(R.id.btn_clear_login_phone)
    View btn_clear_login_phone;
    @BindView(R.id.send_verify_code_container)
    View send_verify_code_container;
    @BindView(R.id.btn_sms_verify)
    View btn_sms_verify;
    @BindView(R.id.btn_audio_verify)
    View btn_audio_verify;
    @BindView(R.id.btn_login)
    View btn_login;
    @BindView(R.id.et_login_account)
    EditText et_login_account;
    @BindView(R.id.et_login_password)
    EditText et_login_password;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_verify_code)
    EditText et_verify_code;
    @BindView(R.id.cb_remember_password)
    CheckBox cb_remember_password;
    @BindView(R.id.password_show_switch_image)
    ImageView password_show_switch_image;

    String mLoginName = null;
    String mLoginPassword = null;

    boolean isReceivedWxAuthResultBeforeResume = false;
    boolean isInputPhoneRegistered = false;

    private Class clazz;
    private Bundle bundle;

    @Inject
    ILoginPresenter mLoginPresenter;
    @Inject
    EventBus mEventBus;
    @Inject
    JpushHelper mJpushHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initMvp();
        parseArguments();
        initViews();
        mEventBus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, "umAcountLogin");
        hideLoadingViewIfNotReceivedWxAuthResultBeforeResume();
        isReceivedWxAuthResultBeforeResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESET_PASSWORD:

            break;
        }
    }

    @Override
    protected boolean enableLoadingView() {
        return true;
    }

    public void onEventMainThread(RegisterSuccessEvent event) {
        finish();
    }

    public void onEventMainThread(WXAuthResultEvent event) {
        String wechatCode = null;
        if (event != null && event.baseResp != null) {
            if (event.baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH
                    && event.baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                wechatCode = ((SendAuth.Resp) event.baseResp).code;
            }
        }
        Debug.li(getLogTag(), "wechatCode:" + wechatCode);
        // 接收到此事件时，如果Activity还没有走到onResume，设置此值以在onResume中不执行隐藏加载对话框
        // 如果已经Activity已经走过onResume，此值无影响
        isReceivedWxAuthResultBeforeResume = !isResumed;
        mLoginPresenter.loginByWeChat(wechatCode, mJpushHelper.getRegistrationID(), PersistUtils.getLocationInfo(this));
    }

    void initMvp() {
        DaggerLoginComponent.builder()
                .loginModule(ModuleProvider.getInstance().provideLoginModule(this))
                .appComponent(ComponentHolder.getAppComponent())
                .build()
                .inject(this);
    }

    void parseArguments() {
        mLoginName = getIntent().getStringExtra(EXTRA_LOGIN_NAME);
        mLoginPassword = getIntent().getStringExtra(EXTRA_LOGIN_PASSWORD);

        clazz = (Class) getIntent().getSerializableExtra("classStr");
        bundle = getIntent().getExtras();
    }

    void initViews() {
        // 状态栏颜色
        setTintColor(getResources().getColor(R.color.general_bg));

        // 设置标题
        title.setText(R.string.login);

        // 默认隐藏密码
        mLoginPresenter.showPassword(false);

        // 默认显示账号登陆
        switchPage(true);

        initLoginAccountEditText();
        initLoginPasswordEditText();
        initPhoneEditText();
        initVerifyCodeEditText();
        initTimeCountDownView();
        autoLoginOrFillLoginFields();
    }

    void initLoginAccountEditText() {
        // 用户名输入框提示
        et_login_account.setHint(R.string.hint_user_account);

        // 输入了用户名就显示清除用户名按钮
        et_login_account.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateLoginButtonUi();
                btn_clear_login_account.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    void initLoginPasswordEditText() {
        // 密码输入框输入监听
        et_login_password.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateLoginButtonUi();
                // 限制密码输入。由于在代码里手动控制了密码的显示与隐藏，在xml中设置EditText的digits属性会失效，只能在此限制输入的字符。
                Utility.restrictPasswordInput(getApplicationContext(), s);
            }
        });

        // 点击输入密码的软键盘回车键就登陆
        et_login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                loginByAccount();
                return true;
            }
        });
    }

    void initPhoneEditText() {
        // 输入了手机号就显示清除手机号按钮
        // 输入完手机号检测该账号是否注册过
        et_phone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                btn_clear_login_phone.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                String inputText = s.toString();
                if (Utility.isPhoneNumber(inputText)) {
                    mLoginPresenter.checkPhoneHasRegistered(inputText);
                } else {
                    enableSendVerifyCodeButtons(false);
                    updateLoginButtonUi();
                }
            }
        });
    }

    void initVerifyCodeEditText() {
        // 验证码输入4位后允许点击登陆按钮
        et_verify_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateLoginButtonUi();
            }
        });
    }

    void initTimeCountDownView() {
        // 倒计时结束后显示发送验证码按钮
        remain_time.setOnTimeCountDownListener(new TimeCountDownTextView.OnTimeCountDownListener() {
            @Override
            public void onTimeCountDownFinished() {
                showTimeCountDownView(false);
            }
        });
    }

    void autoLoginOrFillLoginFields() {
        // 如果外部传递了登录名和密码，就自动登陆
        if (!StringUtils.isEmpty(mLoginName) && !StringUtils.isEmpty(mLoginPassword)) {
            et_login_account.setText(mLoginName);
            et_login_password.setText(mLoginPassword);
            loginByAccount();
        } else {
            // 自动填入登录信息
            mLoginPresenter.fillLoginFieldsIfNeed();
        }
    }

    void switchPage(boolean isAccountLogin) {
        tab_account_login.setSelected(isAccountLogin);
        tab_phone_login.setSelected(!isAccountLogin);
        account_login_container.setVisibility(isAccountLogin ? View.VISIBLE : View.GONE);
        phone_login_container.setVisibility(!isAccountLogin ? View.VISIBLE : View.GONE);
        btn_forget_password.setVisibility(isAccountLogin ? View.VISIBLE : View.GONE);
        updateLoginButtonUi();
    }

    boolean isInAccountLoginPage() {
        return tab_account_login.isSelected();
    }

    void loginByAccount() {
        String loginName = getInputText(et_login_account);
        String loginPassword = getInputText(et_login_password);
        String registrationId = mJpushHelper.getRegistrationID();
        LocationInfo locationInfo = PersistUtils.getLocationInfo(this);
        boolean rememberPassword = cb_remember_password.isChecked();
        mLoginPresenter.loginByAccount(loginName, loginPassword, registrationId, locationInfo, rememberPassword);
    }

    void loginByVerifyCode() {
        String loginPhone = getInputText(et_phone);
        String verifyCode = getInputText(et_verify_code);
        String registrationId = mJpushHelper.getRegistrationID();
        LocationInfo locationInfo = PersistUtils.getLocationInfo(this);
        mLoginPresenter.loginByVerifyCode(loginPhone, verifyCode, registrationId, locationInfo);
    }

    String getInputText(EditText editText) {
        return editText.getText().toString().replaceAll(" ", "");
    }

    void hideLoadingViewIfNotReceivedWxAuthResultBeforeResume() {
        if (!isReceivedWxAuthResultBeforeResume) {
            hideLoadingView();
        }
    }

    @OnClick(R.id.remember_password_container)
    void toggleRememberPassword() {
        System.out.println("shit happened");
        cb_remember_password.setChecked(!cb_remember_password.isChecked());
    }

    @OnClick(R.id.btn_login)
    void onLoginButtonClick() {
        if (isInAccountLoginPage()) {
            loginByAccount();
        } else {
            loginByVerifyCode();
        }
    }

    @OnClick(R.id.btn_sms_verify)
    void fetchSmsVerifyCode() {
        getVerifyCode(true);
    }

    @OnClick(R.id.btn_audio_verify)
    void fetchAudioVerifyCode() {
        getVerifyCode(false);
    }

    @OnClick(R.id.tab_account_login)
    void switchToAccountLoginPage() {
        switchPage(true);
    }

    @OnClick(R.id.tab_phone_login)
    void switchToPhoneLoginPage() {
        switchPage(false);
    }

    @OnClick(R.id.btn_wx_login)
    void getWeChatToken() {
        mLoginPresenter.getWeChatToken(getString(R.string.wechat_app_id));
    }

    @OnClick(R.id.password_show_switch)
    void toggleShowPassword() {
        mLoginPresenter.toggleShowPassword();
    }

    @OnClick(R.id.btn_clear_login_account)
    void clearInputLoginAccount() {
        et_login_account.setText("");
    }

    @OnClick(R.id.btn_clear_login_phone)
    void clearInputLoginPhone() {
        et_phone.setText("");
    }

    void getVerifyCode(boolean isSmsCode) {
        String inputPhone = getInputText(et_phone);
        mLoginPresenter.getVerifyCode(inputPhone, isSmsCode);
    }

    @OnClick(R.id.btn_register)
    void goToRegisterPage() {
    }

    @OnClick(R.id.btn_forget_password)
    void goToForgetPasswordPage() {
    }

    void verifyPhoneThenModifyPassword(String boundPhone, String token) {
    }

    void bindPhone(String token) {
    }

    void goToBindPhoneSuccessPage(String phone) {
//        Intent intent = new Intent(this, BindPhoneSuccessActivity.class);
//        intent.putExtra(BindPhoneSuccessActivity.EXTRA_PHONE, phone);
//        startActivity(intent);
    }

    void goToModifyPasswordPage(String phone, String verifyCode, String token) {
//        Intent intent = new Intent(this, ModifyPasswordActivity.class)
//                .putExtra(ModifyPasswordActivity.EXTRA_TEMP_TOKEN, token)
//                .putExtra(ModifyPasswordActivity.EXTRA_ACCOUNT_TYPE, ModifyPasswordActivity.ACCOUNT_TYPE_USER)
//                .putExtra(ModifyPasswordActivity.EXTRA_PHONE, phone)
//                .putExtra(ModifyPasswordActivity.EXTRA_VERIFY_CODE, verifyCode);
//        startActivity(intent);
    }

    void goToResetPasswordPage(String phone, String verifyCode) {
    }

    // >>>>>>>>>>>>>>> View interface

    @Override
    public void showPassword(boolean shown) {
        password_show_switch_image.setImageResource(shown ? R.drawable.ic_password_visible : R.drawable.ic_password_invisible);
        int oldSelection = et_login_password.getSelectionStart();
        et_login_password.setInputType(shown ?
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );
        et_login_password.setSelection(oldSelection);
    }

    @Override
    public void showWrongInputLoginName() {
        CustomToast.showToast(R.string.phone_number_not_empty);
    }

    @Override
    public void showWrongInputPassword() {
        CustomToast.showToast(R.string.password_not_empty);
    }

    @Override
    public void exit() {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void showWeChatNotInstalled() {
        CustomToast.showToast(R.string.toast_no_install_wx);
    }

    @Override
    public void fillLoginFieldsIfNeed(String loginName, String loginPassword, boolean rememberPassword) {
        // 自动填入缓存过的登录名
        et_login_account.setText(loginName);
        if (!StringUtils.isEmpty(loginName)) {
            et_login_account.setSelection(loginName.length());
        }

        // 如果上次登录选择过记住密码，就自动填入密码
        cb_remember_password.setChecked(rememberPassword);
        if (rememberPassword) {
            et_login_password.setText(loginPassword);
        }
    }

    @Override
    public void showLoginFailed(LoginType type) {
        switch (type) {
            case ACCOUNT:
            case VERIFY_CODE:
                CustomToast.showToast(R.string.login_failed);
                break;
            case WECHAT:
                CustomToast.showToast(R.string.toast_wechat_login_failed);
                break;
        }
    }

    @Override
    public void enableSendVerifyCodeButtons(boolean enabled) {
        btn_sms_verify.setEnabled(enabled);
        btn_audio_verify.setEnabled(enabled);
    }

    @Override
    public void updateLoginButtonUi() {
        if (isInAccountLoginPage()) { // 用户名或手机号登陆
            String inputAccount = getInputText(et_login_account);
            String inputPassword = getInputText(et_login_password);
            boolean enableLoginButton = !StringUtils.isEmpty(inputAccount) && !StringUtils.isEmpty(inputPassword);
            btn_login.setEnabled(enableLoginButton);
        } else { // 手机验证码登陆
            String inputPhone = getInputText(et_phone);
            String inputVerifyCode = getInputText(et_verify_code);
            boolean enableLoginButton = Utility.isPhoneNumber(inputPhone)
                    && isInputPhoneRegistered
                    && inputVerifyCode.length() >= 4;
            btn_login.setEnabled(enableLoginButton);
        }
    }

    @Override
    public void showInputPhoneHasNotRegistered() {
        CustomToast.showToast(R.string.toast_input_phone_has_not_registered);
    }

    @Override
    public void showVerifyCodeSentMessage(boolean isSmsCode) {
        int msgStringId = isSmsCode ? R.string.toast_verification_code_send_success : R.string.toast_verification_code_audio;
        CustomToast.showToast(msgStringId);
    }

    @Override
    public void showTimeCountDownView(boolean shown) {
        remain_time.setVisibility(shown ? View.VISIBLE : View.GONE);
        send_verify_code_container.setVisibility(!shown ? View.VISIBLE : View.GONE);
        if (shown) {
            remain_time.startCount();
        }
    }

    @Override
    public void markInputPhoneRegistrationState(boolean registered) {
        isInputPhoneRegistered = registered;
    }

    // <<<<<<<<<<<<<<<<< View interface
}