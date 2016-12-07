package wireless.libs.model.impl;

import com.yunxingzh.wireless.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.dto.StringDto;
import wireless.libs.model.IWifiSpiritedModel;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

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
