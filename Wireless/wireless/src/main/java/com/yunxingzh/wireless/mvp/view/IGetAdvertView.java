package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.StretchVo;

/**
 * Created by stephen on 2016/12/15.
 */

public interface IGetAdvertView extends IBaseView{
    void getAdvertSuccess(AdvertVo advertData);
    void getStretchSuccess(StretchVo stretchVo);
}
