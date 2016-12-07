package wireless.libs.model;

import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/18.
 */

public interface IWifiMapModel {
    interface onGetWifiMapListener{
        void onGetWifiMapSuccess(WifiMapList wifiMapVo);
    }

    /***
     * 根据经纬度获取地图附近热点列表
     * @param
     * @param
     */
    void getWifiMap(double longitude, double latitude, onGetWifiMapListener listener);
}