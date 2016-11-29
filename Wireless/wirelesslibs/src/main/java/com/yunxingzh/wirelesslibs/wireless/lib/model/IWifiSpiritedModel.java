package com.yunxingzh.wirelesslibs.wireless.lib.model;

/**
 * Created by stephon on 2016/11/29.
 */

public interface IWifiSpiritedModel {
    interface onWifiSpiritedListener{
        void onWifiSpiritedSuccess();
        void onWifiSpiritedFailed(int error);
    }

    /***
     * wifi公益
     * @param
     * @param
     */
    void wifiSpirited(int uid, String token,int term,double version,long ts,int nettype,String ssid,String password,double longitude, double latitude,onWifiSpiritedListener listener);

}
