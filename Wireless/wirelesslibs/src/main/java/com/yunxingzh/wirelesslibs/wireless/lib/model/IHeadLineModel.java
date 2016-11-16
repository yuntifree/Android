package com.yunxingzh.wirelesslibs.wireless.lib.model;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineModel {

    interface onGetHeadLineListener{
        void onGetHeadLineSuccess(NewsVo newsVo);
        void onGetHeadLineFailed(int error);
        void onGetHeadLineFailed(String errorMsg);
    }

    interface onClickCountListener{
        void onClickCountSuccess();
        void onClickCountFailed(int error);
        void onClickCountFailed(String errorMsg);
    }

    interface onGetFontInfoListener{
        void onGetFontInfoSuccess(FontInfoVo fontInfoVo);
        void onGetFontInfoFailed(int error);
        void onGetFontInfoFailed(String errorMsg);
    }

    /***
     * 获取头条（新闻，视频，应用，游戏）内容
     * @param type 0-新闻 1-视频 2-应用 3-游戏
     * @param seq 序列号，分页拉取用
     * @param listener
     */
    void getHeadLine(int uid, String token, int term, double version, long ts,
                     int nettype,int type, int seq, onGetHeadLineListener listener);

    /***
     * 记录点击次数
     * @param listener
     */
    void clickCount(int uid, String token, int term, double version, long ts,
                     int nettype,int id,int type, onClickCountListener listener);

    /***
     * 拉下半页的信息
     * @param listener
     */
    void getFontInfo(int uid, String token, int term, double version, long ts,
                    int nettype,onGetFontInfoListener listener);
}
