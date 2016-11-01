package com.yunxingzh.wireless.config;

public class Constants {
    public static final String SP_KEY_MARK = "UUID";
    public static final String SP_KEY_TOKEN = "Token";
    public static final String SP_KEY_MACHINE = "MachineId";
    public static final String SP_KEY_USER = "sp_key_user";
    public static final String SP_KEY_AREA = "sp_key_area";

    //webview页面flag
    public static final String FLAG_WEBVIEW = "iswebviwefrom";
    public static final int FLAG_HOME_CONTACT_US = 0;//联系我们
    public static final int FLAG_ABOUT_US = 1;//关于我们
    public static final int FLAG_REGISTER = 2;//注册协议
    public static final int FLAG_ADVERTISES_PAGE = 3;//资讯Item点击

    //进出口页面flag
    public static final String FLAG_IN_OR_OUT = "isInFrom";
    public static final int FLAG_HOME_CAR = 0;//中港进出口
    public static final int FLAG_HOME_AIR = 1;//空运进出口
    public static final int FLAG_SAN_HUO = 2;//海运出口（散货）
    public static final int FLAG_SAN_HUO_CAR = 3;//中港出口（散货）
    public static final int FLAG_SAN_HUO_AIR_OUT = 4;//空运出口（散货）

    //资讯贸易和招商招聘页面flag
    public static final String FLAG_NEWS_FROM = "isNewsFrom";
    public static final int FLAG_NEWS = 0;//资讯贸易
    public static final int FLAG_ADVERTISES = 1;//招商招聘

    //修改地址和添加地址flag
    public static final String FLAG_UPDATE_OR_ADD = "updateOrAdd";
    public static final int FLAG_ADD = 0;//添加地址
    public static final int FLAG_UPDATE = 1;//修改地址

    //选择省市区flag
    public static final String FLAG_SELECT_ADDRESS = "pro_city_coun";
    public static final int FLAG_PROVINCE = 0;//省
    public static final int FLAG_CITY = 1;//市
    public static final int FLAG_COUNTRY = 2;//区

    //下单选择地址flag
    public static final String FLAG_FROM_ORDER = "fromOrder";
    public static final int FLAG_ORDER = 0;//来自订单页面
    public static final int FLAG_PERSONAL = 1;//来自地址管理页面

    //各列表flag
    public static final String FLAG_LIST = "list";
    public static final int FLAG_SHIPPING_PAGE = 0;//来自海运出口-订船-航运线路
    public static final int FLAG_LAND_PAGE = 1;//来自海运出口-订车-货运线路
    public static final int FLAG_MY_ORDER = 2;//来自用户订单列表
    public static final int FLAG_SEARCH_AIR_LIST = 3;//来自查空运机场列表
    public static final int FLAG_SEARCH_SEA_LIST = 4;//来自查海运港口列表
    public static final int FLAG_LOGISTICAL_LIST = 5;//来自物流状态列表

    //下单选择出口服务flag
    public static int FLAG_ORDER_SELECT = 0;//1订船2订车3报关4退税

    //海运进口或中港进口flag
    public static final String FLAG_SEA_OR_CAR = "seaOrCar";
    public static final int FLAG_SEA_IN = 0;//海运进口
    public static final int FLAG_LAND_CAR = 1;//中港进口
    public static final int FLAG_LAND_CAR_OUT = 2;//中港出口
    public static final int FLAG_AIR_IN = 3;//空运进口

    //查海运或查空运flag
    public static final String FLAG_SEARCH_SEA_OR_AIR = "searchSeaOrAir";
    public static final int FLAG_AIR = 0;//查空运
    public static final int FLAG_SEA = 1;//查海运

    //订单状态flag
    public static final String FLAG_ORDER_STATE = "order_state";
    public static final int FLAG_ING = 0;//进行中
    public static final int FLAG_FINISH = 1;//已完成



    public static final int PAGE_SIZE = 5;

}
