package com.sulong.elecouple.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.sulong.elecouple.entity.AllAreaDataItem;
import com.sulong.elecouple.entity.AreaItem;
import com.sulong.elecouple.entity.ProvinceDataItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lq on 2016/10/31.
 */
public class CityDataHelper {

    Context context;

    public CityDataHelper(Context context) {
        this.context = context;
    }

    /**
     * 获取指定城市的所有行政区名称
     *
     * @param cityName 城市名
     * @return 指定城市的所有行政区名称
     */
    public List<String> getDistricts(String cityName) {
        if (StringUtils.isEmpty(cityName)) {
            return null;
        }
        AllAreaDataItem allAreaDataItem = readAllCityDataFromFile();
        if (allAreaDataItem == null
                || allAreaDataItem.data == null
                || allAreaDataItem.data.city_district == null) {
            return null;
        }
        for (AllAreaDataItem.CityDistrict cityDistrict : allAreaDataItem.data.city_district) {
            if (StringUtils.equals(cityDistrict.area_name, cityName)) {
                List<String> districtList = new ArrayList<>();
                for (AllAreaDataItem.SubArea sub_area : cityDistrict.sub_area) {
                    districtList.add(sub_area.area_name);
                }
                return districtList;
            }
        }
        return null;
    }

    public AllAreaDataItem readAllCityDataFromFile() {
        String fileVersion = PreferenceManager.getDefaultSharedPreferences(context).getString(ConstantUtils.KEY_ALL_AREA_VERSION, "");
        String citiesJsonData;
        if (StringUtils.isEmpty(fileVersion)) {
            citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
        } else {
            File file = new File(context.getFilesDir() + File.separator + ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            if (file.exists()) {
                Debug.li(getLogTag(), "readStringFromApplicationFile" + "\n" + " path : " + file.getAbsolutePath());
                citiesJsonData = Utility.readStringFromApplicationFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            } else {
                Debug.li(getLogTag(), "readStringFromAssetFile" + "\n" + " path : " + file.getAbsolutePath());
                citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_ALL_AREA_JSON_NAME);
            }
        }
        if (StringUtils.isEmpty(citiesJsonData)) {
            return null;
        }
        return JsonParser.getInstance().fromJson(citiesJsonData, AllAreaDataItem.class);
    }

    // -------------------------------

    public ProvinceDataItem readCityDataFromFile() {
        String fileVersion = PreferenceManager.getDefaultSharedPreferences(context).getString(ConstantUtils.KEY_AREA_VERSION, "");
        String citiesJsonData;
        if (StringUtils.isEmpty(fileVersion)) {
            citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_AREA_JSON_NAME);
        } else {
            File file = new File(context.getFilesDir() + File.separator + ConstantUtils.FILE_AREA_JSON_NAME);
            if (file.exists()) {
                Debug.li(getLogTag(), "readStringFromApplicationFile" + "\n" + " path : " + file.getAbsolutePath());
                citiesJsonData = Utility.readStringFromApplicationFile(context, ConstantUtils.FILE_AREA_JSON_NAME);
            } else {
                Debug.li(getLogTag(), "readStringFromAssetFile" + "\n" + " path : " + file.getAbsolutePath());
                citiesJsonData = Utility.readStringFromAssetFile(context, ConstantUtils.FILE_AREA_JSON_NAME);
            }
        }
        if (StringUtils.isEmpty(citiesJsonData)) {
            return null;
        }
        return JsonParser.getInstance().fromJson(citiesJsonData, ProvinceDataItem.class);
    }

    public List<ProvinceDataItem.ProvinceData> getProvinceData(){
        return readCityDataFromFile().data;
    }
    public Map<String, List<ProvinceDataItem.ProvinceData.CityData>> getProvinceCityMap(){
        Map<String, List<ProvinceDataItem.ProvinceData.CityData>> provinceCityMap = new HashMap<>();
        for (ProvinceDataItem.ProvinceData data : readCityDataFromFile().data) {
            provinceCityMap.put(data.area_name, data.sub_area);
        }
        return provinceCityMap;
    }
    public Map<String, List<AreaItem>> getCityDistrictMap(){
        Map<String, List<AreaItem>> cityDistrictMap = new HashMap<>();
        for (ProvinceDataItem.ProvinceData data : readCityDataFromFile().data) {
            for (ProvinceDataItem.ProvinceData.CityData cityData : data.sub_area) {
                cityDistrictMap.put(cityData.area_name, cityData.sub_area);
            }
        }
        return cityDistrictMap;
    }

    String getLogTag() {
        return getClass().getSimpleName();
    }
}