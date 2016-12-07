package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IWifiManagerPresenter;
import com.yunxingzh.wireless.mvp.view.IWifiManagerView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.ArrayList;

import wireless.libs.bean.vo.WifiVo;
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
            iWifiManagerModel.getWifi(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),longitude,latitude,ssids,this);
        }
    }

    @Override
    public void onGetWifiSuccess(WifiVo wifiVo) {
        if (iWifiManagerView != null) {
            iWifiManagerView.hideProgress();
            iWifiManagerView.getWifiSuccess(wifiVo);
        }
    }

    @Override
    public void onGetWifiFailed(int error) {
        if (iWifiManagerView != null) {
            iWifiManagerView.hideProgress();
            iWifiManagerView.showError(error);
        }
    }

}
