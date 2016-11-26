package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IRegisterPresenter;
import com.yunxingzh.wireless.mvp.view.IRegisterView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.model.IUserModel;
import com.yunxingzh.wirelesslibs.wireless.lib.model.impl.UserModelImpl;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

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
    public void getValidateCode(int type,String phone) {
        if (iLoginView != null && inputValidate()) {
            iLoginView.showProgress();
            iUserModel.getValidateCode(0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),
                    type,phone, this);
        }
    }

    @Override
    public void register(String username, String password, int code) {
        if (iLoginView != null){
            iLoginView.showProgress();
            iUserModel.register(0,Double.parseDouble(AppUtils.getVersionName(MyApplication.sApplication)),
                    StringUtils.getCurrentTime(),AppUtils.getNetWorkType(MyApplication.sApplication),username,password,code,
                    AppUtils.getPhoneModel(),AppUtils.getChannelName(MyApplication.sApplication) == null ? "" : AppUtils.getChannelName(MyApplication.sApplication),
                    MyApplication.sApplication.getMark(),this);
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
        MyApplication.sApplication.setUser(userVo);
        MyApplication.sApplication.setToken(userVo.getData().getToken());
        MyApplication.sApplication.setWifiPwd(userVo.getData().getWifipass());
        MyApplication.sApplication.setUserName(iLoginView.getPhone());
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
