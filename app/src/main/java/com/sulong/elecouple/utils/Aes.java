package com.sulong.elecouple.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Aes {

    public static final String KEY = "hktx2015wearewin";

    /**
     * 加密
     *
     * @param input
     * @param key
     * @return
     */
    public static String encrypt(String input, String key) {

        byte[] crypted = null;

        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, skey);

            crypted = cipher.doFinal(input.getBytes("utf-8"));

        } catch (Exception e) {

            System.out.println(e.toString());

        }

        return crypted == null ? "" : new String(Base64Util.encode(crypted));

    }


    /**
     * 解密
     *
     * @param input
     * @param key
     * @return String
     */
    public static String decrypt(String input, String key) {

        byte[] output = null;

        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, skey);

            output = cipher.doFinal(Base64Util.decode(input));

        } catch (Exception e) {

            System.out.println(e.toString());

        }

        return output == null ? "" : new String(output);

    }

}
