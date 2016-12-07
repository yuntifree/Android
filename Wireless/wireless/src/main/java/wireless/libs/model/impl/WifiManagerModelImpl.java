package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.resp.WifiList;
import wireless.libs.model.IWifiManagerModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/15.
 */

public class WifiManagerModelImpl implements IWifiManagerModel {

    @Override
    public void getWifi(double longitude, double latitude, String[] ssids, final onGetWifiListener listener) {
        NetWorkWarpper.getWifi(longitude,latitude,ssids,new HttpHandler<WifiList>() {
            @Override
            public void onSuccess(ServerTip serverTip, WifiList resquestVo) {
                listener.onGetWifiSuccess(resquestVo);
            }
        });
    }
}
