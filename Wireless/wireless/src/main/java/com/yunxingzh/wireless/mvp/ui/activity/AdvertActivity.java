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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.StringUtils;

/**
 * Created by stephen on 2016/12/12.
 * 应用启动-广告活动页
 */

public class AdvertActivity extends BaseActivity implements View.OnClickListener {

    private TextView mAdvertInfoTv, mAdvertTimeTv;
    private ImageView mAdvertBgIv;
    private LinearLayout mAdvertJumpLay;
    private int recLen = 3;
    private Handler handler = new Handler();

    private String url;
    private String imgPath;

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
    }

    public void initData() {
        imgPath = getIntent().getStringExtra(Constants.ADVERT_IMG);
        url = getIntent().getStringExtra(Constants.ADVERT_URL);
        if (!StringUtils.isEmpty(imgPath) && !StringUtils.isEmpty(url)) {
            Bitmap img = BitmapFactory.decodeFile(imgPath);
            mAdvertBgIv.setImageBitmap(img);
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
        if (mAdvertInfoTv == v) {//了解详情
            if (!StringUtils.isEmpty(url)) {
                startActivity(WebViewActivity.class, Constants.URL, url, Constants.TITLE, "", Constants.FROM_ADVERT);
            }
            finish();
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

    public void startActivity(Class activity, String urlKey, String url, String titleKey, String title, String flag) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(urlKey, url);
        intent.putExtra(titleKey, title);
        intent.putExtra("fromAdvert", flag);
        startActivity(intent);
    }
}
