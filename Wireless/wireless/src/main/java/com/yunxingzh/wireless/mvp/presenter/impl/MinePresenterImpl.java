package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IMinePresenter;
import com.yunxingzh.wireless.mvp.view.IMineView;

import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.model.IMineModel;
import wireless.libs.model.impl.MineModelImpl;

/**
 * Created by stephen on 2017/2/24.
 */

public class MinePresenterImpl implements IMinePresenter, IMineModel.onImageUploadListener {

    private IMineModel iMineModel;
    private IMineView iMineView;

    public MinePresenterImpl(IMineView view) {
        iMineView = view;
        iMineModel = new MineModelImpl();
    }

    @Override
    public void applyImageUpload(int size, String format) {
        if (iMineView != null) {
            iMineModel.applyImageUpload(size, format, this);
        }
    }

    @Override
    public void onDestroy() {
        iMineView = null;
    }

    @Override
    public void onImageUploadSuccess(ImageUploadVo imageUploadVo) {
        if (iMineView != null) {
            iMineView.applyImageUploadSuccess(imageUploadVo);
        }
    }
}
