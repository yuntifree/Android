package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.StringDto;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.UserDto;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IUserModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by Stephen on 2016/9/9.
 *
 */
public class UserModelImpl implements IUserModel {

    @Override
    public void getValidateCode(int term,double version,long ts,int nettype,int type, String phone,final onValidateCodeListener listener) {
        String jsonStr = JsonUtils.jsonStirngForUser(term, version, ts, nettype, type, phone,"","",0,"","","");
        OkRequestParams param = new OkRequestParams();
        param.put("key", jsonStr);
        OkHttpUtil.post(Api.GET_PHONE_CODE, param, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onValidateCodeSuccess();
                } else {
                    listener.onValidateCodeFailed(response.getDesc());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onValidateCodeFailed(error);
            }
        });
    }

    @Override
    public void register(int term,double version,long ts,int nettype,String username,String password,
                      int code,String model,String channel,String udid,final onRegisterListener listener) {
        String jsonStr = JsonUtils.jsonStirngForUser(term, version, ts, nettype,0,"", username, password,code,model,channel,udid);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.REGISTER, params, new OkHttpResBeanHandler<UserInfoVo>() {
            @Override
            public void onSuccess(int code, Headers headers, UserInfoVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onRegisterSuccess(response);
                } else {
                    listener.onRegisterFailed(response.getDesc());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onRegisterFailed(error);
            }
        });
    }
}
