package com.sulong.elecouple.utils;

import com.google.gson.JsonSyntaxException;
import com.sulong.elecouple.entity.SimpleResult1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * Created by ydh on 7/29/15.
 */
public class JsonParser {
    public static JsonParser jsonParser;

    public static JsonParser getInstance() {
        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }

        return jsonParser;
    }

    public <T extends SimpleResult1> T fromJson(byte[] response, Class<T> tClass) {
        return fromJson(Utility.getStrFromByte(response), tClass);
    }

    public <T extends SimpleResult1> T fromJson(String response, Class<T> tClass) {
        String jsonStr = response.replace("\"data\":[]", "\"data\":null")
                .replace("\"address_info\":[]", "\"address_info\":null");

        T obj = null;
        try {
            obj = Utility.getGson().fromJson(jsonStr, tClass);
        } catch (NumberFormatException e) {
            Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
        } catch (JsonSyntaxException e) {
            Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
        } catch (IllegalStateException e) {
            Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
        }

        // try to parse as SimpleResult
        if (obj == null) {

            SimpleResult1 result1 = null;
            try {
                result1 = Utility.getGson().fromJson(jsonStr, SimpleResult1.class);
            } catch (NumberFormatException e) {
                Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
            } catch (JsonSyntaxException e) {
                Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
            } catch (IllegalStateException e) {
                Debug.fi("JsonParser", e.toString() + " json = " + jsonStr);
            }
            Debug.i("simpleresult = " + result1);
            if (result1 == null) {
                return null;
            }

            try {
                Constructor<T> constructor = tClass.getConstructor(new Class[0]);
                obj = constructor.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                obj.copy(result1);
            }
        }

        return obj;
    }
}
