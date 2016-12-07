package wireless.libs.model;

import wireless.libs.bean.resp.ServiceList;
import wireless.libs.bean.vo.ServiceVo;

/**
 * Created by stephon on 2016/11/7.
 */

public interface IServiceModel {
//    interface onGetServiceListener{
//        void onGetServiceSuccess(ServiceVo serviceVo);
//        void onGetServiceFailed(int error);
//    }

    interface onGetServiceListListener{
        void onGetServiceListSuccess(ServiceList serviceList);
    }

    /***
     * 获取服务列表
     * @param
     * @param
     */
    //void getService(int uid, String token, int term, double version, long ts, int nettype, onGetServiceListener listener);

    void getServiceList(onGetServiceListListener listListener);
}
