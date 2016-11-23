package com.yunxingzh.wireless.config;

/**
 * Created by stephon on 2016/11/20.
 */

public class EventBusType {

    private int mMsg;
    private int childMsg;
    private int itemIndex;

    public EventBusType(int msg) {
        mMsg = msg;
    }

    public EventBusType(int msg,int childMsg) {
        this.mMsg = msg;
        this.childMsg = childMsg;
    }

    public EventBusType(int msg,int childMsg,int itemIndex) {
        this.mMsg = msg;
        this.childMsg = childMsg;
        this.itemIndex = itemIndex;
    }

    public int getMsg(){
        return mMsg;
    }

    public int getChildMsg(){
        return childMsg;
    }

    public int getItemIndex() {
        return itemIndex;
    }
}
