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
    public static final String VIDEO_COUNT = WEB_HOST + "report_play";//记录视频播放次数
}
