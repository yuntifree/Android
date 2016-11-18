package com.yunxingzh.wireless.mvp.view;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/18.
 */

public interface IWifiMapView  extends IBaseView{
    void getWifiMapSuccess(WifiMapVo wifiMapVo);
}
