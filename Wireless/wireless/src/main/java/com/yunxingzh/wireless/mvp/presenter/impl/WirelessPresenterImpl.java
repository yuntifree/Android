package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.view.IGetJokesView;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wireless.mvp.view.IWirelessView;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.model.IWeatherNewsModel;
import wireless.libs.model.IWirelessModel;
import wireless.libs.model.impl.WeatherNewsModelImpl;
import wireless.libs.model.impl.WirelessModelImpl;

/**
 * Created by stephen on 2017/2/9.
 */

public class WirelessPresenterImpl implements IWirelessPresenter, IWirelessModel.onClickCountListener,
        IWeatherNewsModel.onWeatherNewsListener, IWirelessModel.onGetFontInfoListener, IWirelessModel.onWifiConnectListener {

    private IWeatherNewsModel iWeatherNewsModel;
    private IWirelessModel iWirelessModel;
    private IWirelessView iWirelessView;
    private IHeadLineView iHeadLineView;
    private IServiceView iServiceView;
    private IGetLiveListView iGetLiveListView;
    private IGetJokesView iGetJokesView;

    public WirelessPresenterImpl(IWirelessView view) {
        iWirelessView = view;
        iHeadLineView = null;
        iServiceView = null;
        iGetLiveListView = null;
        iGetJokesView = null;
        iWirelessModel = new WirelessModelImpl();
        iWeatherNewsModel = new WeatherNewsModelImpl();
    }

    public WirelessPresenterImpl(IHeadLineView view) {
        iWirelessView = null;
        iServiceView = null;
        iGetLiveListView = null;
        iGetJokesView = null;
        iHeadLineView = view;
        iWirelessModel = new WirelessModelImpl();
    }

    public WirelessPresenterImpl(IServiceView view) {
        iWirelessView = null;
        iHeadLineView = null;
        iGetLiveListView = null;
        iGetJokesView = null;
        iServiceView = view;
        iWirelessModel = new WirelessModelImpl();
    }

    public WirelessPresenterImpl(IGetLiveListView view) {
        iWirelessView = null;
        iHeadLineView = null;
        iServiceView = null;
        iGetJokesView = null;
        iGetLiveListView = view;
        iWirelessModel = new WirelessModelImpl();
    }

    public WirelessPresenterImpl(IGetJokesView view) {
        iWirelessView = null;
        iHeadLineView = null;
        iServiceView = null;
        iGetLiveListView = null;
        iGetJokesView = view;
        iWirelessModel = new WirelessModelImpl();
    }

    @Override
    public void clickCount(int id,int type, String name) {
        if (iWirelessView != null){
            iWirelessModel.clickCount(id,type,name,this);
        }
    }

    @Override
    public void weatherNews() {
        if (iWirelessView != null){
            iWeatherNewsModel.weatherNews(this);
        }
    }

    @Override
    public void getFontInfo() {
        if (iWirelessView != null){
            iWirelessModel.getFontInfo(this);
        }
    }

    @Override
    public void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac) {
        if (iWirelessView != null) {
            iWirelessModel.wifiConnect(wlanacname,wlanuserip,wlanacip,wlanusermac,apmac,this);
        }
    }

    @Override
    public void onClickCountSuccess() {
        if (iWirelessView != null){
        }
    }

    @Override
    public void onWeatherNewsSuccess(WeatherNewsList weatherNewsVo) {
        if (iWirelessView != null){
            iWirelessView.weatherNewsSuccess(weatherNewsVo);
        }
    }

    @Override
    public void onGetFontInfoSuccess(FontInfoList fontInfoVo) {
        if (iWirelessView != null){
            iWirelessView.getFontInfoSuccess(fontInfoVo);
        }
    }

    @Override
    public void onWifiConnectSuccess() {
        if (iWirelessView != null) {
            iWirelessView.wifiConnectSuccess();
        }
    }

    @Override
    public void onWifiConnectFaild() {
        if (iWirelessView != null) {
            iWirelessView.wifiConnectFailed();
        }
    }

    @Override
    public void onDestroy() {
        iWirelessView = null;
        iHeadLineView = null;
        iGetLiveListView = null;
        iServiceView = null;
        iGetJokesView = null;
    }
}
