package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.FileUtil;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asus_ on 2016/11/26.
 * 欢迎界面
 */

public class WelcomActivity extends BaseActivity {

    boolean isFirst;
    private String url;
    private String imgPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //实现欢迎界面的自动跳转
        Timer timer = new Timer();

        isFirst = SPUtils.get(WelcomActivity.this, "isFirst", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isFirst) {//第一次
                    SPUtils.put(WelcomActivity.this, "isFirst", false);
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

    public void startActivity(Class activity, String urlKey, String url, String byteKey, String mByte) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(urlKey, url);
        intent.putExtra(byteKey, mByte);
        startActivity(intent);
    }
}
