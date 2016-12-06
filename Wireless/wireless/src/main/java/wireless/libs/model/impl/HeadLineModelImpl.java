package wireless.libs.model.impl;

import com.yunxingzh.wireless.utils.JsonUtils;

import okhttp3.Headers;
import wireless.libs.api.Api;
import wireless.libs.api.HttpCode;
import wireless.libs.bean.dto.StringDto;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.vo.FontInfoVo;
import wireless.libs.bean.vo.NewsVo;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;
import wireless.libs.okhttp.OkHttpUtil;
import wireless.libs.okhttp.OkRequestParams;
import wireless.libs.okhttp.response.OkHttpResBeanHandler;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLineModelImpl implements IHeadLineModel {
    @Override
    public void getHeadLine(int uid, String token, int term, double version, long ts,
                            int nettype,int type, int seq, final onGetHeadLineListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,type,seq,0,0,0,"","");
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.NEWS_LIST, params, new OkHttpResBeanHandler<NewsVo>() {
            @Override
            public void onSuccess(int code, Headers headers, NewsVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetHeadLineSuccess(response);
                } else {
                    listener.onGetHeadLineFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetHeadLineFailed(error);
            }
        });
    }

    @Override
    public void clickCount(int uid, String token, int term, double version, long ts, int nettype, int id,int type, final onClickCountListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,type,0,id,0,0,"","");
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.CLICK_COUNT, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onClickCountSuccess();
                } else {
                    listener.onClickCountFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onClickCountFailed(error);
            }
        });
    }

    @Override
    public void getFontInfo(int uid, String token, int term, double version, long ts, int nettype,final onGetFontInfoListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0,"","");
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_FONT_INFO, params, new OkHttpResBeanHandler<FontInfoVo>() {
            @Override
            public void onSuccess(int code, Headers headers, FontInfoVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetFontInfoSuccess(response);
                } else {
                    listener.onGetFontInfoFailed(response.getErrno());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetFontInfoFailed(error);
            }
        });
    }
}
