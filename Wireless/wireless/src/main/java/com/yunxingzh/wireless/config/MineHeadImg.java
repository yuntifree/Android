package com.yunxingzh.wireless.config;

import wireless.libs.bean.vo.UserInfoVo;

/**
 * Created by stephen on 2017/2/27.
 */

public class MineHeadImg {
    private int mFlag;
    private String mMsg;
    private UserInfoVo userInfoVo;

    public MineHeadImg(int mFlag, String mMsg) {
        this.mFlag = mFlag;
        this.mMsg = mMsg;
    }

    public MineHeadImg(int mFlag, String mMsg, UserInfoVo userInfoVo) {
        this.mFlag = mFlag;
        this.mMsg = mMsg;
        this.userInfoVo = userInfoVo;
    }

    public int getmFlag() {
        return mFlag;
    }

    public String getmMsg() {
        return mMsg;
    }

    public UserInfoVo getUserInfoVo() {
        return userInfoVo;
    }
}
