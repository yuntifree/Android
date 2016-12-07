package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.IWifiSpiritedPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiSpiritedPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IWifiSpiritedView;
import com.yunxingzh.wireless.utils.LocationUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

/**
 * Created by stephon on 2016/11/29.
 * wifi公益
 */

public class WifiSpiritedActivity extends BaseActivity implements View.OnClickListener, IWifiSpiritedView {

    private TextView mTitleNameTv, mSpJoinBtn;
    private EditText mSpSsidEt, mSpPwdEt;
    private ImageView mTitleReturnIv;
    private IWifiSpiritedPresenter iWifiSpiritedPresenter;
    private LocationUtils locationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_spirited);
        initView();
        initData();
    }

    public void initView() {
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.wifi_spirited);
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mSpJoinBtn = findView(R.id.sp_join_btn);
        mSpJoinBtn.setOnClickListener(this);
        mSpSsidEt = findView(R.id.sp_ssid_et);
        mSpPwdEt = findView(R.id.sp_pwd_et);
    }

    public void initData() {
        locationUtils = LocationUtils.getInstance(this);
        iWifiSpiritedPresenter = new WifiSpiritedPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mSpJoinBtn == v) {
            if (StringUtils.isEmpty(getSsid())) {
                ToastUtil.showMiddle(this, R.string.input_ssid);
                return;
            }
            locationUtils.startMonitor(locationHandler);
        }
    }


    final Handler locationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            iWifiSpiritedPresenter.wifiSpirited(locationUtils.getBaseLocation().longitude,locationUtils.getBaseLocation().latitude,getSsid(),getPwd());
        }
        }
    };

    @Override
    public void wifiSpiritedSuccess() {
        ToastUtil.showMiddle(this, R.string.spirited_join_success);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtils.stopMonitor();
    }

    public String getSsid() {
        return mSpSsidEt.getText() + "";
    }

    public String getPwd() {
        return mSpPwdEt.getText() + "";
    }
}
