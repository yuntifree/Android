package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.NickNameList;

/**
 * Created by stephen on 2017/2/25.
 */

public interface IDefHeadView extends IBaseView {
    void getDefHeadSuccess(DefHeadList defHeadList);
    void getRandNickSuccess(NickNameList nickNameList);
}
