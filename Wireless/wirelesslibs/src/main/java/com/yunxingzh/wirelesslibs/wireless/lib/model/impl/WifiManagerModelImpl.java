package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IWifiManagerModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/15.
 */

public class WifiManagerModelImpl implements IWifiManagerModel {

    @Override
    public void getWifi(int uid, String token, int term, double version, long ts, int nettype, double longitude, double latitude, final onGetWifiListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,longitude,latitude);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_WIFI_LIST, params, new OkHttpResBeanHandler<WifiVo>() {
            @Override
            public void onSuccess(int code, Headers headers, WifiVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetWifiSuccess(response);
                } else {
                    listener.onGetWifiFailed(response.getDesc());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetWifiFailed(error);
            }
        });
    }
}
