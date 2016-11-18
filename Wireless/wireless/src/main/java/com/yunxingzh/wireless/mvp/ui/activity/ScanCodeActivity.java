package com.yunxingzh.wireless.mvp.ui.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;

/**
 * Created by stephon on 2016/11/17.
 * 首页扫码连接东莞wifi
 */

public class ScanCodeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.safecenter_barcode);
    }

    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
