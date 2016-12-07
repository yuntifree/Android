package wireless.libs.model;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.vo.FontInfoVo;
import wireless.libs.bean.vo.NewsVo;
import wireless.libs.network.HttpHandler;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineModel {

    interface onGetHeadLineListener{
        void onGetHeadLineSuccess(NewsVo newsVo);
        void onGetHeadLineFailed(int error);
    }

    interface onClickCountListener{
        void onClickCountSuccess();
        void onClickCountFailed(int error);
    }

    interface onGetFontInfoListener{
        void onGetFontInfoSuccess(FontInfoVo fontInfoVo);
        void onGetFontInfoFailed(int error);
    }

    /***
     * 获取头条（新闻，视频，应用，游戏）内容
     * @param type 0-新闻 1-视频 2-应用 3-游戏
     * @param seq 序列号，分页拉取用
     * @param listener
     */
    void getHeadLine(int uid, String token, int term, double version, long ts,
                     int nettype, int type, int seq, onGetHeadLineListener listener);

    /***
     * 记录点击次数
     * @param listener
     */
    void clickCount(int uid, String token, int term, double version, long ts,
                    int nettype, int id, int type, onClickCountListener listener);

    /***
     * 拉下半页的信息
     * @param listener
     */
    void getFontInfo(int uid, String token, int term, double version, long ts,
                     int nettype, onGetFontInfoListener listener);

}
