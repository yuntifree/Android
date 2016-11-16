package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.util.List;

/**
 * Created by stephon on 2016/11/7.
 */

public class ServiceVo {
    private int errno;//错误码
    private String desc;//错误信息
    private DataVo data;

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

    public DataVo getData() {
        return data;
    }

    public void setData(DataVo data) {
        this.data = data;
    }

    public class DataVo{
        private List<ServiceData> services;

        public List<ServiceData> getServices() {
            return services;
        }

        public void setServices(List<ServiceData> services) {
            this.services = services;
        }

        public class ServiceData{
            private String title;
            private String icon;
            private List<ServiceChildData> items;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ServiceChildData> getItems() {
                return items;
            }

            public void setItems(List<ServiceChildData> items) {
                this.items = items;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public class ServiceChildData{
                private String title;
                private String dst;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
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
}
