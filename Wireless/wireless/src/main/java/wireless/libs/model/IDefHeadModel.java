package wireless.libs.model;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.NickNameList;

/**
 * Created by stephen on 2017/2/25.
 */

public interface IDefHeadModel {

    interface onGetDefHeadListener{
        void onGetDefHeadSuccess(DefHeadList defHeadList);
    }

    interface onGetRandNickListener{
        void onGetRandNickSuccess(NickNameList nickNameList);
    }

    /***
     * 获取用户默认头像
     * @param listener
     */
    void getDefHead(onGetDefHeadListener listener);

    /***
     * 获取随机用户名
     * @param listener
     */
    void getRandNick(onGetRandNickListener listener);
}
