package com.yunxingzh.wirelesslibs.wireless.lib.model;

/**
 * Created by stephon on 2016/11/27.
 */

public interface IConnectDGCountModel {
    interface onConnectDGCountListener{
        void onConnectDGCountSuccess();
        void onConnectDGCountFailed(int error);
    }

    /***
     * 上报连接东莞wifi次数
     * @param listener
     */
    void connectDGCount(int uid, String token, int term, double version, long ts,
                    int nettype,String apmac, onConnectDGCountListener listener);
}
