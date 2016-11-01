package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.ILoginPresenter;
import com.yunxingzh.wireless.mvp.view.ILoginView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IUserModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.UserModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by Stephen on 2016/9/10.
 */
public class LoginPresenterImpl implements ILoginPresenter,IUserModel.onLoginListener{

    private ILoginView iLoginView;
    private IUserModel iUserModel;

    public LoginPresenterImpl(ILoginView view) {
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


        if(StringUtils.isEmpty(iLoginView.getPwd())){
            iLoginView.setPwd(R.string.input_password);
            return false;
        }

        if(iLoginView.getPwd().length() < 6 || iLoginView.getPwd().length() > 16){
            iLoginView.setPwd(R.string.password_length_error);
            return false;
        }

        return true;
    }

    @Override
    public void showLogin(String phone, String password) {
        if (iLoginView != null && inputValidate()) {
            iLoginView.showProgress();
            iUserModel.login(phone,password, this);
        }
    }

    @Override
    public void onLoginSuccess(UserInfoVo userVo) {
        MyApplication.sApplication.setUser(userVo);
        MyApplication.sApplication.setToken(userVo.getToken());
        if (iLoginView != null) {
            iLoginView.hideProgress();
            iLoginView.loginSuccess();
        }
    }

    @Override
    public void onLoginFailed(int error) {
        if (iLoginView != null) {
            iLoginView.hideProgress();
            iLoginView.showError(error);
        }
    }

    @Override
    public void onLoginFailed(String errorMsg) {
        if (iLoginView != null) {
            iLoginView.hideProgress();
            iLoginView.showErrorMsg(errorMsg);
        }
    }
}
