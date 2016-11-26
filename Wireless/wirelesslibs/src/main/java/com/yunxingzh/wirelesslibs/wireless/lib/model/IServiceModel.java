package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;

/**
 * Created by stephon on 2016/11/7.
 */

public interface IServiceModel {
    interface onGetServiceListener{
        void onGetServiceSuccess(ServiceVo serviceVo);
        void onGetServiceFailed(int error);
    }

    /***
     * 获取服务列表
     * @param
     * @param
     */
    void getService(int uid, String token,int term,double version,long ts,int nettype,onGetServiceListener listener);
}
