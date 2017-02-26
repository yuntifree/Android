package wireless.libs.model.impl;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.JokeList;
import wireless.libs.bean.resp.LiveList;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.model.IHeadLineModel;
import wireless.libs.network.HttpHandler;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon on 2016/11/3.
 */

public class HeadLineModelImpl implements IHeadLineModel {

    @Override
    public void getHeadLine(int type, int seq, final onGetHeadLineListener listener) {
        NetWorkWarpper.hot(type,seq,new HttpHandler<HotInfoList>() {
            @Override
            public void onSuccess(ServerTip serverTip, HotInfoList requestVo) {
                listener.onGetHeadLineSuccess(requestVo);
            }

            @Override
            public void onFailure(ServerTip serverTip) {
                listener.onGetHeadLineFaild();
            }
        });
    }

    @Override
    public void getLiveList(int seq, final onGetLiveListListener listener) {
        NetWorkWarpper.getLiveList(seq, new HttpHandler<LiveList>() {
            @Override
            public void onSuccess(ServerTip serverTip, LiveList requestVo) {
                listener.onGetLiveListSuccess(requestVo);
            }

            @Override
            public void onFailure(ServerTip serverTip) {
                listener.onGetLiveListFaild();
            }
        });
    }

    @Override
    public void getJokes(int seq, final onGetJokesListener listener) {
        NetWorkWarpper.getJokes(seq, new HttpHandler<JokeList>() {
            @Override
            public void onSuccess(ServerTip serverTip, JokeList requestVo) {
                listener.onGetJokesSuccess(requestVo);
            }

            @Override
            public void onFailure(ServerTip serverTip) {
                listener.onGetJokesFaild();
            }
        });
    }
}
