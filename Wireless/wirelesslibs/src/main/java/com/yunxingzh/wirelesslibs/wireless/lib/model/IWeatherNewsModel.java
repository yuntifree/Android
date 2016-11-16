package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWeatherNewsModel {

    interface onWeatherNewsListener{
        void onWeatherNewsSuccess(WeatherNewsVo weatherNewsVo);
        void onWeatherNewsFailed(int error);
        void onWeatherNewsFailed(String errorMsg);
    }

    /***
     * 获取首页新闻及天气
     * @param listener
     */
    void weatherNews(int uid, String token, int term, double version, long ts,
                    int nettype, onWeatherNewsListener listener);
}
