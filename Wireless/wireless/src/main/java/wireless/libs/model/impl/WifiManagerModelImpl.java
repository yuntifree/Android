package wireless.libs.model.impl;

import com.yunxingzh.wireless.mvp.ui.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.vo.WifiVo;
import wireless.libs.model.IWifiManagerModel;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by stephon on 2016/11/15.
 */

public class WifiManagerModelImpl implements IWifiManagerModel {

    @Override
    public void getWifi(int uid, String token, int term, double version, long ts, int nettype, double longitude, double latitude, String[] ssids, final onGetWifiListener listener) {
        String jsonStr= JsonUtils.jsonStirngForWifi(uid,token,term,version,ts,nettype,longitude,latitude,ssids);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_WIFI_LIST, params, new OkHttpResBeanHandler<WifiVo>() {
            @Override
            public void onSuccess(int code, Headers headers, WifiVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetWifiSuccess(response);
                } else {
                    listener.onGetWifiFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetWifiFailed(error);
            }
        });
    }
}
