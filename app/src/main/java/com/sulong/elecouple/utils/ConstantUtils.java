package com.sulong.elecouple.utils;

import com.sulong.elecouple.BuildConfig;

public class ConstantUtils {

    private static final String SERVER_TYPE_MALLDEV = "malldev";
    private static final String SERVER_TYPE_O2ODEV = "o2odev";
    private static final String SERVER_TYPE_DEVSHOP = "devshop";
    private static final String SERVER_TYPE_TESTSHOP = "testshop";
    private static final String SERVER_TYPE_ONLINE_TEST = "online_test";
    private static final String SERVER_TYPE_RELEASE = "release";
    private static final String SERVER_TYPE_MANJIAN = "manjian";
    private static final String SERVER_TYPE_YOUHUA = "youhua";

    private static String SERVER_URL = "http://shop.aigegou.com/";
    private static String MOBLIE_URL = "mobile/";

    static {
        if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_O2ODEV)) {
            SERVER_URL = "http://o2odev.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_MALLDEV)) {
            SERVER_URL = "http://malldev.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_DEVSHOP)) {
            SERVER_URL = "http://devshop.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_RELEASE)) {
            SERVER_URL = "http://shop.aigegou.com/";
            MOBLIE_URL = "agg/mobile/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_TESTSHOP)) {
            SERVER_URL = "http://testshop.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_ONLINE_TEST)) {
            SERVER_URL = "http://online.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_MANJIAN)) {
            SERVER_URL = "http://manjian.aigegou.com/";
        } else if (BuildConfig.SERVER_TYPE.equals(SERVER_TYPE_YOUHUA)) {
            SERVER_URL = "http://youhua.aigegou.com/";
        } else {
            SERVER_URL = "http://192.168.6.27/";
        }
    }

    private static String INDEX_PHP = MOBLIE_URL + "index.php";

    public static final String DEFAULT_LNG = "120.544392";
    public static final String DEFAULT_LAT = "31.282958";
    public static final String DEFAULT_RADIUS = "0.0";

    public static final String KEY_URL = "url";
    public static final String KEY_ALL_AREA_VERSION = "all_area_version";
    public static final String FILE_ALL_AREA_JSON_NAME = "all_area.json";
    public static final String KEY_AREA_VERSION = "area_version";
    public static final String FILE_AREA_JSON_NAME = "area.json";
    /** 绑定的手机号 */
    public static final String PREF_KEY_BINDING_PHONE = "bindingPhone";

    /**
     * 加密的版本号
     */
    public static final String PREF_ENCRYPT_VERSION = "encrypt_version";
    /**
     * 登陆类型
     */
    public static final String PREF_KEY_LOGIN_TYPE = "loginType";
    /**
     * 邀请码
     */
    public static final String PREF_KEY_INVITATION_CODE = "myCode";
    /**
     * 登录用户名
     */
    public static final String PREF_KEY_REMEMBERED_LOGIN_NAME = "phoneNumber";
    /**
     * 登录密码
     */
    public static final String PREF_KEY_PASSWORD = "humanPassword";
    /**
     * 记住密码
     */
    public static final String PREF_KEY_REMEMBER = "isRemember";
    /**
     * 纬度
     */
    public static final String PREF_KEY_LATITUDE = "latitude";
    /**
     * 经度
     */
    public static final String PREF_KEY_LONGITUDE = "longitude";
    /**
     * 选择的城市名
     */
    public static final String PREF_KEY_CITY_NAME = "cityName";
    /**
     * 定位的省份
     */
    public static final String PREF_KEY_GPS_PROVINCE = "province";
    /**
     * 定位的城市名
     */
    public static final String PREF_KEY_GPS_CITY_NAME = "gpsCityName";
    /**
     * 定位的地址
     */
    public static final String PREF_KEY_ADDRESS = "address";
    /**
     * 地址描述
     */
    public static final String PREF_KEY_LOCATION_DESCRIBE = "locationDescribe";
    /**
     * 定位的地区
     */
    public static final String PREF_KEY_GPS_DISTRICT = "gps_district_name";
    /**
     * 选择的地区
     */
    public static final String PREF_KEY_DISTRICT = "district_name";
    /**
     * 定位的精度范围
     */
    public static final String PREF_KEY_RADIUS = "radius";
    public static final String PREF_KEY_VERSION_CODE = "versionCode";
    public static final String PREF_KEY_MESSAGE_PUSH_TO_VENDOR_COUNT = "PREF_KEY_MESSAGE_PUSH_TO_VENDOR_COUNT";
    public static final String PREF_KEY_LAST_LOCATE_TIME = "PREF_KEY_LAST_LOCATE_TIME";
    public static final String PREF_KEY_DEFAULT_SHARE_IMAGE = "PREF_KEY_DEFAULT_SHARE_IMAGE";
    public static final String KEY_TOKEN_MEMBER_ID = "token_member_id";
    public static final String KEY_USER_ID = "user_id";
    public static final String LOGIN_PREF = "login_info";
    public static final String PREF_NAME_FOREVER_USER_INFO = "forever_user_info";
    public static final String GPS_PREF = "gps_info";
    public static final String KEY_ID = "id";
    public static final String KEY_MEMBER_ID = "member_id";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TITLE_RES_ID = "KEY_TITLE_RES_ID";
    public static final String KEY_CONTENT_RES_ID = "KEY_CONTENT_RES_ID";
    public static final String KEY_CONTENT_HINT_RES_ID = "KEY_CONTENT_HINT_RES_ID";
    public static final String KEY_CONTENT_RES_STR = "KEY_CONTENT_RES_STR";
    public static final String KEY_DLG_LEFT_BTN_RES_ID = "KEY_DLG_LEFT_BTN_RES_ID";
    public static final String KEY_DLG_RIGHT_BTN_RES_ID = "KEY_DLG_RIGHT_BTN_RES_ID";
    public static final String KEY_DLG_LEFT_BTN_TEXT = "KEY_DLG_LEFT_BTN_TEXT";
    public static final String KEY_DLG_RIGHT_BTN_TEXT = "KEY_DLG_RIGHT_BTN_TEXT";
    public static final String KEY_DLG_SHOWTITLE = "showtitle";
    public static final String KEY_DLG_ONE_EDIT_MODLE = "KEY_DLG_ONE_EDIT_MODLE";
    public static final String KEY_DLG_SHOWTWOCONTENT_MODLE = "KEY_DLG_SHOWTWOCONTENT_MODLE";
    public static final String KEY_EDIT_TEXT = "KEY_EDIT_TEXT";
    public static final int INVALID_ID = -1;
    public static final String KEY_TOKEN = "key";
    public static final String KEY_CLIENT_TYPE = "client_type";
    public static final String KEY_VERSION_CODE = "ver_code";
    public static final String KEY_VERSION_NAME = "version_name";
    public static final String KEY_ANDROID_VERSION = "android_version";
    public static final String KEY_MEMBER_AVATAR = "member_avatar";
    public static final String KEY_MEMBER_TOKEN = "member_token";
    public static final String KEY_NICKNAME = "nick_name";
    public static final String KEY_SERVICE_TELEPHONE = "KEY_SERVICE_TELEPHONE";

    /** 支付方式：支付宝 */
    public static final String PAY_TYPE_ALIPAY = "alipay";
    /** 支付方式：微信app内支付 */
    public static final String PAY_TYPE_WECHAT_APP = "wx_app";
    /** 支付方式：招行一卡通 */
    public static final String PAY_TYPE_CMB = "zhaohang";
    /** 接口请求错误类型：网络请求失败 */
    public static final int STATUS_FAILED = -2;
    /** 接口请求错误类型：无法解析出正常的数据 */
    public static final int STATUS_NO_DATA = -1;
    /** 接口请求错误类型：参数错误 */
    public static final int STATUS_WRONG_PARAM = 80000;
    /** 接口请求错误类型：用户授权错误 */
    public static final int STATUS_AUTH_FAIL = 80001;
    /** 接口请求错误类型：操作错误（该有的信息却没有，或者操作失败等） */
    public static final int STATUS_NOT_EXPECT_RESULT = 80002;
    /** 接口请求错误类型：数据库异常错误 */
    public static final int STATUS_DATABASE_ERROR = 80004;
    /** 接口请求错误类型：服务器错误 */
    public static final int STATUS_SERVER_ERROR = 80005;

    /**
     * 本地广播动作：登录冲突
     */
    public static String ACTION_LOGIN_CONFLICT = "ACTION_LOGIN_CONFLICT";
    /**
     * 本地广播动作：关闭所有需要登录才能操作的页面
     */
    public static String ACTION_CLOSE_PAGE_WHICH_NEED_LOGIN = "ACTION_CLOSE_PAGE_WHICH_NEED_LOGIN";
    /**
     * 本地广播动作：关闭所有页面
     */
    public static String ACTION_CLOSE_ALL_PAGE = "ACTION_CLOSE_ALL_PAGE";

    public static StringBuffer getPhpUrl() {
        StringBuffer sb = new StringBuffer(SERVER_URL);
        return sb.append(INDEX_PHP);
    }

    public static String getPhpUrl(String suffix) {
        return getPhpUrl().append("?").append(suffix).toString();
    }
}