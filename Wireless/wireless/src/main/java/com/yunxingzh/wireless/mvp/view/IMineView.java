package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.vo.ImageUploadVo;
import wireless.libs.bean.vo.UserInfoVo;

/**
 * Created by stephen on 2017/2/24.
 */

public interface IMineView extends IBaseView {
    void applyImageUploadSuccess(ImageUploadVo imageUploadVo);
    void getUserInfoSuccess(UserInfoVo userInfoVo);
}
