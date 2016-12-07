package com.yunxingzh.wireless.mvp.view;


import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.WeatherNewsList;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineView extends IBaseView{
    void getHeadLineSuccess(HotInfoList newsVo);
    void weatherNewsSuccess(WeatherNewsList weatherNewsVo);
    void getFontInfoSuccess(FontInfoList fontInfoVo);
}
