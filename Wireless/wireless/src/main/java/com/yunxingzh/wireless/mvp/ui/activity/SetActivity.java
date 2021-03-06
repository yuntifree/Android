package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.BuildConfig;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.CheckUpdateDialog;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mview.loading.ACProgressConstant;
import com.yunxingzh.wireless.mview.loading.ACProgressFlower;
import com.yunxingzh.wireless.mvp.presenter.IGetAdvertPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.GetAdvertPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IGetAdvertView;
import com.yunxingzh.wireless.utils.CacheCleanUtil;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.UpdateVo;

/**
 * Created by stephen on 2016/12/23.
 * 设置
 */

public class SetActivity extends BaseActivity implements View.OnClickListener, IGetAdvertView {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv, mSetCacheSizeTv, mSetUseTv, mSetVersionTv, mSetUpdateNumTv, mSwitchPhoneTv;
    private LinearLayout mSetCleanLay, mSetAboutLay, mSetUpdateLay;
    private ACProgressFlower acProgressPie;
    private IGetAdvertPresenter iGetAdvertPresenter;
    private CheckUpdateDialog checkUpdateDialog;
    private UpdateVo data;
    private AlertView alertView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.set);
        mSetVersionTv = findView(R.id.set_version_tv);
        mSetCacheSizeTv = findView(R.id.set_cache_size_tv);
        mSetCleanLay = findView(R.id.set_clean_lay);
        mSetCleanLay.setOnClickListener(this);
        mSetAboutLay = findView(R.id.set_about_lay);
        mSetAboutLay.setOnClickListener(this);
        mSetUseTv = findView(R.id.set_use_tv);
        mSetUseTv.setOnClickListener(this);
        mSetUpdateLay = findView(R.id.set_update_lay);
        mSetUpdateLay.setOnClickListener(this);
        mSetUpdateNumTv = findView(R.id.set_update_num_tv);
        mSwitchPhoneTv = findView(R.id.switch_phone_tv);
        mSwitchPhoneTv.setOnClickListener(this);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        iGetAdvertPresenter = new GetAdvertPresenterImpl(this);
        iGetAdvertPresenter.checkUpdate();
        mSetVersionTv.setText("v" + BuildConfig.VERSION_NAME);
        mSetCacheSizeTv.setText(CacheCleanUtil.getTotalCacheSize(this));
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            MobclickAgent.onEvent(this, "setting_cancel");
            finish();
        } else if (mSetCleanLay == v) {//清除缓存
            String cacheSize = CacheCleanUtil.getTotalCacheSize(this);
            if (cacheSize.equals("0KB")) {
                ToastUtil.showMiddle(this, "不需要清理~");
            } else {
                acProgressPie = new ACProgressFlower.Builder(this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .text("请稍候...")
                        .fadeColor(Color.DKGRAY).build();
                acProgressPie.show();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        acProgressPie.dismiss();
                        mSetCacheSizeTv.setText(CacheCleanUtil.getTotalCacheSize(SetActivity.this));
                        ToastUtil.showMiddle(SetActivity.this, "清理完成~");
                    }
                }, 2000);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CacheCleanUtil.clearAllCache(SetActivity.this);//清缓存
                    }
                }).start();
            }
        } else if (mSetAboutLay == v) {//关于我们
            startActivity(WebViewActivity.class, "关于我们", Constants.ABOUT_US);
        } else if (mSetUseTv == v) {//用户协议
            startActivity(WebViewActivity.class, "用户协议", Constants.URL_AGREEMENT);
        } else if (mSetUpdateLay == v) {//版本升级
            if (mSetUpdateNumTv.getVisibility() == View.VISIBLE && data != null) {
                checkUpdateDialog = new CheckUpdateDialog(this, data);
                checkUpdateDialog.show();
            } else {
                ToastUtil.showMiddle(this, "当前已是最新版本");
            }
        } else if (mSwitchPhoneTv == v) {//切换号码
            alertView = new AlertView("温馨提示", "确定退出当前帐号？", "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position != AlertView.CANCELPOSITION) {
                        MainApplication.get().loginOut();
                        Constants.FRAGMENT = Constants.WIRELESS_FLAG;//回到首页
                        Intent intent = new Intent(SetActivity.this,RegisterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        alertView.dismiss();
                    }
                }
            }).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {
                }
            });
            alertView.show();
        }
    }

    public void startActivity(Class activity, String title, String url) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.URL, url);
        startActivity(intent);
    }

    @Override
    public void checkUpdateSuccess(UpdateVo updateVo) {
        if (updateVo != null) {
            data = updateVo;
            mSetUpdateNumTv.setVisibility(View.VISIBLE);
            mSetUpdateNumTv.setText(updateVo.version.toString());
        }
    }

    @Override
    public void getAdvertSuccess(AdvertVo advertData) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iGetAdvertPresenter != null) {
            iGetAdvertPresenter.onDestroy();
        }
    }
}
