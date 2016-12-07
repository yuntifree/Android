package wireless.libs.model.impl;

import com.yunxingzh.wireless.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.resp.ServiceList;
import wireless.libs.bean.vo.ServiceVo;
import wireless.libs.model.IServiceModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServiceModelImpl implements IServiceModel{
//    @Override
//    public void getService(int uid, String token, int term, double version, long ts, int nettype, final onGetServiceListener listener) {
//        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0,"","");
//        OkRequestParams params = new OkRequestParams();
//        params.put("key", jsonStr);
//
//        OkHttpUtil.post(Api.SERVICE, params, new OkHttpResBeanHandler<ServiceVo>() {
//            @Override
//            public void onSuccess(int code, Headers headers, ServiceVo response) {
//                if (response.getErrno() == HttpCode.HTTP_OK) {
//                    listener.onGetServiceSuccess(response);
//                } else {
//                    listener.onGetServiceFailed(response.getErrno());
//                }
//            }
//
//            @Override
//            public void onFailure(int code, Headers headers, int error, Throwable t) {
//                listener.onGetServiceFailed(error);
//            }
//        });
//    }

    @Override
    public void getServiceList(final onGetServiceListListener listListener) {
        NetWorkWarpper.services(new HttpHandler<ServiceList>() {
            @Override
            public void onSuccess(ServerTip serverTip, ServiceList serviceList) {
                listListener.onGetServiceListSuccess(serviceList);
            }
        });
    }
}
