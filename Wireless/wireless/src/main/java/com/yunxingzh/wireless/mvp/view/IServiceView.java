package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.FindList;
import wireless.libs.bean.resp.ServiceList;

/**
 * Created by stephon on 2016/11/6.
 */

public interface IServiceView extends IBaseView{
    void getServiceListSuccess(ServiceList services);
    void getFindSuccess(FindList findList);
}
