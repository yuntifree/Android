package com.yunxingzh.wireless.mvp.presenter;

/**
 * Created by Stephen on 2016/9/10.
 */
public interface IRegisterPresenter extends IBasePresenter{
    boolean inputValidate();
    void getValidateCode(int type);
    void register(String username,String password);
}
