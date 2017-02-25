package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;

/**
 * Created by stephen on 2017/2/25.\
 * 修改昵称
 */

public class NickNameActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText("昵称");
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        }
    }
}
