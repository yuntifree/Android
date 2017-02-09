package wireless.libs.model;

import wireless.libs.bean.resp.FontInfoList;

/**
 * Created by stephen on 2017/2/9.
 */

public interface IWirelessModel {
    interface onClickCountListener{
        void onClickCountSuccess();
    }

    interface onGetFontInfoListener{
        void onGetFontInfoSuccess(FontInfoList fontInfoVo);
    }

    interface onWifiConnectListener{
        void onWifiConnectSuccess();
        void onWifiConnectFaild();
    }

    /***
     * 记录点击次数
     * @param listener
     */
    void clickCount(int id, int type, onClickCountListener listener);

    /***
     * 拉下半页的信息
     * @param listener
     */
    void getFontInfo(onGetFontInfoListener listener);

    /***
     * 连接东莞wifi
     * @param listener
     */
    void wifiConnect(String wlanacname, String wlanuserip, String wlanacip, String wlanusermac, String apmac, onWifiConnectListener listener);

}
