package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.model.IDefHeadModel;
import wireless.libs.model.impl.DefHeadModelImpl;

/**
 * Created by stephen on 2017/2/25.
 */

public class DefHeadPresenterImpl implements IDefHeadPresenter, IDefHeadModel.onGetDefHeadListener {

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
    public void onDestroy() {
        iDefHeadView = null;
    }

    @Override
    public void onGetDefHeadSuccess(DefHeadList defHeadList) {
        if (iDefHeadView != null) {
            iDefHeadView.getDefHeadSuccess(defHeadList);
        }
    }
}
