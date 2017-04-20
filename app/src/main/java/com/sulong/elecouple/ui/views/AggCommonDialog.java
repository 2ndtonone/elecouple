package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.ui.dialog.BaseDialogFragment;
import com.sulong.elecouple.utils.ConstantUtils;
import com.sulong.elecouple.utils.StringUtils;
import com.sulong.elecouple.utils.Utility;

/**
 * Agg (2014) All Rights Reserved.
 * Created by Herbert Dai on 7/2/15.
 */

public class AggCommonDialog extends BaseDialogFragment implements View.OnClickListener {
    private OpClickListener mOpListener;
    private ContentClickListener mContentClickListener;
    private Button mLeftBtn;
    private Button mRightBtn;
    private Context mContext;
    private EditText dialog_input_txt;
    private View dialog_edit_line;
    private TextView dialog_content_txt_top;
    private boolean showTwoContentClick;
    private View buttom_center_line;
    private View dialog_two_conten_line;
    private TextView mContentTxt;
    private TextView mTitleTxt;

    public AggCommonDialog() {
    }

    public void setContent(String progress) {
        mContentTxt.setText(progress);
    }

    public void setmContentClickListener(ContentClickListener mContentClickListener) {
        this.mContentClickListener = mContentClickListener;
    }

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

        buttom_center_line = layout.findViewById(R.id.buttom_center_line);
        dialog_two_conten_line = layout.findViewById(R.id.dialog_two_conten_line);

        dialog_input_txt = (EditText) layout.findViewById(R.id.dialog_input_txt);
        dialog_edit_line = layout.findViewById(R.id.dialog_edit_line);

        dialog_content_txt_top = (TextView) layout.findViewById(R.id.dialog_content_txt_top);


        layout.findViewById(R.id.dialog_right_btn).setOnClickListener(this);
        layout.findViewById(R.id.dialog_left_btn).setOnClickListener(this);
        mContentTxt.setOnClickListener(this);
        dialog_content_txt_top.setOnClickListener(this);


        Bundle bundle = getArguments();
        if (bundle != null) {
            int titleRes = bundle.getInt(ConstantUtils.KEY_TITLE_RES_ID, -1);
            int contentRes = bundle.getInt(ConstantUtils.KEY_CONTENT_RES_ID, -1);
            int hitRes = bundle.getInt(ConstantUtils.KEY_CONTENT_HINT_RES_ID, -1);
            String content = bundle.getString(ConstantUtils.KEY_CONTENT_RES_STR);
            int leftbtnRes = bundle.getInt(ConstantUtils.KEY_DLG_LEFT_BTN_RES_ID, -1);
            int rightBtnRes = bundle.getInt(ConstantUtils.KEY_DLG_RIGHT_BTN_RES_ID, -1);
            boolean showtitle = bundle.getBoolean(ConstantUtils.KEY_DLG_SHOWTITLE, true);
            String rightBtnString = bundle.getString(ConstantUtils.KEY_DLG_RIGHT_BTN_TEXT);
            String leftBtnString = bundle.getString(ConstantUtils.KEY_DLG_LEFT_BTN_TEXT);
            String oldName = bundle.getString(ConstantUtils.KEY_EDIT_TEXT);

            boolean showEdit = bundle.getBoolean(ConstantUtils.KEY_DLG_ONE_EDIT_MODLE, false);

            showTwoContentClick = bundle.getBoolean(ConstantUtils.KEY_DLG_SHOWTWOCONTENT_MODLE, false);

            if (!showtitle) {
                hideTitle();
            }

            if (showEdit) {
                dialog_input_txt.setVisibility(View.VISIBLE);
                dialog_edit_line.setVisibility(View.VISIBLE);
                if (hitRes > 0) {
                    dialog_input_txt.setHint(hitRes);
                }
                if (StringUtils.isNotBlank(oldName)) {
                    dialog_input_txt.setText(oldName);
                }
                mContentTxt.setVisibility(View.GONE);
            }

            if (showTwoContentClick) {
                dialog_content_txt_top.setVisibility(View.VISIBLE);
                dialog_two_conten_line.setVisibility(View.VISIBLE);
                mContentTxt.setVisibility(View.VISIBLE);
                mRightBtn.setVisibility(View.GONE);
                buttom_center_line.setVisibility(View.GONE);
            }

            if (StringUtils.isNotBlank(rightBtnString)) {
                mRightBtn.setText(rightBtnString);
            }

            if (StringUtils.isNotBlank(leftBtnString)) {
                mRightBtn.setText(leftBtnString);
            }

            if (titleRes > 0)
                mTitleTxt.setText(titleRes);
            if (contentRes > 0)
                mContentTxt.setText(contentRes);
            if (leftbtnRes > 0)
                mLeftBtn.setText(leftbtnRes);
            if (rightBtnRes > 0)
                mRightBtn.setText(rightBtnRes);

            if (content != null)
                mContentTxt.setText(content);
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(Utility.dip2px(getActivity(), 300), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOpListener = null;
    }

    public void setmOpListener(OpClickListener listener) {
        mOpListener = listener;
    }

    private void setTitle(int title) {
        mTitleTxt.setText(title);
    }

    private void hideTitle() {
        mTitleTxt.setVisibility(View.GONE);
    }

    private void setContent(int content) {
        mContentTxt.setText(content);
    }

    public String getEditText() {
        return dialog_input_txt.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_right_btn:
                if (mOpListener != null) {
                    mOpListener.onRightClicked();
                }
                break;
            case R.id.dialog_left_btn:
                if (mOpListener != null) {
                    mOpListener.onLeftClicked();
                }
                dismiss();
                break;
            case R.id.dialog_content_txt_top:
                if (mContentClickListener != null && showTwoContentClick) {
                    mContentClickListener.onTopClicked();
                    dismiss();
                }

                break;
            case R.id.dialog_content_txt:
                if (mContentClickListener != null && showTwoContentClick) {
                    mContentClickListener.onButtomClicked();
                    dismiss();
                }

                break;
        }

    }

    public interface OpClickListener {
        public void onLeftClicked();

        public void onRightClicked();
    }

    public interface ContentClickListener {
        public void onTopClicked();

        public void onButtomClicked();
    }

    public static class SimpleOpClickListener implements OpClickListener {
        @Override
        public void onLeftClicked() {
        }

        @Override
        public void onRightClicked() {
        }
    }

}
