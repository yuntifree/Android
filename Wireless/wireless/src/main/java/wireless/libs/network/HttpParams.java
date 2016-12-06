package wireless.libs.network;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yunxingzh.wireless.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import wireless.libs.bean.req.BaseRequest;


/**
 * 请求参数的封装
 *
 * @author jayce
 * @date 2015/05/24
 * Add parameter for http request , can only be the kind included in bases[] , or throw exception !
 */
public class HttpParams {

    private BaseRequest mBaseRequest;

    private Map<String, Object> params = new HashMap<>();

    Class bases[] = {Integer.class, Long.class, Short.class, Float.class, Double.class, String.class, JSONArray.class, JSON.class};


    public HttpParams() {
        mBaseRequest = new BaseRequest();
    }

    /**
     * 添加键值对参数 与 setEntity 互斥 data
     *
     * @param key
     * @param param
     */
    public void add(String key, Object param) {
        mBaseRequest.data = params;
        try {
            if (isValidate(param)) {
                params.put(key, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个对象当做参数data 与 add 互斥 data
     *
     * @param o
     */
    public void setEntity(Object o) {
        mBaseRequest.data = o;
    }


    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 请求参数的json
     *
     * @return
     */
    public String toJson() {
        return JsonUtils.toJSONString(mBaseRequest);
    }

    public boolean isValidate(Object param) throws Exception {
        Class cls = param.getClass();
        for (Class c : bases) {
            if (cls == c)
                return true;
        }
        throw new Exception("param " + param + " is not allowed.");
    }
}
