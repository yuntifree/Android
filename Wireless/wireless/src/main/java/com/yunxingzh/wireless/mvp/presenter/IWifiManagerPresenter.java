package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWifiManagerPresenter extends IBasePresenter{
    void getWifi(double longitude, double latitude, String[] ssids);
}
