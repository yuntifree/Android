package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IRegisterPresenter;
import com.yunxingzh.wireless.mvp.view.IRegisterView;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.User;
import wireless.libs.model.IUserModel;
import wireless.libs.model.impl.UserModelImpl;

/**
 * Created by Stephen on 2016/9/10.
 */
public class RegisterPresenterImpl implements IRegisterPresenter,IUserModel.onValidateCodeListener,IUserModel.onRegisterListener{

    private IRegisterView iRegisterView;
    private IUserModel iUserModel;

    public RegisterPresenterImpl(IRegisterView view) {
        iRegisterView = view;
        iUserModel = new UserModelImpl();
    }

    @Override
    public boolean inputValidate() {

        if(StringUtils.isEmpty(iRegisterView.getPhone())){
            iRegisterView.setPhone(R.string.enter_phone);
            return false;
        }

        if(!StringUtils.validatePhoneNumber(iRegisterView.getPhone())){
            iRegisterView.setPhone(R.string.enter_right_phone);
            return false;
        }

        return true;
    }

    @Override
    public void getValidateCode(String phone) {
        if (iRegisterView != null && inputValidate()) {
            iUserModel.getValidateCode(phone,this);
        }
    }

    @Override
    public void register(String username, String code) {
        if (iRegisterView != null){
            iUserModel.register(username,code,this);
        }
    }

    @Override
    public void onValidateCodeSuccess() {
        if (iRegisterView != null){
            iRegisterView.getValidateCodeSuccess();
        }
    }

    @Override
    public void onRegisterSuccess(User userVo) {
        MainApplication.get().setUser(userVo);
        MainApplication.get().setToken(userVo.token);
        MainApplication.get().setPrivdata(userVo.privdata);
        MainApplication.get().setExpire(userVo.expiretime);
        MainApplication.get().setUserName(iRegisterView.getPhone());
        MainApplication.get().setWifiPwd(iRegisterView.getCode());
        if (iRegisterView != null){
            iRegisterView.registerSuccess();
        }
    }

    @Override
    public void onDestroy() {
        iRegisterView = null;
    }
}
