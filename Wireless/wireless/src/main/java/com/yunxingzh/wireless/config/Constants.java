package com.yunxingzh.wireless.config;

public class Constants {
    public static final String SP_KEY_MARK = "UUID";
    public static final String SP_KEY_TOKEN = "Token";
    public static final String SP_WIFI_PWD = "wifiPwd";
    public static final String SP_TIME_OUT = "timeout";
    public static final String SP_USER_NAME = "userName";
    public static final String SP_KEY_MACHINE = "MachineId";
    public static final String SP_KEY_USER = "sp_key_user";
    public static final String SP_KEY_AREA = "sp_key_area";

    //webview页面flag
    public static final String FLAG_WEBVIEW = "iswebviwefrom";
    public static final int FLAG_HOME_CONTACT_US = 0;//联系我们
    public static final int FLAG_ABOUT_US = 1;//关于我们

    public static final int ITEM_HEIGHT = 10;//recylerview的item间隔
    public static final int PAGE_SIZE = 5;
    public static final int LISTVIEW_ITEM_HEIGHT = 60;
    public static final String VIDEO_URL = "video_url";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String FROM_NEWS = "fromNews";

    //eventBus flag
    public static final int HEAD_LINE_NEWS_FLAG = 100;//头条新闻点击上报flag
    public static final int MAIN_NEWS_FLAG = 101;//首页新闻点击上报flag
    public static final int WIRELESS = 1;//无线
    public static final int HEAD_LINE = 2;//头条
    public static final int SERVICE = 3;//服务
    public static final int VIDEO = 4;//视频
    public static final int SPEED_TEST = 5;//wifi测速
    public static final int SPEED_FLAG = 6;//wifi测速flag

    //0、网络正常，可以发起调用认证、下线等接口。1、已经认证成功。
    public static final int NET_OK = 0;
    public static final int VALIDATE_SUCCESS = 1;

    //类型 0-晴 1-阴 2-雨 3-雪
    public static final int SUNNY = 0;
    public static final int CLOUDY = 1;
    public static final int RAIN = 2;
    public static final int SNOW = 3;

    public static final int TIME_OUT = 15*1000;
    public static final String SSID = "无线东莞DG-FREE";

    // url
    public static final String URL_AGREEMENT = "http://www.yunxingzh.com/app/agreement.html";
    public static final String URL_WEATHER = "http://shenbao.dg.gov.cn/dgcsfw_zfb/csfw/dg_qxj/weixinportal.jsp";

}
