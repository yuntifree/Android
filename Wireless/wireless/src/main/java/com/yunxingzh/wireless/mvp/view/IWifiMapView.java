package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/18.
 */

public interface IWifiMapView  extends IBaseView{
    void getWifiMapSuccess(WifiMapVo wifiMapVo);
}
