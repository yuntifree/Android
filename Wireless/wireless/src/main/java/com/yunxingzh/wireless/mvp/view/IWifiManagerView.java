package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.vo.WifiVo;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWifiManagerView extends IBaseView {
    void getWifiSuccess(WifiVo wifiVo);
}
