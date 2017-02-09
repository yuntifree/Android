package com.yunxingzh.wireless.mvp.view;


import wireless.libs.bean.resp.HotInfoList;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineView extends IBaseView{
    void getHeadLineSuccess(HotInfoList newsVo);
    void getHeadLineFaild();
}
