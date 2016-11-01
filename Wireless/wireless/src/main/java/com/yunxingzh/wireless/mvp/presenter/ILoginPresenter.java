package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by Stephen on 2016/9/10.
 */
public interface ILoginPresenter {
    boolean inputValidate();
    void showLogin(String phone, String password);
}
