package wireless.libs.model;

import wireless.libs.bean.vo.AdvertVo;

/**
 * Created by stephon on 2016/11/27.
 */

public interface IConnectDGCountModel {
    interface onConnectDGCountListener{
        void onConnectDGCountSuccess();
    }

    interface onGetAdvertListener{
        void onGetAdvertSuccess(AdvertVo advertData);
    }

    /***
     * 上报连接东莞wifi次数
     * @param listener
     */
    void connectDGCount(String apmac, onConnectDGCountListener listener);

    /***
     * 获取应用首页广告
     * @param listener
     */
    void getAdvert(onGetAdvertListener listener);
}
