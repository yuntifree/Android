package wireless.libs.model.impl;

import wireless.libs.bean.resp.FindList;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IServiceModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServiceModelImpl implements IServiceModel{

    @Override
    public void getFind(final onGetFindListener listListener) {
        NetWorkWarpper.getFind(new HttpHandler<FindList>() {
            @Override
            public void onSuccess(ServerTip serverTip, FindList findList) {
                listListener.onGetFindSuccess(findList);
            }
        });
    }
}
