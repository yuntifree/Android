package com.yunxingzh.wireless.utils;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 生成json的工具类
 */

public class JsonUtils {

    private static final String TAG = "JsonError";

    public static String toJSONString(Object object) {
        try {
            String json = JSON.toJSONString(object);
            LogUtils.d(TAG, "json = "+ json);
            return json;
        }
        catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Bean-Json错误. object = " + object);
        }
        return null;
    }

    /**********************************************************/

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        try {
            return JSON.parseArray(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static List<Object> parseArray(String text, Type[] types) {
        try {
            return JSON.parseArray(text, types);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            T result = JSON.parseObject(text, clazz);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static <T> T parseObject(String text, Type clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr){
       JSONObject jsonObject ;
        try {
            jsonStr = jsonStr.replace("\\", "");//有反斜杠\,需要替换掉
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

}
