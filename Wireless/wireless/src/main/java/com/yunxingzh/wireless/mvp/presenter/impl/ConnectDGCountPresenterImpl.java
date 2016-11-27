package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IConnectDGCountPresenter;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IConnectDGCountModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.ConnectDGCountModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

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
            iConnectDGCountModel.connectDGCount(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),apmac,this);
        }
    }

    @Override
    public void onConnectDGCountSuccess() {
        if (iConnectDGCountView != null){
            iConnectDGCountView.hideProgress();
            iConnectDGCountView.connectDGCountSuccess();
        }
    }

    @Override
    public void onConnectDGCountFailed(int error) {
        if(iConnectDGCountView != null){
            iConnectDGCountView.hideProgress();
            iConnectDGCountView.showError(error);
        }
    }
}
