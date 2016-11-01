package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.StringDto;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.UserDto;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IUserModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;

import okhttp3.Headers;

/**
 * Created by Stephen on 2016/9/9.
 */
public class UserModelImpl implements IUserModel {

    @Override
    public void getValidateCode(String yzm, String truetime, String phone,final onValidateCodeListener listener) {
        OkRequestParams params = new OkRequestParams();
        params.put("yzm", yzm);
        params.put("truetime", truetime);
        params.put("phone", phone);

        OkHttpUtil.post(Api.GET_VALIDATE_CODE, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getState() == HttpCode.HTTP_OK) {
                    listener.onValidateCodeSuccess();
                } else {
                    listener.onValidateCodeFailed(response.getMsg());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onValidateCodeFailed(error);
            }
        });
    }

    @Override
    public void login(String phone, String password, final onLoginListener listener) {
        OkRequestParams params = new OkRequestParams();
        params.put("phone", phone);
        params.put("password", password);

        OkHttpUtil.post(Api.LOGIN, params, new OkHttpResBeanHandler<UserDto>() {
            @Override
            public void onSuccess(int code, Headers headers, UserDto response) {
                if (response.getState() == HttpCode.HTTP_OK) {
                    listener.onLoginSuccess(response.getData());
                } else {
                    listener.onLoginFailed(response.getMsg());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onLoginFailed(error);
            }
        });
    }
}
