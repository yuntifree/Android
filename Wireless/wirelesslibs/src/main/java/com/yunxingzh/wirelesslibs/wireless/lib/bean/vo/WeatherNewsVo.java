package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.util.List;

/**
 * Created by stephon on 2016/11/15.
 */

public class WeatherNewsVo {
    private int errno;//错误码
    private String desc;//错误信息
    private WeatherNewsData data;

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

    public WeatherNewsData getData() {
        return data;
    }

    public void setData(WeatherNewsData data) {
        this.data = data;
    }

    public class WeatherNewsData{
        private WeatherVo weather;
        private List<mainNewsVo> news;

        public WeatherVo getWeather() {
            return weather;
        }

        public void setWeather(WeatherVo weather) {
            this.weather = weather;
        }

        public List<mainNewsVo> getNews() {
            return news;
        }

        public void setNews(List<mainNewsVo> news) {
            this.news = news;
        }

        public class WeatherVo{
            private int temp;
            private int type;
            private String info;

            public int getTemp() {
                return temp;
            }

            public void setTemp(int temp) {
                this.temp = temp;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public class mainNewsVo{
            private int id;
            private String title;
            private List<String> images;
            private String source;
            private String ctime;
            private String dst;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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
        }
    }
}
