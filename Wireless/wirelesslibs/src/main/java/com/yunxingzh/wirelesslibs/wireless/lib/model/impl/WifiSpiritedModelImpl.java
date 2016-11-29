package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.StringDto;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiMapVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IWifiSpiritedModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/29.
 */

public class WifiSpiritedModelImpl implements IWifiSpiritedModel {
    @Override
    public void wifiSpirited(int uid, String token, int term, double version, long ts, int nettype, String ssid, String password, double longitude, double latitude,final onWifiSpiritedListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,longitude,latitude,ssid,password);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.WIFI_SPIRITED, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onWifiSpiritedSuccess();
                } else {
                    listener.onWifiSpiritedFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onWifiSpiritedFailed(error);
            }
        });
    }
}
