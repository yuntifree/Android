package com.yunxingzh.wireless.mvp.presenter;

import java.util.List;

/**
 * Created by stephon on 2016/11/15.
 */

public interface IWifiManagerPresenter {
    void getWifi(double longitude, double latitude, String[] ssids);
}
