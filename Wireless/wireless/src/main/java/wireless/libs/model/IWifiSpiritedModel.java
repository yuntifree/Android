package wireless.libs.model;

/**
 * Created by stephon on 2016/11/29.
 */

public interface IWifiSpiritedModel {
    interface onWifiSpiritedListener{
        void onWifiSpiritedSuccess();
    }

    /***
     * wifi公益
     * @param
     * @param
     */
    void wifiSpirited(String ssid, String password, double longitude, double latitude, onWifiSpiritedListener listener);

}
