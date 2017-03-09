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

}
