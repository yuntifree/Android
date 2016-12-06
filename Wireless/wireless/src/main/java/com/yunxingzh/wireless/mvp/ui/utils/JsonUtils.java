package com.yunxingzh.wireless.mvp.ui.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 生成json的工具类
 */

public class JsonUtils {

    private JsonUtils() {

    }

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
//        MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
//                0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
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
                array.put(ssids[i]);
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
}
