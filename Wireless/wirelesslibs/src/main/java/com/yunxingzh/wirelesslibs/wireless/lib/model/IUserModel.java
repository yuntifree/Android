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

    interface onLoginListener{
        void onLoginSuccess(UserInfoVo userVo);
        void onLoginFailed(int error);
        void onLoginFailed(String errorMsg);
    }

    /****
     * 获取短信验证码
     * @param yzm
     * @param truetime
     * @param phone
     * @param listener
     */
    void getValidateCode(String yzm, String truetime, String phone, onValidateCodeListener listener);

    /***
     * 登录
     * @param phone
     * @param password
     */
    void login(String phone, String password, onLoginListener listener);
}
