package com.yunxingzh.wirelesslibs.wireless.lib.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 生成json的工具类
 */

public class JsonUtils {

    private JsonUtils() {
    }

    ;


/*    {
        "uid":xxxx,   //用户id
            "token":"xxxx",  //用户token
            "term": xxx, //用户终端 0-android 1-ios
            "version":xx, //客户端版本号
            "ts": xxxxx, // 请求时间戳
            "nettype":xxx, //网络类型
            "data":{
        "type":xxx,
                "seq":xxx
    }
    }*/

    public static String createJsonStirng(String uid, int term, int version, long ts, int nettype, int type, int seq) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
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
