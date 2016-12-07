package com.yunxingzh.wireless.mvp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Carey on 2016/5/25.
 */
public class NetWorkBaseActivity extends BaseActivity {

    private static final int REQUEST_NETWORK_ERROR = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (!NetUtils.isNetworkAvailable(getApplicationContext())) {
//            startActivity(new Intent(this, MainActivity.class));
//        }
    }
}
