package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLinePresenter extends IBasePresenter{
    void getHeadLine(int type, int seq);
    void clickCount(int id,int type);
    void weatherNews();
    void getFontInfo();
}
