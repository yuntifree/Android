package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.FileUtil;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

import wireless.libs.bean.vo.AutoLoginVo;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by asus_ on 2016/11/26.
 * 欢迎界面
 */

public class WelcomActivity extends BaseActivity {

    private int version;
    private String url;
    private String imgPath;
    private Timer timer;
    private TimerTask task;
    private ImageView mSpinnerIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        mSpinnerIv = (ImageView) findViewById(R.id.spinner_iv);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) mSpinnerIv.getBackground();
        // 开始动画
        spinner.start();

        if (!StringUtils.isEmpty(MainApplication.get().getToken()) &&
                StringUtils.isExpired(MainApplication.get().getExpire())) {//如果过期
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AutoLoginVo data = NetWorkWarpper.autoLogin(MainApplication.get().getPrivdata());
                    if (data != null) {
                        MainApplication.get().setExpire(data.expiretime);
                        MainApplication.get().setToken(data.token);
                        MainApplication.get().setPrivdata(data.privdata);
                    }

                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }).start();
        } else {
            advertJump();
        }
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                advertJump();
            }
        }
    };
    public void advertJump(){
        //实现欢迎界面的自动跳转
        timer = new Timer();
        version = SPUtils.get(WelcomActivity.this, "version", 0);
        final int currentVersion = AppUtils.getVersionCode(WelcomActivity.this);
        task = new TimerTask() {
            @Override
            public void run() {
                if (version == 0 || version != currentVersion) {//第一次或者覆盖安装新版本
                    SPUtils.put(WelcomActivity.this, "version", AppUtils.getVersionCode(WelcomActivity.this));
                    startActivity(GuidedActivity.class, "", "", "", "");
                    finish();
                } else {
                    String expireDate = SPUtils.get(MainApplication.get(),Constants.ADVERT_DATE,"");
                    if (!StringUtils.isExpired(expireDate)) {
                        imgPath = SPUtils.get(MainApplication.get(), Constants.ADVERT_IMG, "");
                        url = SPUtils.get(MainApplication.get(), Constants.ADVERT_URL, "");
                        if (FileUtil.isFileExist(imgPath) && !StringUtils.isEmpty(url)) {
                            startActivity(AdvertActivity.class, Constants.ADVERT_URL, url, Constants.ADVERT_IMG, imgPath);
                        } else {
                            //本地没有直接跳首页
                            startActivity(MainActivity.class, "", "", "", "");
                        }
                    } else {
                        //活动过期直接跳首页
                        startActivity(MainActivity.class, "", "", "", "");
                    }
                    finish();
                }
            }
        };
        timer.schedule(task, 1000); //1秒后
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void startActivity(Class activity, String urlKey, String url, String byteKey, String mByte) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(urlKey, url);
        intent.putExtra(byteKey, mByte);
        startActivity(intent);
    }
}
