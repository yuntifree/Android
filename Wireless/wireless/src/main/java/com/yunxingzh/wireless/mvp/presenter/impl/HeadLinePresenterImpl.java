package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.LiveList;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.model.impl.HeadLineModelImpl;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener, IHeadLineModel.onGetLiveListListener {

    private IHeadLineView iHeadLineView = null;
    private IGetLiveListView iGetLiveListView = null;
    private IHeadLineModel iHeadLineModel = null;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iGetLiveListView = null;
        iHeadLineModel = new HeadLineModelImpl();
    }

    public HeadLinePresenterImpl(IGetLiveListView view) {
        iHeadLineView = null;
        iGetLiveListView = view;
        iHeadLineModel = new HeadLineModelImpl();
    }

    public HeadLinePresenterImpl() {
        iHeadLineModel = new HeadLineModelImpl();
    }

    @Override
    public void getHeadLine(int type, int seq) {
        if (iHeadLineView != null){
            iHeadLineModel.getHeadLine(type,seq,this);
        }
    }

    @Override
    public void onDestroy() {
        // TODO: model Destroy
        iHeadLineView = null;
       // iHeadLineModel = null;
        iGetLiveListView = null;
    }

    @Override
    public void getLiveList(int offset) {
        if (iGetLiveListView != null){
            iHeadLineModel.getLiveList(offset,this);
        }
    }

    @Override
    public void onGetHeadLineSuccess(HotInfoList newsVo) {
        if (iHeadLineView != null){
            iHeadLineView.getHeadLineSuccess(newsVo);
        }
    }

    @Override
    public void onGetHeadLineFaild() {
        if (iHeadLineView != null){
            iHeadLineView.getHeadLineFaild();
        }
    }

    @Override
    public void onGetLiveListSuccess(LiveList liveList) {
        if (iGetLiveListView != null){
            iGetLiveListView.getLiveListSuccess(liveList);
        }
    }

    @Override
    public void onGetLiveListFaild() {
        if (iGetLiveListView != null){
            iGetLiveListView.getLiveListFaild();
        }
    }

}
