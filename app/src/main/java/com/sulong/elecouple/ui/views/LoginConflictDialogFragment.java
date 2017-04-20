package com.sulong.elecouple.ui.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.login.LoginManager;
import com.sulong.elecouple.ui.activity.LoginActivity;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.Utility;

/**
 * Created by ydh on 2015/11/24.
 */
public class LoginConflictDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "LoginConflictDialogFragment";
    public static final String ARGUMENTS_TEXT_CONTENT = "arguments_text_content";
    public static final String TYPE_USER_LOGIN = "user_login";
    public static final String TYPE_VENDOR_LOGIN = "type_vendor_login";
    public static final String LOGIN_TYPE = "login_type";
    public String login_type;
    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView dialog_content_txt_top;
    private TextView mContentTxt;
    private TextView mTitleTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = android.app.DialogFragment.STYLE_NORMAL;
        int theme = R.style.DimPanel;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.ah_common_dialog, container, false);
        mTitleTxt = (TextView) layout.findViewById(R.id.dialog_title_txt);
        mContentTxt = (TextView) layout.findViewById(R.id.dialog_content_txt);
        mLeftBtn = (Button) layout.findViewById(R.id.dialog_left_btn);
        mRightBtn = (Button) layout.findViewById(R.id.dialog_right_btn);
        dialog_content_txt_top = (TextView) layout.findViewById(R.id.dialog_content_txt_top);

        layout.findViewById(R.id.dialog_right_btn).setOnClickListener(this);
        layout.findViewById(R.id.dialog_left_btn).setOnClickListener(this);
        mContentTxt.setOnClickListener(this);
        dialog_content_txt_top.setOnClickListener(this);

        hideTitle();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String content = bundle.getString(ARGUMENTS_TEXT_CONTENT);
            if (!TextUtils.isEmpty(content)) {
                mContentTxt.setText(content);
            } else {
                mContentTxt.setText(R.string.login_conflict_msg);
            }
            login_type = bundle.getString(LOGIN_TYPE);
        } else {
            mContentTxt.setText(R.string.login_conflict_msg);
        }
        mLeftBtn.setText(R.string.cancel);
        mRightBtn.setText(R.string.login);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(Utility.dip2px(getActivity(), 300), WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (TextUtils.equals(TYPE_USER_LOGIN, login_type) && !LoginManager.getInstance().isLogin()) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(ConstantUtils.ACTION_CLOSE_PAGE_WHICH_NEED_LOGIN));
            getActivity().finish();
        }
        if (TextUtils.equals(TYPE_VENDOR_LOGIN, login_type) && LoginManager.getInstance().isLogin()) {
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_right_btn:
                goToLoginPage();
                break;
            case R.id.dialog_left_btn:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void hideTitle() {
        mTitleTxt.setVisibility(View.GONE);
    }

    private void goToLoginPage() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public String getLogTag() {
        return TAG;
    }

}
