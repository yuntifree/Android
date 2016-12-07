package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.WifiList;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWifiManagerView extends IBaseView {
    void getWifiSuccess(WifiList wifiVo);
}
