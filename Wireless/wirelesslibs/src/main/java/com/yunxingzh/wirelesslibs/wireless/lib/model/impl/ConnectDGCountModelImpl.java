package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.StringDto;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IConnectDGCountModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/27.
 */

public class ConnectDGCountModelImpl implements IConnectDGCountModel{

    @Override
    public void connectDGCount(int uid, String token, int term, double version, long ts, int nettype, String apmac,final onConnectDGCountListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,apmac);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.CONNECT_DG_WIFI, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onConnectDGCountSuccess();
                } else {
                    listener.onConnectDGCountFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onConnectDGCountFailed(error);
            }
        });
    }
}
