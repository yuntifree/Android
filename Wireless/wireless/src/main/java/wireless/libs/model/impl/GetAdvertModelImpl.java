package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.model.IGetAdvertModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2016/12/15.
 */

public class GetAdvertModelImpl implements IGetAdvertModel{
    @Override
    public void getAdvert(final onGetAdvertListener listener) {
        NetWorkWarpper.getAdvert(new HttpHandler<AdvertVo>() {
            @Override
            public void onSuccess(ServerTip serverTip, AdvertVo resquestVo) {
                listener.onGetAdvertSuccess(resquestVo);
            }
        });
    }
}
