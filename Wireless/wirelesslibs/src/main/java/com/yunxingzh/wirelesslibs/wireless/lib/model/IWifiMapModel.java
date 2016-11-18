package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/18.
 */

public interface IWifiMapModel {
    interface onGetWifiMapListener{
        void onGetWifiMapSuccess(WifiMapVo wifiMapVo);
        void onGetWifiMapFailed(int error);
        void onGetWifiMapFailed(String errorMsg);
    }

    /***
     * 根据经纬度获取地图附近热点列表
     * @param
     * @param
     */
    void getWifiMap(int uid, String token,int term,double version,long ts,int nettype,double longitude, double latitude,onGetWifiMapListener listener);
}