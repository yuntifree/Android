package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IGetAdvertPresenter;
import com.yunxingzh.wireless.mvp.view.IGetAdvertView;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.model.IGetAdvertModel;
import wireless.libs.model.impl.GetAdvertModelImpl;

/**
 * Created by stephen on 2016/12/15.
 */

public class GetAdvertPresenterImpl implements IGetAdvertPresenter,IGetAdvertModel.onGetAdvertListener {

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
    public void onGetAdvertSuccess(AdvertVo advertData) {
        if (iGetAdvertView != null) {
            iGetAdvertView.getAdvertSuccess(advertData);
        }
    }

    @Override
    public void onDestroy() {
        iGetAdvertView = null;
    }
}
