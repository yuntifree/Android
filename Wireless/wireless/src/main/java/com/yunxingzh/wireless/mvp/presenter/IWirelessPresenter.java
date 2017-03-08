package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by stephen on 2017/2/9.
 */

public interface IWirelessPresenter extends IBasePresenter {
    void clickCount(int id, int type, String name);
    void weatherNews();
    void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac);
    void getNearAps(double longitude, double latitude);
}
