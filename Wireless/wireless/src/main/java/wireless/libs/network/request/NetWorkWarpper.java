package wireless.libs.network.request;

import com.alibaba.fastjson.JSONArray;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.User;
import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.ServiceList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.resp.WifiList;
import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.HttpParams;
import wireless.libs.network.HttpUtils;

/**
 * 网络请求统一这里来管理  在方法的末尾一定要指定HttpHandler默认的泛型类型 HttpHandler<T> T缺省 默认为object  也适用于不需要data返回值的接口
 *
 * @author jerry
 * @date 2016/03/21
 * <p>
 * 修订: 2016/06/30 carlos
 * 1. 添加search()方法的注释
 */
public class NetWorkWarpper {

    /***
     * 获取验证码
     */
    public static void validateCode(int type, String phone, HttpHandler<Object> httpHandler) {
        String path = "get_phone_code";
        HttpParams httpParams = new HttpParams();
        httpParams.add("type", type);
        httpParams.add("phone", phone);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /**
     * 注册接口
     * @param code        md5 加密
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
     * 获取首页新闻天气
     * @param handler
     */
    public static void weatherNews(HttpHandler<WeatherNewsList> handler) {
        String path = "get_weather_news";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * 获取头条信息
     * @param httpHandler
     */
    public static void hot(int type, int seq, HttpHandler<HotInfoList> httpHandler) {
        String path = "hot";
        HttpParams httpParams = new HttpParams();
        httpParams.add("type", type);
        httpParams.add("seq", seq);
        HttpUtils.post(path, httpParams, httpHandler);
    }

    /***
     * 拉首页下半页的信息
     */
    public static void getFontInfo(HttpHandler<FontInfoList> handler) {
        String path = "get_front_info";
        HttpParams httpParams = new HttpParams();
        HttpUtils.post(path, httpParams, handler);
    }

    /***
     * 记录点击次数
     */
    public static void clickCount(int id, int type, HttpHandler<Object> handler) {
        String path = "report_click";
        HttpParams httpParams = new HttpParams();
        httpParams.add("id", id);
        httpParams.add("type", type);
        HttpUtils.post(path, httpParams, handler);
    }

    /***
     * 获取wifi列表
     */
    public static void getWifi(double longitude, double latitude, String[] ssids, HttpHandler<WifiList> handler) {
        String path = "get_wifi_pass";
        HttpParams httpParams = new HttpParams();
        httpParams.add("longitude", longitude);
        httpParams.add("latitude", latitude);

        JSONArray array = new JSONArray();
        for (int i = 0; i < ssids.length; i++) {
            array.add(ssids[i]);
        }
        httpParams.add("ssids", array);
        HttpUtils.post(path, httpParams, handler);
    }

    /***
     * 根据经纬度获取地图附近热点列表
     * @param
     */
    public static void getWifiMap(double longitude, double latitude, HttpHandler<WifiMapList> handler) {
        String path = "get_nearby_aps";
        HttpParams httpParams = new HttpParams();
        httpParams.add("longitude", longitude);
        httpParams.add("latitude", latitude);
        HttpUtils.post(path, httpParams, handler);
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

    /**
     * 上报连接东莞wifi次数
     * @param handler
     */
    public static void connectDGCount(String apmac,HttpHandler<Object> handler) {
        String path = "report_apmac";
        HttpParams httpParams = new HttpParams();
        httpParams.add("apmac",apmac);
        HttpUtils.post(path, httpParams, handler);
    }

    /**
     * wifi公益
     * @param handler
     */
    public static void wifiSpirited(String ssid, String password, double longitude, double latitude, HttpHandler<Object> handler) {
        String path = "report_wifi";
        HttpParams httpParams = new HttpParams();
        httpParams.add("ssid",ssid);
        httpParams.add("password",password);
        httpParams.add("longitude",longitude);
        httpParams.add("latitude",latitude);
        HttpUtils.post(path, httpParams, handler);
    }


}