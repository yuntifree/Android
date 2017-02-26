package wireless.libs.model.impl;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IDefHeadModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2017/2/25.
 */

public class DefHeadModelImpl implements IDefHeadModel {

    @Override
    public void getDefHead(final onGetDefHeadListener listener) {
            NetWorkWarpper.getDefHead(new HttpHandler<DefHeadList>() {
                @Override
                public void onSuccess(ServerTip serverTip, DefHeadList o) {
                    listener.onGetDefHeadSuccess(o);
                }
        });
    }
}
