package com.yunxingzh.wirelesslibs.wireless.lib.api;

public class Api {
    public static final String WEB_SCHEME = "http://";
    // 正式环境地址
   // public static final String WEB_HOST = WEB_SCHEME + "";
    // 测试地址
    public static final String WEB_HOST = WEB_SCHEME + "120.25.133.234/";

    public static final String GET_PHONE_CODE = WEB_HOST + "get_phone_code";//获取短信验证码
    public static final String REGISTER = WEB_HOST + "register";//注册
    public static final String NEWS_LIST = WEB_HOST + "hot";//获取新闻 -视频 -应用 -游戏
    public static final String SERVICE = WEB_HOST + "services";//服务
    public static final String CLICK_COUNT = WEB_HOST + "report_click";//记录点击次数
    public static final String GET_WIFI_LIST = WEB_HOST + "fetch_wifi";//获取wifi列表
    public static final String GET_WEATHER_NEWS = WEB_HOST + "get_weather_news";//首页天气及新闻
    public static final String GET_FONT_INFO = WEB_HOST + "get_front_info";//首页底部内容
    public static final String GET_WIFI_MAP = WEB_HOST + "get_nearby_aps";//获取地图周围热点

    public static final String CHECK_NET = WEB_HOST + "checkEnv";//连接东莞wifi之前先检查网络状况
    public static final String USER_VALIDATE = WEB_HOST + "wifiLogon";//连接东莞wifi用户认证

}
