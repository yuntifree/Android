package com.yunxingzh.wirelesslibs.wireless.lib.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 生成json的工具类
 */

public class JsonUtils {

    private JsonUtils() {

    }

    public static String jsonStirngForUser(int term, double version, long ts,int nettype, int type,
                                           String phone,String username,String password,
                                           int code,String model,String channel,String udid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("term", term);
            jsonObject.put("version", version);
            jsonObject.put("ts", ts);
            jsonObject.put("nettype", nettype);
            JSONObject data = new JSONObject();
            data.put("type", type);
            data.put("phone", phone);
            data.put("username", username);
            data.put("password", password);
            data.put("code", code);
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
                                          int nettype,int type,int seq) {
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
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }
}
