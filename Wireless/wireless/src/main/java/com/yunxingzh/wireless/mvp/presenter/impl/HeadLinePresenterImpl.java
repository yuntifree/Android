package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.IServiceView;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.vo.FontInfoVo;
import wireless.libs.bean.vo.NewsVo;
import wireless.libs.bean.vo.WeatherNewsVo;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.model.IWeatherNewsModel;
import wireless.libs.model.impl.HeadLineModelImpl;
import wireless.libs.model.impl.WeatherNewsModelImpl;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener,IHeadLineModel.onClickCountListener,
        IWeatherNewsModel.onWeatherNewsListener,IHeadLineModel.onGetFontInfoListener {

    private IWeatherNewsModel iWeatherNewsModel;
    private IHeadLineView iHeadLineView;
    private IHeadLineModel iHeadLineModel;
    private IServiceView iServiceView;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iServiceView = null;
        iHeadLineModel = new HeadLineModelImpl();
        iWeatherNewsModel = new WeatherNewsModelImpl();
    }

    public HeadLinePresenterImpl(IServiceView view) {
        iHeadLineView = null;
        iServiceView = view;
        iHeadLineModel = new HeadLineModelImpl();
    }

    @Override
    public void getHeadLine(int type, int seq) {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iHeadLineModel.getHeadLine(MainApplication.sApplication.getUser().getData().getUid(), MainApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),type,seq,this);
        }
    }

    @Override
    public void clickCount(int id,int type) {
        if (iHeadLineView != null){
            //iHeadLineView.showProgress();
            iHeadLineModel.clickCount(MainApplication.sApplication.getUser().getData().getUid(), MainApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),id,type,this);
        }
    }

    @Override
    public void weatherNews() {
        if (iHeadLineView != null){
            //iHeadLineView.showProgress();
            iWeatherNewsModel.weatherNews(MainApplication.sApplication.getUser().getData().getUid(), MainApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),this);
        }
    }

    @Override
    public void getFontInfo() {
        if (iHeadLineView != null){
            //iHeadLineView.showProgress();
            iHeadLineModel.getFontInfo(MainApplication.sApplication.getUser().getData().getUid(), MainApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),this);
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
    public void onClickCountSuccess() {
        if (iHeadLineView != null){
            //iHeadLineView.hideProgress();
        }
    }

    @Override
    public void onClickCountFailed(int error) {
        if(iHeadLineView != null){
            //iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onWeatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
        if (iHeadLineView != null){
            //iHeadLineView.hideProgress();
            iHeadLineView.weatherNewsSuccess(weatherNewsVo);
        }
    }

    @Override
    public void onWeatherNewsFailed(int error) {
        if (iHeadLineView != null){
            //iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onGetFontInfoSuccess(FontInfoVo fontInfoVo) {
        if (iHeadLineView != null){
            //iHeadLineView.hideProgress();
            iHeadLineView.getFontInfoSuccess(fontInfoVo);
        }
    }

    @Override
    public void onGetFontInfoFailed(int error) {
        if (iHeadLineView != null){
            //iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }
}
