package com.sulong.elecouple.entity;

import java.io.Serializable;

/**
 * Created by ydh on 2017/2/10.
 */

public class AggVendor implements Serializable {

    /** 商户登陆token */
    public String key;
    /** 店铺id */
    public int shop_id;
    /** 商户管理员的用户id */
    public int shop_admin_id;
    /** 商户作为用户的用户id */
    public int member_id;
    /** 商户登录名 */
    public String username;
    /** 店铺名称 */
    public String storeName;
    /** 商户类别： 1 本土商户, 2 商城商户 */
    public int tag;
    /** 商户积分 */
    public String points;
    /** 商户绑定的手机号，空字符表示未绑定手机号 */
    public String bindMobile;
    /** 1为同意了商户协议 */
    public int is_agree;
    /** 1为强密码 */
    public int isStrongPassword;
    /** 1为邮储商户 */
    public String postal;
}
