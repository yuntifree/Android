package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.resp.WifiMapList;

/**
 * Created by stephen on 2017/2/9.
 */

public interface IWirelessView extends IBaseView {
    void weatherNewsSuccess(WeatherNewsList weatherNewsVo);
    void wifiConnectSuccess();
    void wifiConnectFailed();
    void getNearApsSuccess(WifiMapList wifiMapList);
}
