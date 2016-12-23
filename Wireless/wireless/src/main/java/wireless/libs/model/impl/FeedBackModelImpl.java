package wireless.libs.model.impl;

import wireless.libs.bean.resp.ServerTip;
import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.model.IFeedBackModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephen on 2016/12/23.
 */

public class FeedBackModelImpl implements IFeedBackModel {

    @Override
    public void feedBack(String content, final onFeedBackListener listener) {
        NetWorkWarpper.feedBack(content, new HttpHandler<Object>() {
            @Override
            public void onSuccess(ServerTip serverTip, Object resquestVo) {
                listener.onFeedBackSuccess();
            }
        });
    }
}
