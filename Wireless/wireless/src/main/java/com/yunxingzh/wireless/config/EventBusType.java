package com.yunxingzh.wireless.config;

/**
 * Created by stephon on 2016/11/20.
 */

public class EventBusType {

    private int mMsg;
    private int childMsg;

    public EventBusType(int msg) {
        mMsg = msg;
    }

    public EventBusType(int msg,int childMsg) {
        this.mMsg = msg;
        this.childMsg = childMsg;
    }

    public int getMsg(){
        return mMsg;
    }

    public int getChildMsg(){
        return childMsg;
    }

}
