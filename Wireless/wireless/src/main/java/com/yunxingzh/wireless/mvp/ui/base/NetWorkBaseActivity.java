package com.yunxingzh.wireless.mvp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yunxingzh.wireless.mvp.ui.activity.HttpErrorActivity;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

/**
 * Created by Carey on 2016/5/25.
 */
public class NetWorkBaseActivity extends BaseActivity {

    private static final int REQUEST_NETWORK_ERROR = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetUtils.isNetworkAvailable(getApplicationContext())) {
            startActivityForResult(new Intent(this, HttpErrorActivity.class), REQUEST_NETWORK_ERROR);
        }
    }
}
