package com.sulong.elecouple.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.AreaItem;
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
 * Created by lq on 2016/11/26.
 */

public class ChangeAddressDialogFragment extends BaseDialogFragment implements OnWheelChangedListener {

    /** 省的WheelView控件 */
    @BindView(R.id.id_province)
    WheelView mProvince;
    /** 市的WheelView控件 */
    @BindView(R.id.id_city)
    WheelView mCity;
    /** 区的WheelView控件 */
    @BindView(R.id.id_area)
    WheelView mArea;

    /** 当前省的名称 */
    String mCurrentProvinceName;
    String mCurrentProvinceId;
    /** 当前市的名称 */
    String mCurrentCityName;
    String mCurrentCityId;
    /** 当前区的名称 */
    String mCurrentAreaName = "";
    String mCurrentAreaId = "";

    Builder mBuilder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        createContentView(dialog);
        configDialogSize(dialog);
        return dialog;
    }

    private void createContentView(Dialog dialog) {
        ViewGroup parentView = dialog.getWindow() != null ? (ViewGroup) dialog.getWindow().getDecorView() : null;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_address, parentView, false);
        dialog.setContentView(dialogView);
        ButterKnife.bind(this, dialogView);
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);

        mProvince.setViewAdapter(new ListAreaWheelAdapter(getActivity(), mBuilder.provinceData));
        mProvince.setVisibleItems(6);
        mCity.setVisibleItems(6);
        mArea.setVisibleItems(6);

        updateCities();
        initSelection();
    }

    @OnClick(R.id.btn_myinfo_cancel)
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.btn_myinfo_sure)
    void selectCurrentAddress() {
        if (mBuilder.onAddressChangeListener != null) {
            if (mBuilder.areaDataMap.containsKey(mCurrentCityName)) {
                int pCurrent = mArea.getCurrentItem();
                AreaItem areaItem = mBuilder.areaDataMap.get(mCurrentCityName).get(pCurrent);
                mCurrentAreaName = areaItem.area_name;
                mCurrentAreaId = areaItem.area_id;
            }
            mBuilder.onAddressChangeListener.onChanged(mCurrentProvinceName, mCurrentCityName,
                    mCurrentAreaName, mCurrentProvinceId, mCurrentCityId, mCurrentAreaId);
        }
        dismiss();
    }

    /** change事件的处理 */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            updateAreas();
        } else if (wheel == mArea) {
            mCurrentAreaName = mBuilder.areaDataMap.get(mCurrentCityName).get(newValue).area_name;
            mCurrentAreaId = mBuilder.areaDataMap.get(mCurrentCityName).get(newValue).area_id;
        }
    }

    /** 根据当前的省，更新市WheelView的信息 */
    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvinceName = mBuilder.provinceData.get(pCurrent).area_name;
        mCurrentProvinceId = mBuilder.provinceData.get(pCurrent).area_id;
        List<ProvinceDataItem.ProvinceData.CityData> cities = mBuilder.cityDataMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new ArrayList<>();
        }
        mCity.setViewAdapter(new ListAreaWheelAdapter(getActivity(), cities));
        mCity.setCurrentItem(0);
        updateAreas();
    }

    /** 根据当前的市，更新区WheelView的信息 */
    private void updateAreas() {
        int pCurrent = mCity.getCurrentItem();
        mCurrentCityName = mBuilder.cityDataMap.get(mCurrentProvinceName).get(pCurrent).area_name;
        mCurrentCityId = mBuilder.cityDataMap.get(mCurrentProvinceName).get(pCurrent).area_id;
        List<AreaItem> areas = mBuilder.areaDataMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new ArrayList<>();
        }
        mArea.setViewAdapter(new ListAreaWheelAdapter(getActivity(), areas));
        mArea.setCurrentItem(0);
    }

    void initSelection() {
        if (!TextUtils.isEmpty(mBuilder.selectedProvinceName)) {
            for (int i = 0; i < mBuilder.provinceData.size(); i++) {
                ProvinceDataItem.ProvinceData province = mBuilder.provinceData.get(i);
                if (TextUtils.equals(province.area_name, mBuilder.selectedProvinceName)) {
                    mProvince.setCurrentItem(i);
                    updateCities();
                }
            }
        }
        if (!TextUtils.isEmpty(mBuilder.selectedCityId)) {
            List<ProvinceDataItem.ProvinceData.CityData> cities = mBuilder.cityDataMap.get(mCurrentProvinceName);
            if (cities == null) {
                cities = new ArrayList<>();
            }
            for (int i = 0; i < cities.size(); i++) {
                ProvinceDataItem.ProvinceData.CityData city = cities.get(i);
                if (TextUtils.equals(city.area_id, mBuilder.selectedCityId)) {
                    mCity.setCurrentItem(i);
                    updateAreas();
                }
            }
        }
        if (!TextUtils.isEmpty(mBuilder.selectedAreaId)) {
            List<AreaItem> areas = mBuilder.areaDataMap.get(mCurrentCityName);
            if (areas == null) {
                areas = new ArrayList<>();
            }
            for (int i = 0; i < areas.size(); i++) {
                AreaItem arae = areas.get(i);
                if (TextUtils.equals(arae.area_id, mBuilder.selectedAreaId)) {
                    mArea.setCurrentItem(i);
                }
            }
        }
    }

    void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    public static class Builder {
        OnAddressChangeListener onAddressChangeListener;
        /** 所有省 */
        List<ProvinceDataItem.ProvinceData> provinceData;
        /** key - 省 value - 市s */
        Map<String, List<ProvinceDataItem.ProvinceData.CityData>> cityDataMap;
        /** key - 市 values - 区s */
        Map<String, List<AreaItem>> areaDataMap;
        String selectedProvinceName;
        String selectedCityId;
        String selectedAreaId;

        public Builder setSelectedProvinceName(String selectedProvinceId) {
            this.selectedProvinceName = selectedProvinceId;
            return this;
        }

        public Builder setSelectedCityId(String selectedCityId) {
            this.selectedCityId = selectedCityId;
            return this;
        }

        public Builder setSelectedAreaId(String selectedAreaId) {
            this.selectedAreaId = selectedAreaId;
            return this;
        }

        public Builder setProvinceData(List<ProvinceDataItem.ProvinceData> data) {
            this.provinceData = data;
            return this;
        }

        public Builder setCityDataMap(Map<String, List<ProvinceDataItem.ProvinceData.CityData>> data) {
            this.cityDataMap = data;
            return this;
        }

        public Builder setAreaDataMap(Map<String, List<AreaItem>> data) {
            this.areaDataMap = data;
            return this;
        }

        public Builder setOnAddressChangeListener(OnAddressChangeListener listener) {
            this.onAddressChangeListener = listener;
            return this;
        }

        public ChangeAddressDialogFragment create() {
            ChangeAddressDialogFragment dialog = new ChangeAddressDialogFragment();
            dialog.setBuilder(this);
            return dialog;
        }
    }

    public interface OnAddressChangeListener {
        void onChanged(String province, String city, String district, String provinceId, String cityId, String areaId);
    }
}