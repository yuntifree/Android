package wireless.libs.model;

import wireless.libs.bean.resp.WifiList;

/**
 * Created by stephon on 2016/11/15.
 */


public interface IWifiManagerModel {
    interface onGetWifiListener{
        void onGetWifiSuccess(WifiList wifiVo);
    }

    /***
     * 根据经纬度获取附近wifi列表
     * @param
     * @param
     */
    void getWifi(double longitude, double latitude, String[] ssids, onGetWifiListener listener);
}
