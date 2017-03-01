package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.FindList;

/**
 * Created by stephon on 2016/11/6.
 */

public interface IServiceView extends IBaseView{
    void getFindSuccess(FindList findList);
}
