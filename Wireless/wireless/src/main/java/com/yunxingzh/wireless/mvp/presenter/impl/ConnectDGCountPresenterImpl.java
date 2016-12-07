package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IConnectDGCountPresenter;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;

import wireless.libs.model.IConnectDGCountModel;
import wireless.libs.model.impl.ConnectDGCountModelImpl;

/**
 * Created by stephon on 2016/11/27.
 */

public class ConnectDGCountPresenterImpl implements IConnectDGCountPresenter,IConnectDGCountModel.onConnectDGCountListener {

    private IConnectDGCountModel iConnectDGCountModel;
    private IConnectDGCountView iConnectDGCountView;

    public ConnectDGCountPresenterImpl(IConnectDGCountView view) {
        iConnectDGCountView = view;
        iConnectDGCountModel = new ConnectDGCountModelImpl();
    }

    @Override
    public void connectDGCount(String apmac) {
        if (iConnectDGCountView != null){
            iConnectDGCountView.showProgress();
            iConnectDGCountModel.connectDGCount(apmac,this);
        }
    }

    @Override
    public void onConnectDGCountSuccess() {
        if (iConnectDGCountView != null){
            iConnectDGCountView.hideProgress();
            iConnectDGCountView.connectDGCountSuccess();
        }
    }
}
