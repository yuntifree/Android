package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IConnectDGCountModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/27.
 */

public class ConnectDGCountModelImpl implements IConnectDGCountModel{

    @Override
    public void connectDGCount(String apmac,final onConnectDGCountListener listener) {
        NetWorkWarpper.connectDGCount(apmac,new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object resquestVo) {
                listener.onConnectDGCountSuccess();
            }
        });
    }
}