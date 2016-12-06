package wireless.libs.bean.vo;

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
        private List<MWifiInfo> wifipass;

        public List<MWifiInfo> getWifipass() {
            return wifipass;
        }

        public void setWifipass(List<MWifiInfo> wifipass) {
            this.wifipass = wifipass;
        }

        public class MWifiInfo implements Serializable{
            private String ssid;
            private String pass;

            public String getSsid() {
                return ssid;
            }

            public void setSsid(String ssid) {
                this.ssid = ssid;
            }

            public String getPass() {
                return pass;
            }

            public void setPass(String pass) {
                this.pass = pass;
            }
        }
    }
}
