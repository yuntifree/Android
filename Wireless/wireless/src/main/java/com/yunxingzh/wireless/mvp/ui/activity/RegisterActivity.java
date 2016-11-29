package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dgwx.app.lib.bl.WifiInterface;
import com.dgwx.app.lib.common.util.SettingUtility;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IRegisterPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.RegisterPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.NetWorkBaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IRegisterView;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AppUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import org.json.JSONObject;

import static com.yunxingzh.wireless.R.string.phone;

/**
 * Created by Stephen on 2016/9/8.
 */
public class RegisterActivity extends NetWorkBaseActivity implements IRegisterView, View.OnClickListener,TextWatcher {

    private final static int TYPE = 0;//验证类型 0-(注册)
    private final static int MILLISECOND = 1000;//毫秒
    private final static int SECOND = 60;//秒
    private final static int TIME = SECOND * MILLISECOND;

    private EditText mValidateCodeEt, mLoPhoneEt;
    private Button mLoRegisterBtn;
    private TextView mGetValidateCodeBtn;
    private IRegisterPresenter iLoginPresenter;
    private TimeCount mTimeCount;
    private String mPhone;
    private String mCode;

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
    }

    public void initData() {
        iLoginPresenter = new RegisterPresenterImpl(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoRegisterBtn) {
            String code = getCode();
            if (StringUtils.isEmpty(code)){
                setCode(R.string.input_code);
            } else if (mCode.equals(code)){
                iLoginPresenter.register(mPhone, StringUtils.getMD5(mCode),Integer.parseInt(mCode));
            } else {
                ToastUtil.showMiddle(RegisterActivity.this, "请输入正确的验证码");
            }
        } else if (view == mGetValidateCodeBtn){
            WifiInterface.wifiRegister(handler, getPhone(), "", 5000);
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        String obj = (String) msg.obj;
        int what = msg.what;
        String reason = " ";
        String pwd = "";
        try {
            JSONObject resp = new JSONObject(obj);
            reason = resp.getJSONObject("head").getString("reason");
            pwd = resp.getJSONObject("body").getString("pwd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (what) {
            case 0:
                SPUtils.put(MyApplication.getInstance(), Constants.SP_USER_NAME, getPhone());
                mPhone = getPhone();
                SPUtils.put(MyApplication.getInstance(), Constants.SP_WIFI_PWD, pwd);
                mCode = pwd;
                break;
            default:
                ToastUtil.showMiddle(RegisterActivity.this, reason);
                break;
        }
        super.handleMessage(msg);
        }
    };

    @Override
    public void getValidateCodeSuccess() {
        mTimeCount = new TimeCount(TIME, MILLISECOND);
        mTimeCount.start();
        mGetValidateCodeBtn.setText(SECOND + getString(R.string.second));
        ToastUtil.showMiddle(this, R.string.get_validate_code_success);
    }

    @Override
    public void registerSuccess() {
        ToastUtil.showMiddle(this, R.string.register_success);
        startActivity(MainActivity.class);
        finish();
    }

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
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
            mGetValidateCodeBtn.setBackgroundResource(R.drawable.style_login_btn);
            mGetValidateCodeBtn.setText(R.string.re_get_verification_code);
            mGetValidateCodeBtn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mGetValidateCodeBtn.setEnabled(false);
            mGetValidateCodeBtn.setBackgroundResource(R.drawable.validate_code_style);
            mGetValidateCodeBtn.setText(millisUntilFinished / MILLISECOND + getString(R.string.second));
        }
    }

    @Override
    public String getPhone() {
        return mLoPhoneEt.getText() + "";
    }

    @Override
    public void setPhone(int msgId) {
        ToastUtil.showMiddle(this, msgId);
    }

    @Override
    public String getCode() {
        return mValidateCodeEt.getText() + "";
    }

    @Override
    public void setCode(int msgId) {
        ToastUtil.showMiddle(this, msgId);
    }

}
