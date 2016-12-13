package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.vo.AdvertVo;

/**
 * Created by stephon on 2016/11/27.
 */

public interface IConnectDGCountView extends IBaseView{
    void connectDGCountSuccess();
    void getAdvertSuccess(AdvertVo advertData);
}
