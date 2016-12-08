package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IWifiManagerPresenter;
import com.yunxingzh.wireless.mvp.view.IWifiManagerView;

import wireless.libs.bean.resp.WifiList;
import wireless.libs.model.IWifiManagerModel;
import wireless.libs.model.impl.WifiManagerModelImpl;

/**
 * Created by stephon on 2016/11/15.
 */

public class WifiManagerPresenterImpl implements IWifiManagerPresenter,IWifiManagerModel.onGetWifiListener {

    private IWifiManagerModel iWifiManagerModel;
    private IWifiManagerView iWifiManagerView;

    public WifiManagerPresenterImpl(IWifiManagerView view) {
        iWifiManagerView = view;
        iWifiManagerModel = new WifiManagerModelImpl();
    }

    @Override
    public void getWifi(double longitude, double latitude, String[] ssids) {
        if (iWifiManagerView != null){
            iWifiManagerView.showProgress();
            iWifiManagerModel.getWifi(longitude,latitude,ssids,this);
        }
    }

    @Override
    public void onGetWifiSuccess(WifiList wifiVo) {
        if (iWifiManagerView != null) {
            iWifiManagerView.hideProgress();
            iWifiManagerView.getWifiSuccess(wifiVo);
        }
    }

    @Override
    public void onDestroy() {
        iWifiManagerView = null;
    }
}
