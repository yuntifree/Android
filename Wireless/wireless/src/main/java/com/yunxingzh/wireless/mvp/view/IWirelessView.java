package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.WeatherNewsList;

/**
 * Created by stephen on 2017/2/9.
 */

public interface IWirelessView extends IBaseView {
    void weatherNewsSuccess(WeatherNewsList weatherNewsVo);
    void getFontInfoSuccess(FontInfoList fontInfoVo);
    void wifiConnectSuccess();
    void wifiConnectFailed();
}
