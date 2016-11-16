package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

/**
 * Created by stephon on 2016/11/16.
 */

public class FontInfoVo {
    private int errno;//错误码
    private String desc;//错误信息
    private FontData data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FontData getData() {
        return data;
    }

    public void setData(FontData data) {
        this.data = data;
    }

    public class FontData{
        
    }
}
