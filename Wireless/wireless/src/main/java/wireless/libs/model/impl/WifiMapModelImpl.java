package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.model.IWifiMapModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/18.
 */

public class WifiMapModelImpl implements IWifiMapModel {

    @Override
    public void getWifiMap(double longitude, double latitude,final onGetWifiMapListener listener) {
        NetWorkWarpper.getWifiMap(longitude,latitude,new HttpHandler<WifiMapList>() {
            @Override
            public void onSuccess(ServerTip serverTip, WifiMapList resquestVo) {
                listener.onGetWifiMapSuccess(resquestVo);
            }
        });
    }
}
