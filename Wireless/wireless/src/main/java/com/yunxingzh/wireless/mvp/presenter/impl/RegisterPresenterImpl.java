package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.presenter.IRegisterPresenter;
import com.yunxingzh.wireless.mvp.view.IRegisterView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.model.IUserModel;
import wireless.libs.model.impl.UserModelImpl;

/**
 * Created by Stephen on 2016/9/10.
 */
public class RegisterPresenterImpl implements IRegisterPresenter,IUserModel.onValidateCodeListener,IUserModel.onRegisterListener{

    private IRegisterView iLoginView;
    private IUserModel iUserModel;

    public RegisterPresenterImpl(IRegisterView view) {
        iLoginView = view;
        iUserModel = new UserModelImpl();
    }

    @Override
    public boolean inputValidate() {

        if(StringUtils.isEmpty(iLoginView.getPhone())){
            iLoginView.setPhone(R.string.enter_phone);
            return false;
        }

        if(!StringUtils.validatePhoneNumber(iLoginView.getPhone())){
            iLoginView.setPhone(R.string.enter_right_phone);
            return false;
        }

        return true;
    }

    @Override
    public void getValidateCode(int type) {
        if (iLoginView != null && inputValidate()) {
            iLoginView.showProgress();
            iUserModel.getValidateCode(0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(), AppUtils.getNetWorkType(MainApplication.sApplication),
                    type, this);
        }
    }

    @Override
    public void register(String username, String password) {
        if (iLoginView != null){
            iLoginView.showProgress();
            iUserModel.register(0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MainApplication.sApplication),username,password,
                    AppUtils.getPhoneModel(),AppUtils.getChannelName(MainApplication.sApplication) == null ? "" : AppUtils.getChannelName(MainApplication.sApplication),
                    MainApplication.sApplication.getMark(),this);
        }
    }

    @Override
    public void onValidateCodeSuccess() {
        if (iLoginView != null){
            iLoginView.hideProgress();
            iLoginView.getValidateCodeSuccess();
        }
    }

    @Override
    public void onValidateCodeFailed(int error) {
        if (iLoginView != null){
            iLoginView.hideProgress();
            iLoginView.showError(error);
        }
    }

    @Override
    public void onRegisterSuccess(UserInfoVo userVo) {
        MainApplication.sApplication.setUser(userVo);
        MainApplication.sApplication.setToken(userVo.getData().getToken());
        MainApplication.sApplication.setUserName(iLoginView.getPhone());
        MainApplication.sApplication.setWifiPwd(iLoginView.getCode());
        if (iLoginView != null){
            iLoginView.hideProgress();
            iLoginView.registerSuccess();
        }
    }

    @Override
    public void onRegisterFailed(int error) {
        if (iLoginView != null){
            iLoginView.hideProgress();
            iLoginView.showError(error);
        }
    }
}
