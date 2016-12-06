package com.yunxingzh.wireless.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


/**
 * 生成json的工具类
 */

public class JsonUtils {

    private static final String TAG = "JsonError";

    public static String jsonStirngForUser(int term, double version, long ts, int nettype, int type, String username, String password,
                                           String model, String channel, String udid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("term", term);
            jsonObject.put("version", version);
            jsonObject.put("ts", ts);
            jsonObject.put("nettype", nettype);
            JSONObject data = new JSONObject();
            data.put("type", type);
            data.put("username", username);
            data.put("password", password);
            data.put("model", model);
            data.put("channel", channel);
            data.put("udid", udid);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    public static String jsonStirngForMain(int uid, String token, int term, double version, long ts,
                                           int nettype, int type, int seq, int id, double longitude, double latitude, String ssid, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("token", token);
            jsonObject.put("term", term);
            jsonObject.put("version", version);
            jsonObject.put("ts", ts);
            jsonObject.put("nettype", nettype);
            JSONObject data = new JSONObject();
            data.put("type", type);
            data.put("seq", seq);
            data.put("id", id);
            data.put("longitude", longitude);
            data.put("latitude", latitude);
            data.put("ssid", ssid);
            data.put("password", password);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

   // public static JSONObject parentJsonNode() {
//        MainApplication.sApplication.getUser().getData().getUid(),MainApplication.sApplication.getToken(),
//                0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
//                StringUtils.getCurrentTime(),
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("uid", uid);
//            jsonObject.put("token", token);
//            jsonObject.put("term", term);
//            jsonObject.put("version", version);
//            jsonObject.put("ts", ts);
//            jsonObject.put("nettype", AppUtils.getNetWorkType());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return jsonObject;
   // }

//    public static String childJsonNode(int a,int b){
//        JSONObject jsObj = parentJsonNode(uid,token,term,version,ts,nettype);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("a",a);
//            jsonObject.put("b",b);
//            jsObj.put("data",jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsObj.toString();
//    }

    public static String jsonStirngForWifi(int uid, String token, int term, double version, long ts,
                                           int nettype, double longitude, double latitude, String[] ssids) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("token", token);
            jsonObject.put("term", term);
            jsonObject.put("version", version);
            jsonObject.put("ts", ts);
            jsonObject.put("nettype", nettype);
            JSONObject data = new JSONObject();
            data.put("longitude", longitude);
            data.put("latitude", latitude);

            JSONArray array = new JSONArray();
            for (int i = 0; i < ssids.length; i++) {
                array.add(ssids[i]);
            }
            data.put("ssids", array);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    public static String jsonStirngForMain(int uid, String token, int term, double version, long ts,
                                           int nettype, String apmac) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("token", token);
            jsonObject.put("term", term);
            jsonObject.put("version", version);
            jsonObject.put("ts", ts);
            jsonObject.put("nettype", nettype);
            JSONObject data = new JSONObject();
            data.put("apmac", apmac);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    /*********************************************************/

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

}
