package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stephon on 2016/11/18.
 */

public class WifiMapVo implements Serializable{
    private int errno;//错误码
    private String desc;//错误信息
    private WifiMapData data;

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

    public WifiMapData getData() {
        return data;
    }

    public void setData(WifiMapData data) {
        this.data = data;
    }

    public class WifiMapData implements Serializable{
        private List<WifiMapInfo> infos;

        public List<WifiMapInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<WifiMapInfo> infos) {
            this.infos = infos;
        }

        public class WifiMapInfo implements Serializable{
            private double longitude;
            private double latitude;
            private String address;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
