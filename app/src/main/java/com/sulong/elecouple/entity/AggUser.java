package com.sulong.elecouple.entity;

import com.sulong.elecouple.utils.ConstantUtils;

import java.io.Serializable;

/**
 * Created by ydh on 2017/2/10.
 */

public class AggUser implements Serializable {

    public String member_avatar;
    public String is_distribution;
    public String sex;

    /** 客服电话 */
    public String service_telephone = "";
    /** 邀请码 */
    public String invitation = "";
    /** 登陆用户名 */
    public String username = "";
    /** 昵称 */
    public String nickname = "";
    /** 登陆的令牌 */
    public String token = "";
    /** 头像 */
    public String member_avatar_url = "";
    /** 服务端加密后的密码，也是环信的登陆密码 */
    public String member_token = "";
    /** 绑定的手机号，无为空字符串 */
    public String member_mobile = "";
    /** 用户id */
    public int user_id = ConstantUtils.INVALID_ID;
    /** 是否强密码 1为强密码 */
    public int isStrongPassword = 1;
    /** 免输支付密码额度，0表示未设置 */
    public int free_password = 0;
    /** 非0为是商户 */
    public int is_seller = 0;
    /** 1为是邮储用户 */
    public String postal = "";
}
