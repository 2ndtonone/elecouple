package com.sulong.elecouple.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static int compareDate(String time_one, String time_two) {
        int result = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //得到指定模范的时间
            Date d1 = sdf.parse(time_one);
            Date d2 = sdf.parse(time_two);
            //比较
            result = (int) Math.abs(((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000))) + 1;

        } catch (Exception e) {
            //handle exception
            return result;
        }

        return result;
    }

    //获取当前时间，精确到分，用于显示
    public static String getNowDateMinForView() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm a").format(new Date());
    }

    //获取当前时间，精确到分，用于显示和截取特定字符
    public static String getNowDateMinForUse() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    //获取当前时间，精确到秒，用于显示和截取特定字符
    public static String getNowDateMilString() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    //获取当前时间，精确到秒，用于显示和截取特定字符
    public static String getNowDateString() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    //将date转换成指定的格式
    public static String getDateFormatByDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    //将date转换成指定的格式
    public static String getDateFromString(String time) {
        String date = null;
        long timelong = 0;
        try {
            timelong = Long.valueOf(time);
            date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(timelong * 1000));
            return date;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    //将date转换成指定的格式
    public static String getDateFromString(String time, String format) {
        String date = null;
        long timelong = 0;
        try {
            timelong = Long.valueOf(time);
            date = new SimpleDateFormat(format).format(new Date(timelong * 1000));
            return date;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDataeFromExrtactForm(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(date);
        return s;
    }

    public static String getDateSlashFormat() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }


    public static int[] getDHMS(long mss) {
        try {
            int day = (int) (mss / (1000 * 60 * 60 * 24));
            int hour = (int) ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minute = (int) ((mss % (1000 * 60 * 60)) / (1000 * 60));
            int second = (int) ((mss % (1000 * 60)) / 1000);
            return new int[]{day, hour, minute, second};
        } catch (Exception e) {
            Debug.ERROR("日期计算异常");
        }
        return new int[]{0, 0, 0, 0};
    }
}
