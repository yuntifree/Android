package wireless.libs.model;

import wireless.libs.bean.resp.ServiceList;

/**
 * Created by stephon on 2016/11/7.
 */

public interface IServiceModel {

    interface onGetServiceListListener{
        void onGetServiceListSuccess(ServiceList serviceList);
    }

    /***
     * 获取服务列表
     * @param
     * @param
     */
    void getServiceList(onGetServiceListListener listListener);
}
