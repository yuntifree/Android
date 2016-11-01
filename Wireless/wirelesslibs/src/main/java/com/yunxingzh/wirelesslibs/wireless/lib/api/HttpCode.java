package com.yunxingzh.wirelesslibs.wireless.lib.api;

/**
 * Created by Carey on 2016/5/27.
 */
public class HttpCode {
    //网络请求状态码
    public static final int HTTP_OK = 200;    //请求成功
    public static final int HTTP_ERROR = -500; //代码错误
    public static final int HTTP_PHONE_NUMBER_FORMAT_ERROR = -1000;    //手机号码格式错误
    public static final int HTTP_GET_CODE_ERROR = -1001;//获取验证码出错，请重新获取验证码
    public static final int HTTP_CODE_ERROR = -1002;//验证码错误
    public static final int HTTP_MARK_EMPTY = -1003;//标志为空
    public static final int HTTP_TOKEN_EMPTY = -1004;//Token为空
    public static final int HTTP_USER_UNFIND = -1005;//未找到用户，请重新登录
    public static final int HTTP_MARK_ERROR = -1006;//标志错误
    public static final int HTTP_TOKEN_ERROR = -1007;//Token错误
    public static final int HTTP_PASSWORD_FORMAT_ERROR = -1008;//密码格式错误
    public static final int HTTP_USER_NOT_CHECK = -1009;//用户未审核
    public static final int HTTP_USER_LOCKING = -1010;//用户已锁定
    public static final int HTTP_PASSWORD_ERROR = -1011;//密码错误
    public static final int HTTP_OPERATING_DATA_FAILARE = -1012;//操作数据失败
    public static final int HTTP_SMS_INIT_FAILARE = -1013;//短信模块初始化失败
    public static final int HTTP_GENERATE_CODE_FAILARE = -1014;//生成验证码失败
    public static final int HTTP_SMS_INTERFACE_CONFIGURATION_ERROR = -1015;//短信接口配置错误
    public static final int HTTP_SMS_SEND_FAILARE = -1016;//短信发送失败
    public static final int HTTP_PARAM_UNFIND = -1017;//参数未找到
    public static final int HTTP_INSERT_DATA_FAILARE = -1018;//插入数据失败
    public static final int HTTP_UPDATE_DATA_FAILARE = -1019;//修改数据失败
    public static final int HTTP_DELETE_DATA_FAILARE = -1020;//删除数据失败
    public static final int HTTP_RUN_ERROR = -1021;//运行错误
    public static final int HTTP_USER_REGISTERED = -1022;//用户已被注册
    public static final int HTTP_GOODS_EMPTY = -1023;//购买的商品为空
    public static final int HTTP_GOODS_COUNT_EMPTY = -1024;//购买商品的数量为空
    public static final int HTTP_GOODS_AND_COUNT_ERROR = -1025;//购买的商品以及数量错误
    public static final int HTTP_GOODS_ERROR = -1026;//商品错误
    public static final int HTTP_GOODS_INVENTORY_SHORTAGE = -1027;//商品库存不足
    public static final int HTTP_CREATE_ORDER_FAILARE = -1028;//创建订单失败
    public static final int HTTP_CREATE_ORDER_INFO_FAILARE = -1029;//创建订单详情失败
    public static final int HTTP_ADDRESS_EMPTY = -1030;//地址为空
    public static final int HTTP_ORDER_NOT_EXIST = -1031;//订单不存在
    public static final int HTTP_UPDATE_ORDER_FAILARE = -1032;//更新订单失败
    public static final int HTTP_ADD_FREIGHT_FAILARE = -1033;//添加快递费失败
    public static final int HTTP_UNFIND_SUPPLIER_BY_MARK = -1034;//根据对应的Mark找不到供应商
    public static final int HTTP_ACCOUNT_EXIST = -1035;//该账号已经存在
    public static final int HTTP_NOT_FRANCHISEE = -1036;//不是加盟商
    public static final int HTTP_SMS_HAS_SENT_LATELY = -1038;//在指定的时间内已发送过短信
    public static final int HTTP_THE_ORDER_HAS_REFUND = -1043;//该订单已经申请过退款
    public static final int HTTP_REFUND_MONEY_EXCESS = -1046;//退款金额不得大于商品金额

    public static final int HTTP_SMS_INIT_FAILARE_NEW = -1100;//短信模块初始化失败
    public static final int HTTP_GENERATE_CODE_FAILARE_NEW = -1101;//生成验证码失败
    public static final int HTTP_SMS_INTERFACE_CONFIGURATION_ERROR_NEW = -1102;//短信接口配置错误

    public static final int HTTP_SEND_FAILARE = -1200;//该手机号码已存在
    public static final int HTTP_CREATE_USER_FAILARE = -1201;//创建用户失败
    public static final int HTTP_UPDATE_PASSWORD_FAILARE = -1202;//更新密码失败
    public static final int HTTP_UPDATE_TOKEN_FAILARE = -1203;//更新token失败

    public static final int HTTP_CALLED_PHONE_NUMBER_ERROR = -1203;//被叫手机号码错误
    public static final int HTTP_CALLBACK_FAILED = -1203;//回拨失败
    public static final int HTTP_BALANCE_SHORTAGE = -1203;//余额不足

}
