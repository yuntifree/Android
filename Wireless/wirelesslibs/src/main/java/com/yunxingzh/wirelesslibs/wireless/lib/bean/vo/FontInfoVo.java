package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.util.List;

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
        private UserVo user;
        private List<BannersVo> banner;

        public UserVo getUser() {
            return user;
        }

        public void setUser(UserVo user) {
            this.user = user;
        }

        public List<BannersVo> getBanner() {
            return banner;
        }

        public void setBanner(List<BannersVo> banner) {
            this.banner = banner;
        }

        public class UserVo{
            private int total;
            private int save;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getSave() {
                return save;
            }

            public void setSave(int save) {
                this.save = save;
            }
        }

        public class BannersVo{
            private String img;
            private String dst;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getDst() {
                return dst;
            }

            public void setDst(String dst) {
                this.dst = dst;
            }
        }
    }
}
