package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.dto.StringDto;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IHeadLineModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.JsonUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLineModelImpl implements IHeadLineModel {
    @Override
    public void getHeadLine(int uid, String token, int term, double version, long ts,
                            int nettype,int type, int seq, final onGetHeadLineListener listener) {
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,type,seq,0,0,0);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.NEWS_LIST, params, new OkHttpResBeanHandler<NewsVo>() {
            @Override
            public void onSuccess(int code, Headers headers, NewsVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetHeadLineSuccess(response);
                } else {
                    listener.onGetHeadLineFailed(response.getDesc());
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
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,type,0,id,0,0);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.CLICK_COUNT, params, new OkHttpResBeanHandler<StringDto>() {
            @Override
            public void onSuccess(int code, Headers headers, StringDto response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onClickCountSuccess();
                } else {
                    listener.onClickCountFailed(response.getDesc());
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
        String jsonStr= JsonUtils.jsonStirngForMain(uid,token,term,version,ts,nettype,0,0,0,0,0);
        OkRequestParams params = new OkRequestParams();
        params.put("key", jsonStr);

        OkHttpUtil.post(Api.GET_FONT_INFO, params, new OkHttpResBeanHandler<FontInfoVo>() {
            @Override
            public void onSuccess(int code, Headers headers, FontInfoVo response) {
                if (response.getErrno() == HttpCode.HTTP_OK) {
                    listener.onGetFontInfoSuccess(response);
                } else {
                    listener.onGetFontInfoFailed(response.getDesc());
                }
            }

            @Override
            public void onFailure(int code, Headers headers, int error, Throwable t) {
                listener.onGetFontInfoFailed(error);
            }
        });
    }
}
