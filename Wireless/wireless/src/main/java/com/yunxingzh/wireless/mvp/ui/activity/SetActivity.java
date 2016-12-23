package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunxingzh.wireless.BuildConfig;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.CacheCleanUtil;

/**
 * Created by stephen on 2016/12/23.
 * 设置
 */

public class SetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv, mSetCacheSizeTv, mSetUseTv, mSetVersionTv;
    private LinearLayout mSetCleanLay, mSetIdeaLay, mSetAboutLay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.set);
        mSetVersionTv = findView(R.id.set_version_tv);
        mSetCacheSizeTv = findView(R.id.set_cache_size_tv);
        mSetCleanLay = findView(R.id.set_clean_lay);
        mSetCleanLay.setOnClickListener(this);
        mSetIdeaLay = findView(R.id.set_idea_lay);
        mSetIdeaLay.setOnClickListener(this);
        mSetAboutLay = findView(R.id.set_about_lay);
        mSetAboutLay.setOnClickListener(this);
        mSetUseTv = findView(R.id.set_use_tv);
        mSetUseTv.setOnClickListener(this);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        mSetVersionTv.setText("v" + BuildConfig.VERSION_NAME);
        mSetCacheSizeTv.setText(CacheCleanUtil.getTotalCacheSize(this));
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mSetCleanLay == v) {//清除缓存
            CacheCleanUtil.clearAllCache(this);
        } else if (mSetIdeaLay == v) {//意见反馈
            startActivity(FeedBackActivity.class, "", "");
        } else if (mSetAboutLay == v) {//关于我们
            startActivity(WebViewActivity.class, "关于我们", Constants.ABOUT_US);
        } else if (mSetUseTv == v) {//用户协议
            startActivity(WebViewActivity.class, "用户协议", Constants.URL_AGREEMENT);
        }
    }

    public void startActivity(Class activity, String title, String url) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.URL, url);
        startActivity(intent);
    }
}
