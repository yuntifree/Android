package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IHeadLineModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IWeatherNewsModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.HeadLineModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.WeatherNewsModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLinePresenterImpl implements IHeadLinePresenter,IHeadLineModel.onGetHeadLineListener,IHeadLineModel.onClickCountListener,
        IWeatherNewsModel.onWeatherNewsListener,IHeadLineModel.onGetFontInfoListener {

    private IWeatherNewsModel iWeatherNewsModel;
    private IHeadLineView iHeadLineView;
    private IHeadLineModel iHeadLineModel;

    public HeadLinePresenterImpl(IHeadLineView view) {
        iHeadLineView = view;
        iHeadLineModel = new HeadLineModelImpl();
        iWeatherNewsModel = new WeatherNewsModelImpl();
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
    public void clickCount(int id,int type) {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iHeadLineModel.clickCount(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),id,type,this);
        }
    }

    @Override
    public void weatherNews() {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iWeatherNewsModel.weatherNews(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),this);
        }
    }

    @Override
    public void getFontInfo() {
        if (iHeadLineView != null){
            iHeadLineView.showProgress();
            iWeatherNewsModel.weatherNews(MyApplication.sApplication.getUser().getData().getUid(),MyApplication.sApplication.getToken(),
                    0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),this);
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
    public void onClickCountSuccess() {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
        }
    }

    @Override
    public void onClickCountFailed(int error) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onClickCountFailed(String errorMsg) {
        if(iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showErrorMsg(errorMsg);
        }
    }

    @Override
    public void onWeatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.weatherNewsSuccess(weatherNewsVo);
        }
    }

    @Override
    public void onWeatherNewsFailed(int error) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onWeatherNewsFailed(String errorMsg) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showErrorMsg(errorMsg);
        }
    }

    @Override
    public void onGetFontInfoSuccess(FontInfoVo fontInfoVo) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.getFontInfoSuccess(fontInfoVo);
        }
    }

    @Override
    public void onGetFontInfoFailed(int error) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showError(error);
        }
    }

    @Override
    public void onGetFontInfoFailed(String errorMsg) {
        if (iHeadLineView != null){
            iHeadLineView.hideProgress();
            iHeadLineView.showErrorMsg(errorMsg);
        }
    }
}
