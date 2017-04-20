package com.sulong.elecouple.entity;

import java.util.List;

/**
 * Created by ydh on 2016/6/13.
 */
public class ProvinceDataItem extends SimpleResult1 {

    public List<ProvinceData> data;

    public static class ProvinceData extends AreaItem {

        public List<CityData> sub_area;

        public static class CityData extends AreaItem {

            public List<AreaItem> sub_area;

        }

    }
}
