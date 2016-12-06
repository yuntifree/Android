package com.yunxingzh.wireless.mvp.view;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineView extends IBaseView{
    void getHeadLineSuccess(NewsVo newsVo);
    void weatherNewsSuccess(WeatherNewsVo weatherNewsVo);
    void getFontInfoSuccess(FontInfoVo fontInfoVo);
}
