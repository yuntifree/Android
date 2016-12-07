package wireless.libs.model;

/**
 * Created by stephon on 2016/11/27.
 */

public interface IConnectDGCountModel {
    interface onConnectDGCountListener{
        void onConnectDGCountSuccess();
    }

    /***
     * 上报连接东莞wifi次数
     * @param listener
     */
    void connectDGCount(String apmac, onConnectDGCountListener listener);
}
