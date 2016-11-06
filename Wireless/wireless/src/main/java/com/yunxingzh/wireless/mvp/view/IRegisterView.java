package com.yunxingzh.wireless.mvp.view;

/**
 * Created by Stephen on 2016/9/10.
 */
public interface IRegisterView extends IBaseView {
    String getPhone();
    void setPhone(int msgId);
    String getCode();
    void setCode(int msgId);

    void getValidateCodeSuccess();
    void registerSuccess();
}
