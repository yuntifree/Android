package com.yunxingzh.wirelesslibs.wireless.lib.bean.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stephon on 2016/11/15.
 */

public class WifiVo implements Serializable{
    private int errno;//错误码
    private String desc;//错误信息
    private WifiData data;

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

    public WifiData getData() {
        return data;
    }

    public void setData(WifiData data) {
        this.data = data;
    }

    public class WifiData  implements Serializable{
        private List<MWifiInfo> infos;

        public List<MWifiInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<MWifiInfo> infos) {
            this.infos = infos;
        }

        public class MWifiInfo implements Serializable{
            private String ssid;
            private String username;
            private String password;
            private double longitude;
            private double latitude;

            public String getSsid() {
                return ssid;
            }

            public void setSsid(String ssid) {
                this.ssid = ssid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

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
        }
    }
}
