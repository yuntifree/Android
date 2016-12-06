package wireless.libs.model.impl;

import com.yunxingzh.wireless.mvp.ui.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.dto.StringDto;
import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.model.IUserModel;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by Stephen on 2016/9/9.
 */
public class UserModelImpl implements IUserModel {

    @Override
    public void getValidateCode(int term, double version, long ts, int nettype, int type, final onValidateCodeListener listener) {
        String jsonStr = JsonUtils.jsonStirngForUser(term, version, ts, nettype, type, "", "", "", "", "");
        OkRequestParams param = new OkRequestParams();
        param.put("key", jsonStr);
        OkHttpUtil.post(Api.GET_PHONE_CODE, param, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onValidateCodeSuccess();
                } else {
                    listener.onValidateCodeFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onValidateCodeFailed(error);
            }
        });
    }

    @Override
    public void register(int term, double version, long ts, int nettype, String username, String password,
                         String model, String channel, String udid, final onRegisterListener listener) {
        String jsonStr = JsonUtils.jsonStirngForUser(term, version, ts, nettype, 0, username , password, model, channel, udid);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.REGISTER, params, new OkHttpResBeanHandler<UserInfoVo>() {
            @Override
            public void onSuccess(int code, Headers headers, UserInfoVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onRegisterSuccess(response);
                } else {
                    listener.onRegisterFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onRegisterFailed(error);
            }
        });
    }
}
