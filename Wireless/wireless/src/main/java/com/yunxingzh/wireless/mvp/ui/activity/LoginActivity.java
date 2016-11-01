package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.ILoginPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.LoginPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.NetWorkBaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.ILoginView;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by Stephen on 2016/9/8.
 */
public class LoginActivity extends NetWorkBaseActivity implements ILoginView, View.OnClickListener {

    private TextView mTitleNameTv, mRegisterTv,mLoginForgetPwdTv;
    private LinearLayout mTitleLeftLay;
    private EditText mLoPwdEt,mLoPhoneEt;
    private Button mLoLoginBtn;
    private ILoginPresenter iLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    public void initView(){
        mRegisterTv = findView(R.id.login_reigster_tv);
        mRegisterTv.setOnClickListener(this);
        mLoginForgetPwdTv = findView(R.id.login_forget_pwd_tv);
        mLoginForgetPwdTv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.login);
        mTitleLeftLay = findView(R.id.title_left_lay);
        mTitleLeftLay.setVisibility(View.VISIBLE);
        mTitleLeftLay.setOnClickListener(this);
        mLoPwdEt = findView(R.id.lo_pwd_et);
        mLoPhoneEt = findView(R.id.lo_phone_et);
        mLoLoginBtn = findView(R.id.lo_login_btn);
        mLoLoginBtn.setOnClickListener(this);
    }

    public void initData(){
        iLoginPresenter = new LoginPresenterImpl(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mRegisterTv){
           // startActivity(RegisterActivity.class);
        }else if(view == mTitleLeftLay){
            finish();
        }else if(view == mLoginForgetPwdTv){
           // startActivity(ForgetPwdActivity.class);
        }else if(view == mLoLoginBtn){
            iLoginPresenter.showLogin(getPhone(), StringUtils.getMD5(getPwd()));
        }
    }

    public void startActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    @Override
    public void loginSuccess() {
        ToastUtil.showMiddle(this, R.string.login_success);
        finish();
    }

    @Override
    public String getPhone() {
        return mLoPhoneEt.getText()+"";
    }

    @Override
    public String getPwd() {
        return mLoPwdEt.getText()+"";
    }

    @Override
    public void setPhone(int msgId) {
        ToastUtil.showMiddle(this,msgId);
    }

    @Override
    public void setPwd(int msgId) {
        ToastUtil.showMiddle(this,msgId);
    }

}
