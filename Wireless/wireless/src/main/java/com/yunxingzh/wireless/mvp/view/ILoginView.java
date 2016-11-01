package com.yunxingzh.wireless.mvp.view;

/**
 * Created by Stephen on 2016/9/10.
 */
public interface ILoginView extends IBaseView {
    String getPhone();
    String getPwd();
    void setPhone(int msgId);
    void setPwd(int msgId);

    void loginSuccess();
}
