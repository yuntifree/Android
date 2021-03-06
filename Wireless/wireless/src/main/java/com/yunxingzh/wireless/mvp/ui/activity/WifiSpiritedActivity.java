package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.ClearEditText;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IWifiSpiritedPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiSpiritedPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IWifiSpiritedView;
import com.yunxingzh.wireless.utils.LocationUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * Created by stephon on 2016/11/29.
 * wifi公益
 */

public class WifiSpiritedActivity extends BaseActivity implements View.OnClickListener, IWifiSpiritedView {

    public static final int SPIRITED_PAGER = 3;

    private TextView mTitleNameTv, mSpJoinBtn;
    private ClearEditText mSpSsidEt, mSpPwdEt;
    private ImageView mTitleReturnIv;
    private IWifiSpiritedPresenter iWifiSpiritedPresenter;
    private LocationUtils locationUtils;
    private String mSSID;
    private AlertView alertView;
    private Handler locationHandler = new LocationHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_spirited);
        initView();
        initData();
    }

    public void initView() {
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.INVISIBLE);
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mSpJoinBtn = findView(R.id.sp_join_btn);
        mSpJoinBtn.setOnClickListener(this);
        mSpSsidEt = findView(R.id.sp_ssid_et);
        mSpPwdEt = findView(R.id.sp_pwd_et);
    }

    public void initData() {
        StatusBarColor.compat(this,getResources().getColor(R.color.blue_009CFB));
        mSSID = getIntent().getStringExtra("ssid");
        if (!StringUtils.isEmpty(mSSID)) {
            mSpSsidEt.setText(mSSID);
            mSpPwdEt.setFocusable(true);
            mSpPwdEt.setFocusableInTouchMode(true);
            mSpPwdEt.requestFocus();
            mSpPwdEt.requestFocusFromTouch();
        }
        locationUtils = new LocationUtils(this, SPIRITED_PAGER);
        iWifiSpiritedPresenter = new WifiSpiritedPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            MobclickAgent.onEvent(this, "share_wifi_cancel");
            finish();
        } else if (mSpJoinBtn == v) {
            if (StringUtils.isEmpty(getSsid())) {
                ToastUtil.showMiddle(this, R.string.input_ssid);
                return;
            }
            locationUtils.startMonitor(locationHandler);
        }
    }

    private static class LocationHandler extends Handler {
        private final WeakReference<WifiSpiritedActivity> mActivity;

        public LocationHandler(WifiSpiritedActivity activity) {
            mActivity = new WeakReference<WifiSpiritedActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final WifiSpiritedActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 0) {
                    activity.iWifiSpiritedPresenter.wifiSpirited(activity.getSsid(), activity.getPwd(), activity.locationUtils.getBaseLocation().longitude, activity.locationUtils.getBaseLocation().latitude);
                } else if (msg.what == BDLocation.TypeServerError) {
                    ToastUtil.showMiddle(activity, R.string.location_error);
                    activity.alertView = new AlertView("温馨提示", "亲,定位失败,请打开定位权限", "取消", new String[]{"去设置"}, null, activity, AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position != AlertView.CANCELPOSITION) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                activity.startActivity(intent);
                            }
                        }
                    }).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            if (activity.alertView != null) {
                                activity.alertView.dismiss();
                            }
                        }
                    });
                    activity.alertView.show();
                } else {
                    LogUtils.i("lsd", "location error:" + msg.what);
                }
            }
        }
    };

    @Override
    public void wifiSpiritedSuccess() {
        ToastUtil.wifiSpiritedshow(this, R.string.spirited_join_success);
        mSpSsidEt.setText("");
        mSpPwdEt.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtils.stopMonitor();
        if (iWifiSpiritedPresenter != null) {
            iWifiSpiritedPresenter.onDestroy();
        }
        if (locationHandler != null) {
            locationHandler.removeCallbacksAndMessages(null);
        }
    }

    public String getSsid() {
        return mSpSsidEt.getText() + "";
    }

    public String getPwd() {
        return mSpPwdEt.getText() + "";
    }
}
