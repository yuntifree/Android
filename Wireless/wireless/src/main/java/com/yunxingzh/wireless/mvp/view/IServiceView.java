package com.yunxingzh.wireless.mvp.view;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;

/**
 * Created by stephon on 2016/11/6.
 */

public interface IServiceView extends IBaseView{
    void getServiceSuccess(ServiceVo serviceVo);
}
