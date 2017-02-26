package wireless.libs.model;

import wireless.libs.bean.resp.FindList;
import wireless.libs.bean.resp.ServiceList;

/**
 * Created by stephon on 2016/11/7.
 */

public interface IServiceModel {

    interface onGetServiceListListener{
        void onGetServiceListSuccess(ServiceList serviceList);
    }

    interface onGetFindListener{
        void onGetFindSuccess(FindList findList);
    }

    /***
     * 获取服务列表
     * @param
     * @param
     */
    void getServiceList(onGetServiceListListener listListener);

    /***
     * 发现
     * @param
     * @param
     */
    void getFind(onGetFindListener listListener);
}
