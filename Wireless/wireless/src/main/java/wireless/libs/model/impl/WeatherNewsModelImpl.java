package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.model.IWeatherNewsModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/15.
 */

public class WeatherNewsModelImpl implements IWeatherNewsModel {
    @Override
    public void weatherNews(final onWeatherNewsListener listener) {
        NetWorkWarpper.weatherNews(new HttpHandler<WeatherNewsList>() {
            @Override
            public void onSuccess(ServerTip serverTip, WeatherNewsList resquestVo) {
                listener.onWeatherNewsSuccess(resquestVo);
            }
        });
    }
}
