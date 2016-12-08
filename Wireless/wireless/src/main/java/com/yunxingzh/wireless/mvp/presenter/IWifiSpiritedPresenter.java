package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by stephon on 2016/11/29.
 */

public interface IWifiSpiritedPresenter extends IBasePresenter{
    void wifiSpirited(String ssid,String password,double longitude,double latitude);
}
