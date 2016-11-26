package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asus_ on 2016/11/26.
 * 欢迎界面
 */

public class WelcomActivity extends BaseActivity{

    boolean isFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //实现欢迎界面的自动跳转
        Timer timer = new Timer();
        isFirst = SPUtils.get(this,"isFirst",true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(isFirst){//第一次
                    SPUtils.put(WelcomActivity.this,"isFirst",false);
                    startActivity(GuidedActivity.class);
                }else{
                    startActivity(MainActivity.class);
                }
                finish();
            }
        };
        timer.schedule(task, 1000 * 2); //2秒后
    }

    public void startActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }
}
