package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;

/**
 * Created by stephon on 2016/11/8.
 * 视频播放
 */

public class VideoPlayActivity extends BaseActivity{

    private String playUrl;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    public void initView() {

    }

    public void initData() {
        playUrl = getIntent().getStringExtra(Constants.VIDEO_URL);
    }
}
