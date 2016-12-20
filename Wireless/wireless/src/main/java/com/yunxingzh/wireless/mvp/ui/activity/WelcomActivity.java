package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IConnectDGCountPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.ConnectDGCountPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;
import com.yunxingzh.wireless.utils.BitmapUtils;
import com.yunxingzh.wireless.utils.FileUtil;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import u.aly.d;
import wireless.libs.bean.vo.AdvertVo;

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
                    imgPath = SPUtils.get(WelcomActivity.this, Constants.ADVERT_IMG, "");
                    url = SPUtils.get(WelcomActivity.this, Constants.ADVERT_URL, "");
                    if (!StringUtils.isEmpty(imgPath) && !StringUtils.isEmpty(url)) {
                        startActivity(AdvertActivity.class, Constants.ADVERT_URL, url, Constants.ADVERT_IMG, imgPath);
                    } else {
                        //本地没有直接跳首页
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
