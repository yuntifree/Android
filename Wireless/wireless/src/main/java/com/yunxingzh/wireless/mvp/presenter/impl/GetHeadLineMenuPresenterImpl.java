package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IGetHeadLineMenuPresenter;
import com.yunxingzh.wireless.mvp.view.IGetHeadLineMenuView;

import wireless.libs.bean.resp.MenuList;
import wireless.libs.model.IGetHeadLineMenuModel;
import wireless.libs.model.impl.GetHeadLineMenuModelImpl;

/**
 * Created by stephen on 2016/12/30.
 */

public class GetHeadLineMenuPresenterImpl implements IGetHeadLineMenuPresenter, IGetHeadLineMenuModel.onGetHeadLineMenuListener {

    private IGetHeadLineMenuModel iGetHeadLineMenuModel;
    private IGetHeadLineMenuView iGetHeadLineMenuView;

    public GetHeadLineMenuPresenterImpl(IGetHeadLineMenuView view) {
        iGetHeadLineMenuView = view;
        iGetHeadLineMenuModel = new GetHeadLineMenuModelImpl();
    }

    @Override
    public void getHeadLineMenu() {
        if (iGetHeadLineMenuView != null){
            iGetHeadLineMenuModel.getMenu(this);
        }
    }

    @Override
    public void onGetHeadLineMenuSuccess(MenuList menuList) {
        if (iGetHeadLineMenuView != null){
            iGetHeadLineMenuView.getHeadLineMenuSuccess(menuList);
        }
    }

    @Override
    public void onDestroy() {
        iGetHeadLineMenuView = null;
    }
}
