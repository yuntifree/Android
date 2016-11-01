package com.yunxingzh.wirelesslibs.wireless.lib.api;

public class Api {
    public static final String WEB_SCHEME = "http://";
    // 正式环境地址
    public static final String WEB_HOST = WEB_SCHEME + "aotuyuan.qinto.com/wl/Api.php/Api/";
    // 测试地址
   // public static final String WEB_HOST = WEB_SCHEME + "aotuyuan.qinto.com/wl2/Api.php/Api/";


    //版本信息接口
//    public static final String LED_UPDATE = WEB_HOST + "/ledVersion2/Get.html"; //更新版本
//    public static final String LED_DOWNLOAD_APK = "http://static.xcpnet.com/xcpam.apk";//apk下载地址

    //首页信息
    public static final String MAIN_INFO = WEB_HOST + "indexInformation";//获取首页信息
    public static final String GET_VALIDATE_CODE = WEB_HOST + "cocsms";//获取短信验证码
    public static final String REGISTER = WEB_HOST + "register";//注册
    public static final String LOGIN = WEB_HOST + "login"; // 用户登陆
    public static final String FORGETPWD = WEB_HOST + "passwordForget"; // 忘记密码(重置密码)
    public static final String UPLOAD_IMG = WEB_HOST + "upload_android"; // 上传图片
    public static final String UPDATE_USERINFO = WEB_HOST + "userSave"; // 修改用户信息
    public static final String UPDATE_PWD = WEB_HOST + "passwordSave"; // 修改密码
    public static final String FEED_BACK = WEB_HOST + "feedback"; // 意见反馈
    public static final String GET_NEWS = WEB_HOST + "information"; // 获取资讯列表
    public static final String GET_SEA_OUT_SELECT = WEB_HOST + "exportShippingService"; // 获取海运出口服务列表
    public static final String GET_ADDRESS = WEB_HOST + "address"; // 获取地址
    public static final String ADD_ADDRESS = WEB_HOST + "memberAdd"; // 新增地址
    public static final String GET_USER_ADDRESS = WEB_HOST + "memberAddList"; // 获取用户地址
    public static final String UPDATE_USER_ADDRESS = WEB_HOST + "memberAddSave"; // 修改用户地址
    public static final String PHONE_ORDER = WEB_HOST + "phoneOrder"; // 电话下单（出口海运散货、中港出口散货、空运出口）
    public static final String GET_SHIPPING_LINE = WEB_HOST + "shippingLine"; // 获取海运出口-航运线路
    public static final String GET_LAND_LINE = WEB_HOST + "landLine"; // 获取海运出口-货运线路
    public static final String ORDER_SEA_OUT = WEB_HOST + "exportShippingOrder"; // 海运出口下单
    public static final String GET_BOX_ADDRESS = WEB_HOST + "extractAddress"; // 海运进口-获取提柜地址
    public static final String GET_BOX_SIZE = WEB_HOST + "cabinetType"; // 海运进口-获取柜型
    public static final String GET_MY_ORDER = WEB_HOST + "orderList"; // 获取我的订单列表
    public static final String GET_MY_ORDER_INFO = WEB_HOST + "orderDetails"; // 获取我的订单列表详情
    public static final String ORDER_SEA_IN = WEB_HOST + "importShippingOrder"; // 海运进口-下单
    public static final String GET_CAR_BOX_ADDRESS = WEB_HOST + "hongKong"; // 中港进口-获取提柜地址
    public static final String ORDER_CAR_IN = WEB_HOST + "importTransportationOrder"; // 中港进口-下单
    public static final String ORDER_CAR_OUT = WEB_HOST + "exportTransportationOrder"; // 中港出口-下单
    public static final String ORDER_AIR_IN = WEB_HOST + "importAirOrder"; // 空运进口-下单
    public static final String GET_AIR_LIST = WEB_HOST + "airList"; // 查空运-机场列表
    public static final String GET_AIR_LINE_LIST = WEB_HOST + "airLine"; // 查空运-机场航线列表
    public static final String GET_AIR_LINE_INFO = WEB_HOST + "airLineDetails"; // 查空运-机场航线详情
    public static final String GET_SEA_LIST = WEB_HOST + "portList"; // 查海运-港口列表
    public static final String GET_SEA_LINE_INFO = WEB_HOST + "shippingDetails"; // 查海运-港口列表详情
    public static final String GET_LOGISTICAL_LIST = WEB_HOST + "orderTracking"; // 获取物流状态列表
    public static final String GET_LOCATION = WEB_HOST + "vehiclePositioning"; // 获取位置信息









    //支付接口
    public static final String LED_PAYMENT_WECHAT = "http://passport.xcpnet.com/buy/makeqr/wxpay.html";
    public static final String LED_PAYMENT_ALIPAY = WEB_HOST + "/ledbuy2/topaybyorder/%s.html";
    public static final String LED_PAYMENT_ALIPAY_DISTRIBUTOR = WEB_HOST + "/leddistributorbuy2/topaybyorder/%s.html";
    public static final String LED_PAYMENT_BALANCE = WEB_HOST + "/ledbuy2/submit/balance.html";

    //用户相关接口
    public static final String LED_MACHINE_INFO = WEB_HOST + "/ledpassport2/getmachineinfo.html";// 获取购物机信息


    public static final String LED_USER_INFO = WEB_HOST + "/ledpassport2/getuserinfo.html"; // 获取用户详细信息
    public static final String USER_REGISTER = WEB_HOST + "/ledpassport2/register.html";//用户注册
    public static final String SEND_MSG = WEB_HOST + "/ledpassport2/sendsms/yuntongxun.html";//发送短信
    public static final String LED_REST_PASSWORD = WEB_HOST + "/ledpassport2/findpassword.html"; //找回密码
    public static final String UPDATE_USER_INFO = WEB_HOST + "/leddistributor2/setdistributor.html";    //修改基本信息
    public static final String UPDATE_PAY_PWD = WEB_HOST + "/ledpassport2/setpaypassword.html"; //找回（修改）支付密码
    public static final String BIND_PHONE = WEB_HOST + "/ledpassport2/modifymobile.html";//绑定手机

    public static final String VALIDATE_MOBILE = WEB_HOST + "/ledpassport2/checkregistermobile.html"; //验证手机号码
    public static final String UPDATE_PASSWORD = WEB_HOST + "/ledpassport2/modifypassword.html"; // 修改用户密码(帐户安全)

    //城品惠地址相关接口

}
