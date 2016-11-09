package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IHeadLineModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.IHeadLineModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener,IHeadLineModel.onVideoPlayerCountListener {

    private IHeadLineView iHeadLineView;
    private IHeadLineModel iHeadLineModel;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iHeadLineModel = new IHeadLineModelImpl();
    }

    @Override
    public void getHeadLine(int type, int seq) {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iHeadLineModel.getHeadLine(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),type,seq,this);
        }
    }

    @Override
    public void videoPlayerCount(int id) {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iHeadLineModel.videoPlayerCount(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),id,this);
        }
    }

    @Override
    public void onGetHeadLineSuccess(NewsVo newsVo) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.getHeadLineSuccess(newsVo);
        }
    }

    @Override
    public void onGetHeadLineFailed(int error) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onGetHeadLineFailed(String errorMsg) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showErrorMsg(errorMsg);
        }
    }

    @Override
    public void onVideoPlayerCountSuccess() {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
        }
    }

    @Override
    public void onVideoPlayerCountFailed(int error) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onVideoPlayerCountFailed(String errorMsg) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showErrorMsg(errorMsg);
        }
    }
}
