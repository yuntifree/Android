package wireless.libs.model;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.UpdateVo;

/**
 * Created by stephen on 2016/12/15.
 */

public interface IGetAdvertModel {

    interface onGetAdvertListener{
        void onGetAdvertSuccess(AdvertVo advertData);
    }

    interface onCheckUpdateListener{
        void onCheckUpdateSuccess(UpdateVo updateVo);
    }

    /***
     * 获取应用首页广告
     * @param listener
     */
    void getAdvert(onGetAdvertListener listener);

    /***
     * 检查更新
     * @param listener
     */
    void checkUpdate(String channel, onCheckUpdateListener listener);
}
