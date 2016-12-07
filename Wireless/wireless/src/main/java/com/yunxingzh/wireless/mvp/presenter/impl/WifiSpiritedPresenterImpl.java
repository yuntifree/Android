package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
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
    public void wifiSpirited(double longitude, double latitude, String ssid, String password) {
        if (iWifiSpiritedView != null){
            iWifiSpiritedView.showProgress();
            iWifiSpiritedModel.wifiSpirited(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),ssid,password,longitude,latitude,this);
        }
    }

    @Override
    public void onWifiSpiritedSuccess() {
        if (iWifiSpiritedView != null) {
            iWifiSpiritedView.hideProgress();
            iWifiSpiritedView.wifiSpiritedSuccess();
        }
    }

    @Override
    public void onWifiSpiritedFailed(int error) {
        if (iWifiSpiritedView != null) {
            iWifiSpiritedView.hideProgress();
            iWifiSpiritedView.showError(error);
        }
    }
}
