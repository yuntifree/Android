package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dgwx.app.lib.bl.WifiInterface;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IRegisterPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.RegisterPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IRegisterView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.json.JSONObject;

/**
 * Created by Stephen on 2016/9/8.
 */
public class RegisterActivity extends BaseActivity implements IRegisterView, View.OnClickListener,TextWatcher {

    private final static int TYPE = 0;//验证类型 0-(注册)
    private final static int MILLISECOND = 1000;//毫秒
    private final static int SECOND = 60;//秒
    private final static int TIME = SECOND * MILLISECOND;

    private EditText mValidateCodeEt, mLoPhoneEt;
    private Button mLoRegisterBtn;
    private TextView mGetValidateCodeBtn,mAgreeContent;
    private IRegisterPresenter iLoginPresenter;
    private TimeCount mTimeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    public void initView() {
        mValidateCodeEt = findView(R.id.validate_code_et);
        mValidateCodeEt.addTextChangedListener(this);
        mLoPhoneEt = findView(R.id.lo_phone_et);
        mGetValidateCodeBtn = findView(R.id.get_validate_code_btn);
        mGetValidateCodeBtn.setOnClickListener(this);
        mLoRegisterBtn = findView(R.id.lo_login_btn);
        mLoRegisterBtn.setOnClickListener(this);
        mAgreeContent = findView(R.id.agree_content);
        mAgreeContent.setOnClickListener(this);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.transparent));
        iLoginPresenter = new RegisterPresenterImpl(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoRegisterBtn) {
            String code = getCode();
            if (StringUtils.validatePhoneNumber(getPhone())) {
                if (code.length() > 0 && !StringUtils.isEmpty(code)) {
                    iLoginPresenter.register(getPhone(), code);
                } else {
                    ToastUtil.showMiddle(RegisterActivity.this, R.string.final_validate_code);
                }
            } else {
                ToastUtil.showMiddle(this, R.string.enter_right_phone);
            }
        } else if (view == mGetValidateCodeBtn) {
            if (StringUtils.validatePhoneNumber(getPhone())) {
                if (NetUtils.isNetworkAvailable(this)) {
                    mGetValidateCodeBtn.setText("正在获取");
                    mLoPhoneEt.setEnabled(false);
                    iLoginPresenter.getValidateCode(getPhone());
                } else {
                    ToastUtil.showMiddle(this, R.string.net_error);
                }
            } else {
                ToastUtil.showMiddle(this, R.string.enter_right_phone);
            }
        } else if (view == mAgreeContent){
            startActivity(WebViewActivity.class,"用户协议", Constants.URL_AGREEMENT);
        }
    }

    @Override
    public void getValidateCodeSuccess() {
        mTimeCount = new TimeCount(TIME, MILLISECOND);
        mTimeCount.start();
        mGetValidateCodeBtn.setText(SECOND + getString(R.string.second));
        ToastUtil.showMiddle(this, R.string.get_validate_code_success);
    }

    @Override
    public void registerSuccess() {
        //ToastUtil.showMiddle(this, R.string.register_success);
        startActivity(MainActivity.class,"","");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iLoginPresenter != null) {
            iLoginPresenter.onDestroy();
        }
    }

    public void startActivity(Class activity, String title, String url) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(Constants.TITLE,title);
        intent.putExtra(Constants.URL,url);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mLoRegisterBtn.setTextColor(Color.parseColor("#ffffff"));
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            //mGetValidateCodeBtn.setBackgroundResource(R.drawable.style_login_btn);
            mGetValidateCodeBtn.setText(R.string.re_get_verification_code);
            mGetValidateCodeBtn.setEnabled(true);
            mGetValidateCodeBtn.setPadding(0,0,0,0);
            mLoPhoneEt.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mGetValidateCodeBtn.setEnabled(false);
          //  mGetValidateCodeBtn.setBackgroundResource(R.drawable.validate_code_style);
            mGetValidateCodeBtn.setPadding(40,0,0,0);
            mGetValidateCodeBtn.setText(millisUntilFinished / MILLISECOND + getString(R.string.second));
        }
    }

    @Override
    public String getPhone() {
        String strText = mLoPhoneEt.getText() + "";
        return strText.trim();
    }

    @Override
    public void setPhone(int msgId) {
        ToastUtil.showMiddle(this, msgId);
    }

    @Override
    public String getCode() {
        String code = mValidateCodeEt.getText() + "";
        return code.trim();
    }

    @Override
    public void setCode(int msgId) {
        ToastUtil.showMiddle(this, msgId);
    }
}
