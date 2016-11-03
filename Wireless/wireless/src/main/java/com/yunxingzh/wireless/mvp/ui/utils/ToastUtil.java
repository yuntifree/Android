package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.activity.LoginActivity;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpCallback;

public class ToastUtil {

    private static Toast mToast = null;

    public static void showMiddle(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
       // mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
    }

    public static void showMiddle(Context context, int resId) {
        context = context.getApplicationContext();
        showMiddle(context, context.getString(resId));
    }

    public static void showErrorMsg(Context context, String msg) {
        ToastUtil.showMiddle(context, msg);

    }

    public static void showError(Context context, int error) {
        if (error == 101) {
            showMiddle(context, "登陆信息失效，请重新登陆");
            MyApplication.sApplication.setToken("");
            MyApplication.sApplication.setUser(null);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            return;
        } else if(error == HttpCode.E_MISS_PARAM){
            showMiddle(context, "缺少参数");
            return;
        } else if(error == HttpCode.E_INVAL_PARAM){
            showMiddle(context, "参数异常");
            return;
        } else if(error == HttpCode.E_DATABASE){
            showMiddle(context, "数据库错误");
            return;
        } else if(error == HttpCode.E_INNER){
            showMiddle(context, "服务器内部错误");
            return;
        } else if(error == HttpCode.E_CODE){
            showMiddle(context, "短信验证码错误");
            return;
        } else if(error == HttpCode.E_GET_CODE){
            showMiddle(context, "获取短信验证码失败");
            return;
        } else if(error == HttpCode.E_USED_PHONE){
            showMiddle(context, "手机号重复注册");
            return;
        } else if(error < 100){
            showMiddle(context, "服务器错误");
            return;
        }
        switch (error) {
            case OkHttpCallback.RESPONSE_ERROR_NET:
                showMiddle(context, R.string.net_error_net);
                break;
            case OkHttpCallback.RESPONSE_ERROR_SERVER:
                showMiddle(context, R.string.net_error_server);
                break;
            case OkHttpCallback.RESPONSE_ERROR_TIMEOUT:
                showMiddle(context, R.string.net_error_timeout);
                break;
            case HttpCode.HTTP_OK:
                showMiddle(context, "成功");
                break;
        }
//            case HttpCode.HTTP_ERROR:
//                showMiddle(context, "代码错误");
//                break;
//            case -1000:
//                showMiddle(context, "手机号码格式错误");
//                break;
//            case -1001:
//                showMiddle(context, "获取验证码出错，请重新获取验证码");
//                break;
//            case -1002:
//                showMiddle(context, "验证码错误");
//                break;
//            case -1003:
//                showMiddle(context, "标识为空");
//                break;
//            case -1004:
//                showMiddle(context, "Token为空");
//                break;
//            case -1005:
//                showMiddle(context, "未找到用户");
//                break;
//            case -1006:
//                showMiddle(context, "标识错误");
//                break;
//            case -1007:
//                showMiddle(context, "token错误");
//                break;
//            case -1008:
//                showMiddle(context, "密码格式错误");
//                break;
//            case -1009:
//                showMiddle(context, "用户未审核");
//                break;
//            case -1010:
//                showMiddle(context, "用户已锁定");
//                break;
//            case -1011:
//                showMiddle(context, "用户名或密码错误，请确认用户名或密码是否正确");
//                break;
//            case -1012:
//                showMiddle(context, "操作数据失败");
//                break;
//            case -1013:
//                showMiddle(context, "短信模块初始化失败");
//                break;
//            case -1014:
//                showMiddle(context, "生成验证码失败");
//                break;
//            case -1015:
//                showMiddle(context, "短信接口配置错误");
//                break;
//            case -1016:
//                showMiddle(context, "短信发送失败");
//                break;
//            case -1017:
//                showMiddle(context, "参数未找到");
//                break;
//            case -1018:
//                showMiddle(context, "插入数据失败");
//                break;
//            case -1019:
//                showMiddle(context, "修改数据失败");
//                break;
//            case -1020:
//                showMiddle(context, "删除数据失败");
//                break;
//            case -1021:
//                showMiddle(context, "运行错误");
//                break;
//            case -1022:
//                showMiddle(context, "用户已被注册");
//                break;
//            case -1023:
//                showMiddle(context, "购买的商品为空");
//                break;
//            case -1024:
//                showMiddle(context, "购买商品的数量为空");
//                break;
//            case -1025:
//                showMiddle(context, "购买的商品以及数量错误");
//                break;
//            case -1026:
//                showMiddle(context, "商品错误");
//                break;
//            case -1027:
//                showMiddle(context, "商品库存不足");
//                break;
//            case -1028:
//                showMiddle(context, "创建订单失败");
//                break;
//            case -1029:
//                showMiddle(context, "创建订单详情失败");
//                break;
//            case -1030:
//                showMiddle(context, "地址为空");
//                break;
//            case -1031:
//                showMiddle(context, "订单不存在");
//                break;
//            case -1032:
//                showMiddle(context, "更新订单失败");
//                break;
//            case -1033:
//                showMiddle(context, "添加快递费失败");
//                break;
//            case -1034:
//                showMiddle(context, "根据对应的Mark找不到供应商");
//                break;
//            case -1035:
//                showMiddle(context, "该账号已经存在");
//                break;
//            case -1036:
//                showMiddle(context, "不是加盟商");
//                break;
//            case -1037:
//                showMiddle(context, "不是供应商");
//                break;
//            case -1038:
//                showMiddle(context, "在指定的时间内已发送过短信");
//                break;
//            case -1043:
//                showMiddle(context, "该订单已经申请过退款");
//                break;
//            case -1046:
//                showMiddle(context, "退款金额不得大于商品金额");
//                break;
//            case -1054:
//                showMiddle(context, "定订单已经逾期，不能申请售后");
//                break;
//
//                /**
//                 * ******************************************** 1100
//                 * ******************************************
//                 */
//
//            case -1100:
//                showMiddle(context, "短信模块初始化失败");
//                break;
//            case -1101:
//                showMiddle(context, "生成验证码失败");
//                break;
//            case -1102:
//                showMiddle(context, "短信接口配置错误");
//                break;
//            case -1103:
//                showMiddle(context, "发送失败");
//                break;
//
//            /**
//             * ******************************************** 1200
//             * ******************************************
//             */
//
//            case -1200:
//                showMiddle(context, "该手机号码已存在");
//                break;
//            case -1201:
//                showMiddle(context, "创建用户失败");
//                break;
//            case -1202:
//                showMiddle(context, "更新密码失败");
//                break;
//            case -1203:
//                showMiddle(context, "更新token失败");
//                break;
//
//            /**
//             * ******************************************** 1300
//             * ******************************************
//             */
//
//            case -1300:
//                showMiddle(context, "被叫手机号码错误");
//                break;
//            case -1301:
//                showMiddle(context, "回拨失败");
//                break;
//            case -1302:
//                showMiddle(context, "余额不足");
//                break;
//        }

    }
}
