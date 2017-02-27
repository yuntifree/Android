package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.NickNameList;
import wireless.libs.model.IDefHeadModel;
import wireless.libs.model.impl.DefHeadModelImpl;

/**
 * Created by stephen on 2017/2/25.
 */

public class DefHeadPresenterImpl implements IDefHeadPresenter, IDefHeadModel.onGetDefHeadListener, IDefHeadModel.onGetRandNickListener {

    private IDefHeadModel iDefHeadModel;
    private IDefHeadView iDefHeadView;

    public DefHeadPresenterImpl(IDefHeadView view) {
        iDefHeadView = view;
        iDefHeadModel = new DefHeadModelImpl();
    }

    @Override
    public void getDefHead() {
        if (iDefHeadView != null) {
            iDefHeadModel.getDefHead(this);
        }
    }

    @Override
    public void getRandNick() {
        if (iDefHeadView != null) {
            iDefHeadModel.getRandNick(this);
        }
    }

    @Override
    public void onDestroy() {
        iDefHeadView = null;
    }

    @Override
    public void onGetDefHeadSuccess(DefHeadList defHeadList) {
        if (iDefHeadView != null) {
            iDefHeadView.getDefHeadSuccess(defHeadList);
        }
    }

    @Override
    public void onGetRandNickSuccess(NickNameList nickNameList) {
        if (iDefHeadView != null) {
            iDefHeadView.getRandNickSuccess(nickNameList);
        }
    }
}
