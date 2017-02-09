package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.LiveList;

/**
 * Created by stephen on 2017/2/8.
 */

public interface IGetLiveListView extends IBaseView {
    void getLiveListSuccess(LiveList liveList);
    void getLiveListFaild();
}
