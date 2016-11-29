package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IServiceModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServiceModelImpl implements IServiceModel{
    @Override
    public void getService(int uid, String token, int term, double version, long ts, int nettype, final onGetServiceListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0,"","");
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.SERVICE, params, new OkHttpResBeanHandler<ServiceVo>() {
            @Override
            public void onSuccess(int code, Headers headers, ServiceVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetServiceSuccess(response);
                } else {
                    listener.onGetServiceFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetServiceFailed(error);
            }
        });
    }
}
