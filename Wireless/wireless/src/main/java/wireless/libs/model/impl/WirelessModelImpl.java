package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IWirelessModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2017/2/9.
 */

public class WirelessModelImpl implements IWirelessModel {

    @Override
    public void clickCount(int id, int type, String name, final onClickCountListener listener) {
        NetWorkWarpper.clickCount(id, type, name, new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object o) {
                listener.onClickCountSuccess();
            }
        });
    }

    @Override
    public void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac, final onWifiConnectListener listener) {
        NetWorkWarpper.wifiConnect(wlanacname, wlanuserip, wlanacip, wlanusermac, apmac, new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object o) {
                listener.onWifiConnectSuccess();
            }

            @Override
            public void onFailure(ServerTip serverTip) {
                listener.onWifiConnectFaild();
            }
        });
    }
}
