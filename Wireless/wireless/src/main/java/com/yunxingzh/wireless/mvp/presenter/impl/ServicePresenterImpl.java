package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.view.IServiceView;

import wireless.libs.bean.resp.ServiceList;
import wireless.libs.model.IServiceModel;
import wireless.libs.model.impl.ServiceModelImpl;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServicePresenterImpl implements IServicePresenter,IServiceModel.onGetServiceListListener {

    private IServiceView iServiceView;
    private IServiceModel iServiceModel;

    public ServicePresenterImpl(IServiceView view) {
        iServiceView = view;
        iServiceModel = new ServiceModelImpl();
    }

    @Override
    public void getService() {
        if (iServiceView != null){
            iServiceView.showProgress();
            iServiceModel.getServiceList(this);
        }
    }

    /**
     * hoge: new method
     * @param serviceList
     */
    @Override
    public void onGetServiceListSuccess(ServiceList serviceList) {
        if (iServiceView != null) {
            iServiceView.hideProgress();
            iServiceView.getServiceListSuccess(serviceList);
        }
    }

    @Override
    public void onDestroy() {
        iServiceView = null;
    }
}
