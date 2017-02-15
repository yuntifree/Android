package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLinePresenter extends IBasePresenter{
    void getHeadLine(int type, int seq);
    void onDestroy();
    void getLiveList(int seq);
}
