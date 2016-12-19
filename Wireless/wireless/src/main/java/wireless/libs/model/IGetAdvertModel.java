package wireless.libs.model;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.StretchVo;

/**
 * Created by stephen on 2016/12/15.
 */

public interface IGetAdvertModel {

    interface onGetAdvertListener{
        void onGetAdvertSuccess(AdvertVo advertData);
    }

    interface onGetStretchListener{
        void onGetStretchSuccess(StretchVo stretchVo);
    }

    /***
     * 获取应用首页广告
     * @param listener
     */
    void getAdvert(onGetAdvertListener listener);

    /***
     * 获取首页活动模块
     * @param listener
     */
    void getStretch(onGetStretchListener listener);
}
