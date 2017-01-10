package wireless.libs.model;

import wireless.libs.bean.vo.AutoLoginVo;

/**
 * Created by stephen on 2016/12/23.
 */

public interface IFeedBackModel {

    interface onFeedBackListener{
        void onFeedBackSuccess();
    }

    /***
     * 用户反馈
     * @param listener
     */
    void feedBack(String content, String contact, onFeedBackListener listener);
}
