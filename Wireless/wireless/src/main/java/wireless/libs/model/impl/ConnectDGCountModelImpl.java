package wireless.libs.model.impl;

import com.yunxingzh.wireless.mvp.ui.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.dto.StringDto;
import wireless.libs.model.IConnectDGCountModel;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by stephon on 2016/11/27.
 */

public class ConnectDGCountModelImpl implements IConnectDGCountModel{

    @Override
    public void connectDGCount(int uid, String token, int term, double version, long ts, int nettype, String apmac,final onConnectDGCountListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,apmac);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.CONNECT_DG_WIFI, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onConnectDGCountSuccess();
                } else {
                    listener.onConnectDGCountFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onConnectDGCountFailed(error);
            }
        });
    }
}
