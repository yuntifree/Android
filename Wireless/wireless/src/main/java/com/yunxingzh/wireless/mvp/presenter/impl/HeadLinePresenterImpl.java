package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.view.IGetJokesView;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.JokeList;
import wireless.libs.bean.resp.LiveList;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.model.impl.HeadLineModelImpl;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener, IHeadLineModel.onGetLiveListListener,
IHeadLineModel.onGetJokesListener {

    private IHeadLineView iHeadLineView = null;
    private IGetLiveListView iGetLiveListView = null;
    private IHeadLineModel iHeadLineModel = null;
    private IGetJokesView iGetJokesView = null;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iGetJokesView = null;
        iGetLiveListView = null;
        iHeadLineModel = new HeadLineModelImpl();
    }

    public HeadLinePresenterImpl(IGetLiveListView view) {
        iHeadLineView = null;
        iGetJokesView = null;
        iGetLiveListView = view;
        iHeadLineModel = new HeadLineModelImpl();
    }

    public HeadLinePresenterImpl(IGetJokesView view) {
        iHeadLineView = null;
        iGetLiveListView = null;
        iGetJokesView = view;
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
        iGetJokesView = null;
    }

    @Override
    public void getLiveList(int seq) {
        if (iGetLiveListView != null){
            iHeadLineModel.getLiveList(seq,this);
        }
    }

    @Override
    public void getJokes(int seq) {
        if (iGetJokesView != null) {
            iHeadLineModel.getJokes(seq,this);
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

    @Override
    public void onGetJokesSuccess(JokeList jokeList) {
        if (iGetJokesView != null) {
            iGetJokesView.getJokesSuccess(jokeList);
        }
    }

    @Override
    public void onGetJokesFaild() {
        if (iGetJokesView != null) {
            iGetJokesView.getJokesFaild();
        }
    }
}
