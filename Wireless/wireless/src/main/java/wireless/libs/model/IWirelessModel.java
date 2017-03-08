package wireless.libs.model;

import wireless.libs.bean.resp.WifiMapList;

/**
 * Created by stephen on 2017/2/9.
 */

public interface IWirelessModel {
    interface onClickCountListener{
        void onClickCountSuccess();
    }

    interface onWifiConnectListener{
        void onWifiConnectSuccess();
        void onWifiConnectFaild();
    }


    interface onGetNearApsListener{
        void onGetNearApsSuccess(WifiMapList wifiMapList);
    }

    /***
     * 记录点击次数
     * @param listener
     */
    void clickCount(int id, int type, String name, onClickCountListener listener);

    /***
     * 连接东莞wifi
     * @param listener
     */
    void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac, onWifiConnectListener listener);


    /***
     * 获取周围wifi热点
     * @param listener
     */
    void getNearAps(double longitude, double latitude, onGetNearApsListener listener);

}
