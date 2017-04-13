package com.sulong.elecouple.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author yarine.gao
 * @version 1.0.0
 * @date 2011-8-31
 */
public class StringUtils {

    public static List<Map<String, Object>> getList(JSONArray jsonArray) {
        List<Map<String, Object>> list = null;

        try {
            JSONObject jsonObject;
            list = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();

            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }

            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(cs.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }


    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    public static boolean isNumeric(CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static final String replace(String line, String oldString,
                                       String newString) {
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            buf.append(line2, i, line2.length - i);
            return buf.toString();
        }
        return line;
    }

    public static int toInt(String str, int def) {

        if (str == null || isBlank(str) || !isNumeric(str)) {
            return def;
        }

        return Integer.valueOf(str);
    }

    public static byte[] getBytesUtf8(String string) {
        if (string == null) {
            return null;
        }

        try {
            return string.getBytes("UTF-8");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String getSysTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String jsonExist(JSONObject obj, String key) {
        if (obj.has(key))
            try {
                return obj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        return "";
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 只允许字母和数字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {

        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 验证身份证格式
     *
     * @param text
     * @return
     */
    public static boolean isPersonIdValidation(String text) {
        String regx = "[0-9]{17}[Xx]";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 只显示银行卡的后n位，其余的用*代替
     */
    public static String replaceSubString(String str, int n) {
        if (str.length() <= n) {
            return str;
        }
        String sub = "";
        try {
            sub = str.substring(str.length() - n, str.length());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length() - n; i++) {
                sb = sb.append("*");
            }
            sub = sb.toString() + sub;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub;
    }

    public static String format2Float(float num) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(num);
    }
}




