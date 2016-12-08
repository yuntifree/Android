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
          //  iLoginView.showProgress();
//            iUserModel.getValidateCode(0,Double.parseDouble(AppUtils.getVersionName(MainApplication.sApplication)),
//                    StringUtils.getCurrentTime(), AppUtils.getNetWorkType(MainApplication.sApplication),
//                    type, this);
        }
    }

    @Override
    public void register(String username, String code) {
        if (iLoginView != null){
            iUserModel.register(username,code,this);
        }
    }

    @Override
    public void onValidateCodeSuccess() {
        if (iLoginView != null){
            iLoginView.getValidateCodeSuccess();
        }
    }

    @Override
    public void onRegisterSuccess(User userVo) {
        MainApplication.sApplication.setUser(userVo);
        MainApplication.sApplication.setToken(userVo.token);
        MainApplication.sApplication.setUserName(iLoginView.getPhone());
        MainApplication.sApplication.setWifiPwd(iLoginView.getCode());
        if (iLoginView != null){
            iLoginView.registerSuccess();
        }
    }

    @Override
    public void onDestroy() {
        iLoginView = null;
    }
}
