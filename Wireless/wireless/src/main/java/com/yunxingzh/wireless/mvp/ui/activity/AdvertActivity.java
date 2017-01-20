package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

/**
 * Created by stephen on 2016/12/12.
 * 应用启动-广告活动页
 */

public class AdvertActivity extends BaseActivity implements View.OnClickListener {

    private TextView mAdvertInfoTv, mAdvertTimeTv;
    private ImageView mAdvertBgIv;
    private LinearLayout mAdvertJumpLay, mAdvertClickLay;
    private int recLen = 2;
    private Handler handler = new Handler();

    private String url;
    private String imgPath;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_advert);
        initView();
        initData();
    }

    public void initView() {
        mAdvertInfoTv = findView(R.id.advert_info_tv);
        mAdvertInfoTv.setOnClickListener(this);
        mAdvertJumpLay = findView(R.id.advert_jump_lay);
        mAdvertJumpLay.setOnClickListener(this);
        mAdvertTimeTv = findView(R.id.advert_time_tv);
        mAdvertBgIv = findView(R.id.advert_bg_iv);
        mAdvertBgIv.setOnClickListener(this);
        mAdvertClickLay = findView(R.id.advert_click_lay);
    }

    public void initData() {
        imgPath = getIntent().getStringExtra(Constants.ADVERT_IMG);
        url = getIntent().getStringExtra(Constants.ADVERT_URL);
        title = SPUtils.get(this,Constants.TITLE,"");
        if (!StringUtils.isEmpty(imgPath)) {
            Bitmap img = BitmapFactory.decodeFile(imgPath);
            mAdvertBgIv.setImageBitmap(img);
        }
        if (StringUtils.isEmpty(url)) {
            mAdvertBgIv.setEnabled(false);
            mAdvertClickLay.setVisibility(View.GONE);
        }
        handler.postDelayed(runnable, 1000);
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen != 0) {
                recLen--;
                mAdvertTimeTv.setText(recLen + "");
                handler.postDelayed(this, 1000);
            } else {
                startActivity(MainActivity.class, "", "", "", "", "");
                finish();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (mAdvertInfoTv == v || mAdvertBgIv == v) {//了解详情
            if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(title)) {
                handler.removeCallbacks(runnable);
                startActivity(WebViewActivity.class, Constants.URL, url, Constants.TITLE, title, Constants.ADVERT_FLAG);
                finish();
            }
        } else if (mAdvertJumpLay == v) {//跳过
            startActivity(MainActivity.class, "", "", "", "", "");
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void startActivity(Class activity, String urlKey, String url, String titleKey, String title, String advertFlag) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(urlKey, url);
        intent.putExtra(titleKey, title);
        intent.putExtra(Constants.ADVERT_FLAG, advertFlag);
        startActivity(intent);
    }
}
