package com.yunxingzh.wirelesslibs.wireless.lib.model.impl;

import com.yunxingzh.wirelesslibs.wireless.lib.api.Api;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IHeadLineModel;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkRequestParams;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpResBeanHandler;

import okhttp3.Headers;

/**
 * Created by stephon on 2016/11/3.
 */

public class IHeadLineModelImpl implements IHeadLineModel {
    @Override
    public void getHeadLine(int type, int seq, final onGetHeadLineListener listener) {
        OkRequestParams params = new OkRequestParams();
        params.put("type", type);
        params.put("seq", seq);

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
}
