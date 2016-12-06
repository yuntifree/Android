package wireless.libs.model;

import wireless.libs.bean.vo.WeatherNewsVo;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWeatherNewsModel {

    interface onWeatherNewsListener{
        void onWeatherNewsSuccess(WeatherNewsVo weatherNewsVo);
        void onWeatherNewsFailed(int error);
    }

    /***
     * 获取首页新闻及天气
     * @param listener
     */
    void weatherNews(int uid, String token, int term, double version, long ts,
                     int nettype, onWeatherNewsListener listener);
}
