package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.IServiceView;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.LiveList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.model.IWeatherNewsModel;
import wireless.libs.model.impl.HeadLineModelImpl;
import wireless.libs.model.impl.WeatherNewsModelImpl;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener,IHeadLineModel.onClickCountListener,
        IWeatherNewsModel.onWeatherNewsListener,IHeadLineModel.onGetFontInfoListener, IHeadLineModel.onGetLiveListListener {

    private IWeatherNewsModel iWeatherNewsModel = null;
    private IHeadLineView iHeadLineView = null;
    private IGetLiveListView iGetLiveListView = null;
    private IHeadLineModel iHeadLineModel = null;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iGetLiveListView = null;
        iHeadLineModel = new HeadLineModelImpl();
        iWeatherNewsModel = new WeatherNewsModelImpl();
    }

    public HeadLinePresenterImpl(IGetLiveListView view) {
        iHeadLineView = null;
        iGetLiveListView = view;
        iHeadLineModel = new HeadLineModelImpl();
        iWeatherNewsModel = new WeatherNewsModelImpl();
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
    public void clickCount(int id,int type) {
        if (iHeadLineView != null){
            iHeadLineModel.clickCount(id,type,this);
        }
    }

    @Override
    public void weatherNews() {
        if (iHeadLineView != null){
            iWeatherNewsModel.weatherNews(this);
        }
    }

    @Override
    public void getFontInfo() {
        if (iHeadLineView != null){
            iHeadLineModel.getFontInfo(this);
        }
    }

    @Override
    public void onDestroy() {
        // TODO: model Destroy
        // iWeatherNewsModel = null;
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
    public void onClickCountSuccess() {
        if (iHeadLineView != null){
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
    public void onWeatherNewsSuccess(WeatherNewsList weatherNewsVo) {
        if (iHeadLineView != null){
            iHeadLineView.weatherNewsSuccess(weatherNewsVo);
        }
    }

    @Override
    public void onGetFontInfoSuccess(FontInfoList fontInfoVo) {
        if (iHeadLineView != null){
            iHeadLineView.getFontInfoSuccess(fontInfoVo);
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
