package wireless.libs.network.request;

import com.alibaba.fastjson.JSONArray;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.User;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.ServiceList;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.HttpParams;
import wireless.libs.network.HttpUtils;

/**
 * 网络请求统一这里来管理  在方法的末尾一定要指定HttpHandler默认的泛型类型 HttpHandler<T> T缺省 默认为object  也也适用于不需要data返回值的接口
 *
 * @author jerry
 * @date 2016/03/21
 *
 * 修订: 2016/06/30 carlos
 * 1. 添加search()方法的注释
 */
public class NetWorkWarpper {

    /**************************************
     * 登录/注册相关接口
     ************************************/


    /**
     * 注册接口
     *
     * @param phone
     * @param code md5 加密
     * @param httpHandler
     */
    public static void register(String phone, String code, HttpHandler<User> httpHandler) {
        String path = "register";
        HttpParams httpParams = new HttpParams();
        httpParams.add("username", phone);
        httpParams.add("password", StringUtils.getMD5(code));
        httpParams.add("model", AppUtils.getPhoneModel());
        httpParams.add("channel", AppUtils.getChannelName(MainApplication.getInstance()));
        httpParams.add("udid", MainApplication.getInstance().getMark());

        HttpUtils.post(path, httpParams, httpHandler);
    }

    /**
     * 获取头条信息
     * @param type
     * @param seq
     * @param httpHandler
     */
    public static void hot(int type, int seq, HttpHandler<HotInfoList> httpHandler) {
        String path = "hot";
        HttpParams httpParams = new HttpParams();
        httpParams.add("type", type);
        httpParams.add("seq", seq);

        HttpUtils.post(path, httpParams, httpHandler);
    }

    /**
     * 获取服务列表
     * @param handler
     */
    public static void services(HttpHandler<ServiceList> handler) {
        String path = "services";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }
}