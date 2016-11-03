package com.yunxingzh.wireless.mvp.view;

import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

/**
 * Created by stephon on 2016/11/3.
 */

public interface IHeadLineView extends IBaseView{
    void getHeadLineSuccess(NewsVo newsVo);
}
