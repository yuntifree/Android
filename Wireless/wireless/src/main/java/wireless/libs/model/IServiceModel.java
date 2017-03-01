package wireless.libs.model;

import wireless.libs.bean.resp.FindList;

/**
 * Created by stephon on 2016/11/7.
 */

public interface IServiceModel {

    interface onGetFindListener{
        void onGetFindSuccess(FindList findList);
    }

    /***
     * 发现
     * @param
     * @param
     */
    void getFind(onGetFindListener listListener);
}
