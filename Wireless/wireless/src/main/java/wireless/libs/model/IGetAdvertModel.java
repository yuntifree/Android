package wireless.libs.model;

import wireless.libs.bean.vo.AdvertVo;

/**
 * Created by stephen on 2016/12/15.
 */

public interface IGetAdvertModel {

    interface onGetAdvertListener{
        void onGetAdvertSuccess(AdvertVo advertData);
    }

    /***
     * 获取应用首页广告
     * @param listener
     */
    void getAdvert(onGetAdvertListener listener);
}
