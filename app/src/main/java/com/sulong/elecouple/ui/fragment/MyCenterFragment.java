package com.sulong.elecouple.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sulong.elecouple.R;
import com.sulong.elecouple.entity.AreaItem;
import com.sulong.elecouple.entity.ProvinceDataItem;
import com.sulong.elecouple.ui.dialog.ChangeAddressDialogFragment;
import com.sulong.elecouple.utils.CityDataHelper;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ydh on 2017/4/7.
 */
public class MyCenterFragment extends BaseFragment {

    private View rootView;
    private Unbinder unbinder;
//    private EventBus mEventBus;
    private List<ProvinceDataItem.ProvinceData> provinceData;
    private Map<String, List<ProvinceDataItem.ProvinceData.CityData>> provinceCityMap;
    private Map<String, List<AreaItem>> cityDistrictMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_center, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
//        mEventBus.register(this);
        initViews();
        getData();
    }

    private void getData() {
        CityDataHelper helper = new CityDataHelper(getContext());
        provinceData = helper.getProvinceData();
        provinceCityMap = helper.getProvinceCityMap();
        cityDistrictMap = helper.getCityDistrictMap();
    }

    private void initViews() {
        final TextView tv_city = $(rootView, R.id.tv_city);
        rootView.findViewById(R.id.btn_choose_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangeAddressDialogFragment.Builder()
                        .setProvinceData(provinceData)
                        .setCityDataMap(provinceCityMap)
                        .setAreaDataMap(cityDistrictMap)
                        .setSelectedProvinceName("广东省")
                        .setSelectedCityId(35 + "")
                        .setSelectedAreaId(431 + "")
                        .setOnAddressChangeListener(new ChangeAddressDialogFragment.OnAddressChangeListener() {
                            @Override
                            public void onChanged(String province, String city, String district, String provinceId, String cityId, String areaId) {
                                tv_city.setText(province + " " + city + " " + district);
                            }
                        })
                        .create()
                        .show(getChildFragmentManager(), "ChangeAddressDialogFragment");
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

}
