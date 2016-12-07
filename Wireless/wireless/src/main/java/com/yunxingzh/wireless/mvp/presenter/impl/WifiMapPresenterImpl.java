package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IWifiMapPresenter;
import com.yunxingzh.wireless.mvp.view.IWifiMapView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.WifiMapVo;
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
    public void getWifiMap(double longitude, double latitude) {
        if (iWifiMapView != null){
            iWifiMapView.showProgress();
            iWifiMapModel.getWifiMap(MainApplication.sApplication.getUser().getData().getUid(), MainApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),longitude,latitude,this);
        }
    }

    @Override
    public void onGetWifiMapSuccess(WifiMapVo wifiMapVo) {
        if (iWifiMapView != null) {
            iWifiMapView.hideProgress();
            iWifiMapView.getWifiMapSuccess(wifiMapVo);
        }
    }

    @Override
    public void onGetWifiMapFailed(int error) {
        if (iWifiMapView != null) {
            iWifiMapView.hideProgress();
            iWifiMapView.showError(error);
        }
    }

}
