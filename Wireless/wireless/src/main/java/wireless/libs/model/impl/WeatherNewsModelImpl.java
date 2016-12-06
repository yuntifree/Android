package wireless.libs.model.impl;

import com.yunxingzh.wireless.mvp.ui.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.vo.WeatherNewsVo;
import wireless.libs.model.IWeatherNewsModel;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by stephon on 2016/11/15.
 */

public class WeatherNewsModelImpl implements IWeatherNewsModel {
    @Override
    public void weatherNews(int uid, String token, int term, double version, long ts, int nettype, final onWeatherNewsListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0,"","");
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
