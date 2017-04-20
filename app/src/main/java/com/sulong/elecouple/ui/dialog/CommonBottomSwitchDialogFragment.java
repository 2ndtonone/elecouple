package com.sulong.elecouple.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.AreaItem;
import com.sulong.elecouple.entity.CommonSwitchDialogEntity;
import com.sulong.elecouple.entity.ProvinceDataItem;
import com.sulong.elecouple.ui.adapter.ListAreaWheelAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/**
 * Created by ydh on 2016/11/26.
 */

public class CommonBottomSwitchDialogFragment extends BaseDialogFragment implements OnWheelChangedListener {

    /**
     * 第一个的WheelView控件
     */
    @BindView(R.id.wheelview1)
    WheelView wheelview1;

    Builder mBuilder;
    String mSelectValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.AnimBottom);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        createContentView(dialog);
        return dialog;
    }

    private void createContentView(Dialog dialog) {
        ViewGroup parentView = dialog.getWindow() != null ? (ViewGroup) dialog.getWindow().getDecorView() : null;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_common_bottom_switch, parentView, false);
        dialog.setContentView(dialogView);
        ButterKnife.bind(this, dialogView);
        wheelview1.addChangingListener(this);

        wheelview1.setViewAdapter(new ListAreaWheelAdapter(getActivity(), mBuilder.list));
        wheelview1.setVisibleItems(3);
        wheelview1.setCurrentItem(mBuilder.selectedFirst);

    }

    @OnClick(R.id.tv_cancel)
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.tv_finish)
    void selectFinish() {
        if (mBuilder.onSelectChangeListener != null) {
            mBuilder.onSelectChangeListener.onChanged(mSelectValue);
        }
        dismiss();
    }

    /**
     * change事件的处理
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        mSelectValue = mBuilder.list.get(newValue).area_name;
    }

    void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    public static class Builder {
        OnSelectChangeListener onSelectChangeListener;
        List<AreaItem> list;
        int selectedFirst;

        public Builder setList(List<AreaItem> list) {
            this.list = list;
            return this;
        }

        public Builder setSelectedFirst(int selectedFirst) {
            this.selectedFirst = selectedFirst;
            return this;
        }
        public Builder setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
            this.onSelectChangeListener = onSelectChangeListener;
            return this;
        }

        public CommonBottomSwitchDialogFragment create() {
            CommonBottomSwitchDialogFragment dialog = new CommonBottomSwitchDialogFragment();
            dialog.setBuilder(this);
            return dialog;
        }
    }

    public interface OnSelectChangeListener {
        void onChanged( String selectValue);
    }
}