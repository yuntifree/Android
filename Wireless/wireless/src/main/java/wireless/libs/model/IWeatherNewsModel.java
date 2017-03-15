package wireless.libs.model;

import wireless.libs.bean.resp.WeatherNewsList;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWeatherNewsModel {

    interface onWeatherNewsListener{
        void onWeatherNewsSuccess(WeatherNewsList weatherNewsVo);
        void onWeatherNewsFailed();
    }

    /***
     * 获取首页新闻及天气
     * @param listener
     */
    void weatherNews(onWeatherNewsListener listener);
}
