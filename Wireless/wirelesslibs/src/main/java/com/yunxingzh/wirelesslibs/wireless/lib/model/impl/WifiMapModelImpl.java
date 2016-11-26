package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiMapVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IWifiMapModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/18.
 */

public class WifiMapModelImpl implements IWifiMapModel{

    @Override
    public void getWifiMap(int uid, String token, int term, double version, long ts, int nettype, double longitude, double latitude,final onGetWifiMapListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,longitude,latitude);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_WIFI_MAP, params, new OkHttpResBeanHandler<WifiMapVo>() {
            @Override
            public void onSuccess(int code, Headers headers, WifiMapVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetWifiMapSuccess(response);
                } else {
                    listener.onGetWifiMapFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetWifiMapFailed(error);
            }
        });
    }
}
