package com.sulong.elecouple.login;

import com.sulong.elecouple.entity.AggUser;
import com.sulong.elecouple.entity.AggVendor;
import com.sulong.elecouple.entity.SimpleResult1;

/**
 *
 * Created by ydh on 7/25/15.
 */
public class LoginItem extends SimpleResult1 {

    public Data data;

    @Override
    public String toString() {
        return "LoginItem{" +
                "data=" + data +
                '}';
    }

    public static class Data extends AggUser {

        /** 商户信息 */
        public AggVendor seller_arr;

        @Override
        public String toString() {
            return "Data{" +
                    "service_telephone='" + service_telephone + '\'' +
                    ", invitation='" + invitation + '\'' +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", token='" + token + '\'' +
                    ", member_avatar_url='" + member_avatar_url + '\'' +
                    ", member_token='" + member_token + '\'' +
                    ", member_mobile='" + member_mobile + '\'' +
                    ", user_id=" + user_id +
                    ", isStrongPassword=" + isStrongPassword +
                    ", free_password=" + free_password +
                    ", is_seller=" + is_seller +
                    ", postal=" + postal +
                    ", seller_arr=" + seller_arr +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Data) {
                Data another = (Data) o;
                return user_id == another.user_id
                        && username.equals(another.username)
                        && invitation.equals(another.invitation)
                        && nickname.equals(another.nickname)
                        && token.equals(another.token)
                        && member_avatar_url.equals(another.member_avatar_url)
                        && member_token.equals(another.member_token)
                        ;
            }
            return super.equals(o);
        }
    }
}
