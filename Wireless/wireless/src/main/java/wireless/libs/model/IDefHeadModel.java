package wireless.libs.model;

import wireless.libs.bean.resp.DefHeadList;

/**
 * Created by stephen on 2017/2/25.
 */

public interface IDefHeadModel {

    interface onGetDefHeadListener{
        void onGetDefHeadSuccess(DefHeadList defHeadList);
    }

    /***
     * 获取用户默认头像
     * @param listener
     */
    void getDefHead(onGetDefHeadListener listener);
}
