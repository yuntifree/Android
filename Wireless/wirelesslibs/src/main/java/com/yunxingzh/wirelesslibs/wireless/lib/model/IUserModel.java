package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;

/**
 * Created by Stephen on 2016/9/9.
 */
public interface IUserModel {

    interface onValidateCodeListener{
        void onValidateCodeSuccess();
        void onValidateCodeFailed(int error);
        void onValidateCodeFailed(String errorMsg);
    }

    interface onRegisterListener{
        void onRegisterSuccess(UserInfoVo userVo);
        void onRegisterFailed(int error);
        void onRegisterFailed(String errorMsg);
    }

    /****
     * 获取短信验证码
     * @param phone
     * @param listener
     */
    void getValidateCode(int term,double version,long ts,int nettype,int type, String phone, onValidateCodeListener listener);

    /***
     * 登录
     * @param
     * @param
     */
    void register(int term,double version,long ts,int nettype,String username,String password,
               int code,String model,String channel,String udid,onRegisterListener listener);
}
