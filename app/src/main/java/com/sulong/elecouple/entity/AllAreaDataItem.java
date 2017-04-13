package com.sulong.elecouple.entity;

import com.google.gson.internal.LinkedHashTreeMap;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class AllAreaDataItem extends SimpleResult1 {
    public Data data;

    public class Data {
        public ArrayList<String> hot_city;
        public LinkedHashTreeMap<String, ArrayList<String>> city_index;
        public ArrayList<CityDistrict> city_district;
    }

    public class CityDistrict {
        public int area_id;
        public String area_name;
        public ArrayList<SubArea> sub_area;
    }

    public class SubArea {
        public int area_id;
        public String area_name;
    }

}
