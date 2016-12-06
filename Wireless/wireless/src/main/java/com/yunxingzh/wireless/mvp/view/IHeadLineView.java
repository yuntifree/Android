package com.yunxingzh.wireless.mvp.view;


import wireless.libs.bean.vo.FontInfoVo;
import wireless.libs.bean.vo.NewsVo;
import wireless.libs.bean.vo.WeatherNewsVo;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineView extends IBaseView{
    void getHeadLineSuccess(NewsVo newsVo);
    void weatherNewsSuccess(WeatherNewsVo weatherNewsVo);
    void getFontInfoSuccess(FontInfoVo fontInfoVo);
}
