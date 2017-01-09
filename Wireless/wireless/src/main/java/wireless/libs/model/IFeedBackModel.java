package wireless.libs.model;

import wireless.libs.bean.vo.AutoLoginVo;

/**
 * Created by stephen on 2016/12/23.
 */

public interface IFeedBackModel {

    interface onFeedBackListener{
        void onFeedBackSuccess();
    }

    interface onAutoLoginListener{
        void onAutoLoginSuccess(AutoLoginVo autoLoginVo);
    }

    /***
     * 用户反馈
     * @param listener
     */
    void feedBack(String content, String contact, onFeedBackListener listener);

    /***
     * 自动登录
     * @param listener
     */
    void autoLogin(String privdata, onAutoLoginListener listener);
}
