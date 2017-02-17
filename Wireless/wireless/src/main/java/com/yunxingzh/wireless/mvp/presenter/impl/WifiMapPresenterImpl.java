package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IWifiMapPresenter;
import com.yunxingzh.wireless.mvp.view.IWifiMapView;

import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.model.IWifiMapModel;
import wireless.libs.model.impl.WifiMapModelImpl;

/**
 * Created by stephon on 2016/11/18.
 */

public class WifiMapPresenterImpl implements IWifiMapPresenter,IWifiMapModel.onGetWifiMapListener {

    private IWifiMapModel iWifiMapModel;
    private IWifiMapView iWifiMapView;

    public WifiMapPresenterImpl(IWifiMapView view) {
        iWifiMapView = view;
        iWifiMapModel = new WifiMapModelImpl();
    }

    @Override
    public void getWifiMap() {
        if (iWifiMapView != null){
            iWifiMapModel.getWifiMap(this);
        }
    }

    @Override
    public void onGetWifiMapSuccess(WifiMapList wifiMapVo) {
        if (iWifiMapView != null) {
            iWifiMapView.getWifiMapSuccess(wifiMapVo);
        }
    }

    @Override
    public void onDestroy() {
        iWifiMapView = null;
    }
}
