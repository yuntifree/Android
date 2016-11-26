package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IServiceModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.ServiceModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServicePresenterImpl implements IServicePresenter,IServiceModel.onGetServiceListener {

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
            iServiceModel.getService(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),this);
        }
    }

    @Override
    public void onGetServiceSuccess(ServiceVo serviceVo) {
        if (iServiceView != null) {
            iServiceView.hideProgress();
            iServiceView.getServiceSuccess(serviceVo);
        }
    }

    @Override
    public void onGetServiceFailed(int error) {
        if (iServiceView != null) {
            iServiceView.hideProgress();
            iServiceView.showError(error);
        }
    }
}
