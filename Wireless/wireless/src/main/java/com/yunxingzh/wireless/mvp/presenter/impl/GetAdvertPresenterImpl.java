package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IGetAdvertPresenter;
import com.yunxingzh.wireless.mvp.view.IGetAdvertView;
import com.yunxingzh.wireless.utils.AppUtils;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.StretchVo;
import wireless.libs.bean.vo.UpdateVo;
import wireless.libs.model.IGetAdvertModel;
import wireless.libs.model.impl.GetAdvertModelImpl;

/**
 * Created by stephen on 2016/12/15.
 */

public class GetAdvertPresenterImpl implements IGetAdvertPresenter,IGetAdvertModel.onGetAdvertListener,IGetAdvertModel.onGetStretchListener,
IGetAdvertModel.onCheckUpdateListener {

    private IGetAdvertModel iGetAdvertModel;
    private IGetAdvertView iGetAdvertView;

    public GetAdvertPresenterImpl(IGetAdvertView view) {
        iGetAdvertView = view;
        iGetAdvertModel = new GetAdvertModelImpl();
    }

    @Override
    public void getAdvert() {
        if (iGetAdvertView != null) {
            iGetAdvertModel.getAdvert(this);
        }
    }

    @Override
    public void getStretch() {
        if (iGetAdvertView != null) {
            iGetAdvertModel.getStretch(this);
        }
    }

    @Override
    public void checkUpdate() {
        if (iGetAdvertView != null) {
            iGetAdvertModel.checkUpdate(AppUtils.getChannelName(MainApplication.get()), this);
        }
    }

    @Override
    public void onGetAdvertSuccess(AdvertVo advertData) {
        if (iGetAdvertView != null) {
            iGetAdvertView.getAdvertSuccess(advertData);
        }
    }

    @Override
    public void onGetStretchSuccess(StretchVo stretchVo) {
        if (iGetAdvertView != null) {
            iGetAdvertView.getStretchSuccess(stretchVo);
        }
    }

    @Override
    public void onCheckUpdateSuccess(UpdateVo updateVo) {
        if (iGetAdvertView != null) {
            iGetAdvertView.checkUpdateSuccess(updateVo);
        }
    }

    @Override
    public void onDestroy() {
        iGetAdvertView = null;
    }

}
