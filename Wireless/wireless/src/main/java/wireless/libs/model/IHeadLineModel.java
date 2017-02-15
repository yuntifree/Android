package wireless.libs.model;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.LiveList;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineModel {

    interface onGetHeadLineListener{
        void onGetHeadLineSuccess(HotInfoList newsVo);
        void onGetHeadLineFaild();
    }

    interface onGetLiveListListener{
        void onGetLiveListSuccess(LiveList liveList);
        void onGetLiveListFaild();
    }

    /***
     * 获取头条（新闻，视频，应用，游戏）内容
     * @param type 0-新闻 1-视频 2-应用 3-游戏 4-本地 5-娱乐
     * @param seq 序列号，分页拉取用
     * @param listener
     */
    void getHeadLine(int type, int seq, onGetHeadLineListener listener);

    /***
     * 获取直播列表
     * @param listener
     */
    void getLiveList(int seq, onGetLiveListListener listener);

}
