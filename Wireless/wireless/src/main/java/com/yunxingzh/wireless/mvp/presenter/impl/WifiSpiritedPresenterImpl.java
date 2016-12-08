package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IWifiSpiritedPresenter;
import com.yunxingzh.wireless.mvp.view.IWifiSpiritedView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.model.IWifiSpiritedModel;
import wireless.libs.model.impl.WifiSpiritedModelImpl;

/**
 * Created by stephon on 2016/11/29.
 */

public class WifiSpiritedPresenterImpl implements IWifiSpiritedPresenter,IWifiSpiritedModel.onWifiSpiritedListener {

    private IWifiSpiritedModel iWifiSpiritedModel;
    private IWifiSpiritedView iWifiSpiritedView;

    public WifiSpiritedPresenterImpl(IWifiSpiritedView view) {
        iWifiSpiritedView = view;
        iWifiSpiritedModel = new WifiSpiritedModelImpl();
    }

    @Override
    public void wifiSpirited(String ssid,String password,double longitude,double latitude) {
        if (iWifiSpiritedView != null){
            iWifiSpiritedModel.wifiSpirited(ssid,password,longitude,latitude,this);
        }
    }

    @Override
    public void onWifiSpiritedSuccess() {
        if (iWifiSpiritedView != null) {
            iWifiSpiritedView.wifiSpiritedSuccess();
        }
    }

    @Override
    public void onDestroy() {
        iWifiSpiritedView = null;
    }
}
