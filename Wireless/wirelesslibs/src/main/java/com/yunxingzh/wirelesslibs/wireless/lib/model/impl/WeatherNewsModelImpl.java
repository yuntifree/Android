package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IWeatherNewsModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/15.
 */

public class WeatherNewsModelImpl implements IWeatherNewsModel {
    @Override
    public void weatherNews(int uid, String token, int term, double version, long ts, int nettype, final onWeatherNewsListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_WEATHER_NEWS, params, new OkHttpResBeanHandler<WeatherNewsVo>() {
            @Override
            public void onSuccess(int code, Headers headers, WeatherNewsVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onWeatherNewsSuccess(response);
                } else {
                    listener.onWeatherNewsFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onWeatherNewsFailed(error);
            }
        });
    }
}
