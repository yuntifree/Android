package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by Stephen on 2016/9/10.
 */
public interface IRegisterPresenter {
    boolean inputValidate();
    void getValidateCode(int type,String phone);
    void register(String username,String password,int code);
}
