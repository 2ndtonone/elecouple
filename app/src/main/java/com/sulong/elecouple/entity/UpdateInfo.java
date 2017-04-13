package com.sulong.elecouple.entity;

/**
 * Created by Administrator on 2015/8/25.
 */
public class UpdateInfo extends SimpleResult1 {

    public DataEntity data;

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "data=" + data +
                '}';
    }

    public static class DataEntity {
        public String android_update_info;
        public String mobile_apk;
        public String mobile_ios;
        public int mobile_apk_force_version;
        public int mobile_apk_version;
        public String service_telephone;
        public String area_version;
        public String share_default_pic;

        @Override
        public String toString() {
            return "DataEntity{" +
                    "android_update_info='" + android_update_info + '\'' +
                    "mobile_apk='" + mobile_apk + '\'' +
                    ", mobile_apk_force_version='" + mobile_apk_force_version + '\'' +
                    ", mobile_apk_version='" + mobile_apk_version + '\'' +
                    ", mobile_ios='" + mobile_ios + '\'' +
                    '}';
        }
    }
}
