package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import java.util.List;

/**
 * Created by stephon_ on 2016/11/3.
 */

public class NewsVo {
    private int errno;//错误码
    private String desc;//错误信息
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private List<NewsData> infos;
        private int hasmore;//是否有更多

        public List<NewsData> getInfos() {
            return infos;
        }

        public void setInfos(List<NewsData> infos) {
            this.infos = infos;
        }

        public int getHasmore() {
            return hasmore;
        }

        public void setHasmore(int hasmore) {
            this.hasmore = hasmore;
        }

        public class NewsData{
            /**
             * 新闻视频
             */
            private int id;
            private int seq;//分页用
            private String title;//应用名或新闻title
            private List<String> images;//图片url
            private String source;//新闻来源
            private String ctime;//时间
            private String dst; //源地址或应用下载地址
            private int stype;//0-新闻 1-视频 2-应用 3-游戏
            private int play;//播放次数
            /**
             * 应用游戏
             */
            private String icon;
            private String install;

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getDst() {
                return dst;
            }

            public void setDst(String dst) {
                this.dst = dst;
            }

            public int getStype() {
                return stype;
            }

            public void setStype(int stype) {
                this.stype = stype;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getInstall() {
                return install;
            }

            public void setInstall(String install) {
                this.install = install;
            }

            public int getPlay() {
                return play;
            }

            public void setPlay(int play) {
                this.play = play;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
