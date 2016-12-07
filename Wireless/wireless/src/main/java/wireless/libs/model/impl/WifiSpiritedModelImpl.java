package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IWifiSpiritedModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/29.
 */

public class WifiSpiritedModelImpl implements IWifiSpiritedModel {

    @Override
    public void wifiSpirited(String ssid, String password, double longitude, double latitude,final onWifiSpiritedListener listener) {
        NetWorkWarpper.wifiSpirited(ssid,password,longitude,latitude,new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object resquestVo) {
                listener.onWifiSpiritedSuccess();
            }
        });
    }
}
