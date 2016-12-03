package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.io.Serializable;

public class UserInfoVo implements Serializable {
    private int errno;//错误码
    private String desc;//错误信息
    private UserData data;

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

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public class UserData implements Serializable {
        private int uid;//用户id
        private String token;
        private String privdata;//用于刷新token
        private int expire;// 过期时间(单位秒)

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPrivdata() {
            return privdata;
        }

        public void setPrivdata(String privdata) {
            this.privdata = privdata;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }
    }
}
