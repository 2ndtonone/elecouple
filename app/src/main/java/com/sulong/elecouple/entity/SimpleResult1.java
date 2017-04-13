package com.sulong.elecouple.entity;

import com.sulong.elecouple.utils.ConstantUtils;

import java.io.Serializable;

/**
 *
 * Created by ydh on 6/17/15.
 * {"code":200,"message":"确认成功"}
 */
public class SimpleResult1 implements Serializable {
    public static final int STATUS_AUTH_FAIL = ConstantUtils.STATUS_AUTH_FAIL;
    private static final int STATUS_OK = 200;
    public int code;
    public String message;

    public SimpleResult1() {
    }

    public boolean isOk() {
        return code == STATUS_OK;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SimpleResult1{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public void copy(SimpleResult1 result1) {
        if (result1 == null) {
            return;
        }

        code = result1.code;
        message = result1.message;
    }
}
