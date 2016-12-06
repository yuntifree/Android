package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;

import java.util.ArrayList;

/**
 * Created by stephon on 2016/11/15.
 */


public interface IWifiManagerModel {
    interface onGetWifiListener{
        void onGetWifiSuccess(WifiVo wifiVo);
        void onGetWifiFailed(int error);
    }

    /***
     * 根据经纬度获取附近wifi列表
     * @param
     * @param
     */
    void getWifi(int uid, String token, int term, double version, long ts, int nettype, double longitude, double latitude, ArrayList<String> ssids, onGetWifiListener listener);
}
