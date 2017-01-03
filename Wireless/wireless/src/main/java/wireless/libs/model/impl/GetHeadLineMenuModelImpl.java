package wireless.libs.model.impl;

import wireless.libs.bean.resp.MenuList;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IGetHeadLineMenuModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2016/12/30.
 */

public class GetHeadLineMenuModelImpl implements IGetHeadLineMenuModel {

    @Override
    public void getMenu(final onGetHeadLineMenuListener listener) {
        NetWorkWarpper.getMenu(new HttpHandler<MenuList>() {
            @Override
            public void onSuccess(ServerTip serverTip, MenuList requestVo) {
                listener.onGetHeadLineMenuSuccess(requestVo);
            }
        });
    }
}
