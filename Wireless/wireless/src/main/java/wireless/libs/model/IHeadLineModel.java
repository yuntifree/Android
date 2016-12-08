package wireless.libs.model;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineModel {

    interface onGetHeadLineListener{
        void onGetHeadLineSuccess(HotInfoList newsVo);
    }

    interface onClickCountListener{
        void onClickCountSuccess();
    }

    interface onGetFontInfoListener{
        void onGetFontInfoSuccess(FontInfoList fontInfoVo);
    }

    /***
     * 获取头条（新闻，视频，应用，游戏）内容
     * @param type 0-新闻 1-视频 2-应用 3-游戏
     * @param seq 序列号，分页拉取用
     * @param listener
     */
    void getHeadLine(int type, int seq, onGetHeadLineListener listener);

    /***
     * 记录点击次数
     * @param listener
     */
    void clickCount(int id, int type, onClickCountListener listener);

    /***
     * 拉下半页的信息
     * @param listener
     */
    void getFontInfo(onGetFontInfoListener listener);

    void onDestroy();
}
