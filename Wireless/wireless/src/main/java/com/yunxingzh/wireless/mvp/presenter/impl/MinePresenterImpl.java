package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IMinePresenter;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;
import com.yunxingzh.wireless.mvp.view.IMineView;

import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.model.IMineModel;
import wireless.libs.model.impl.MineModelImpl;

/**
 * Created by stephen on 2017/2/24.
 */

public class MinePresenterImpl implements IMinePresenter, IMineModel.onImageUploadListener, IMineModel.onGetUserInfoListener,
IMineModel.onUpdateUserInfoListener {

    private IMineModel iMineModel;
    private IMineView iMineView;
    private IDefHeadView iDefHeadView;

    public MinePresenterImpl(IMineView view) {
        iDefHeadView = null;
        iMineView = view;
        iMineModel = new MineModelImpl();
    }

    public MinePresenterImpl(IDefHeadView view) {
        iMineView = null;
        iDefHeadView = view;
        iMineModel = new MineModelImpl();
    }

    @Override
    public void applyImageUpload(int size, String format) {
        if (iMineView != null) {
            iMineModel.applyImageUpload(size, format, this);
        }
    }

    @Override
    public void getUserInfo() {
        if (iMineView != null) {
            iMineModel.getUserInfo(MainApplication.get().getUser().uid, this);
        }
    }

    @Override
    public void updateUserInfo(String headurl, String nickname) {
        if (iMineView != null || iDefHeadView != null) {
            iMineModel.updateUserInfo(headurl, nickname, this);
        }
    }

    @Override
    public void onDestroy() {
        iMineView = null;
        iDefHeadView = null;
    }

    @Override
    public void onImageUploadSuccess(ImageUploadVo imageUploadVo) {
        if (iMineView != null) {
            iMineView.applyImageUploadSuccess(imageUploadVo);
        }
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoVo userInfoVo) {
        if (iMineView != null) {
            iMineView.getUserInfoSuccess(userInfoVo);
        }
    }

    @Override
    public void onUpdateUserInfoSuccess() {
        if (iMineView != null) {
            iMineView.updateUserInfoSuccess();
        }
        if (iDefHeadView != null) {
            iDefHeadView.updateUserInfoSuccess();
        }
    }
}
