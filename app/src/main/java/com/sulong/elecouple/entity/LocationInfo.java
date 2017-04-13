package com.sulong.elecouple.entity;

/**
 * 定位信息封装类
 * Created by ydh on 2016/6/7.
 */
public class LocationInfo {
    /** 定位到的纬度 */
    public String latitude = "";
    /** 定位到的经度 */
    public String longitude = "";
    /** 定位到的省份名称 */
    public String province = "";
    /** 定位到的详细地址 */
    public String address = "";
    /** 定位到的区 */
    public String gpsDistrict = "";
    /** 定位到的城市名 */
    public String gpsCityName = "";
    /** 手动切换的城市名 */
    public String selectedCityName = "";
    /** 手动切换的区 */
    public String selectedDistrict = "";
    /** 定位精度 */
    public String radius = "";

    @Override
    public String toString() {
        return "LocationInfo{" + "\n" +
                "\t latitude='" + latitude + '\'' + "\n" +
                "\t longitude='" + longitude + '\'' + "\n" +
                "\t province='" + province + '\'' + "\n" +
                "\t address='" + address + '\'' + "\n" +
                "\t gpsDistrict='" + gpsDistrict + '\'' + "\n" +
                "\t gpsCityName='" + gpsCityName + '\'' + "\n" +
                "\t selectedCityName='" + selectedCityName + '\'' + "\n" +
                "\t selectedDistrict='" + selectedDistrict + '\'' + "\n" +
                "\t radius='" + radius + '\'' + "\n" +
                '}' + "\n";
    }
}