package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.WebViewUtil;

/**
 * Created by stephon on 2016/11/10.
 * 新闻详情
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv,mWebCloseTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String mTitle;
    private String advertFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        mWebCloseTv = findView(R.id.web_close_tv);
        mWebCloseTv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.web_name_tv);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleReturnIv = findView(R.id.web_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        myWebView = findView(R.id.webView);
        myProgressBar = findView(R.id.progress_bar);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));

        mUrl = getIntent().getStringExtra(Constants.URL);
        mTitle = getIntent().getStringExtra(Constants.TITLE);
        advertFlag = getIntent().getStringExtra(Constants.ADVERT_FLAG);
        if (!StringUtils.isEmpty(mTitle)) {
            mTitleNameTv.setText(mTitle);
        }
        WebViewUtil.initWebView(myWebView, myProgressBar);
        WebSettings settings = myWebView.getSettings();
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(true);// 隐藏缩放按钮

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myProgressBar.setVisibility(View.GONE);
                if (mWebCloseTv.getVisibility() == View.INVISIBLE){
                    if (myWebView.canGoBack()) {
                        mWebCloseTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!myWebView.canGoBack()) {
                        mWebCloseTv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            //配置权限（同样在WebChromeClient中实现）
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtils.isEmpty(mTitle) && !StringUtils.isEmpty(title)) {
                    mTitle = title;
                    mTitleNameTv.setText(title);
                }
            }
        });

        myWebView.loadUrl(mUrl);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {//返回
            if (myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                if (!StringUtils.isEmpty(advertFlag)){
                    startActivity(MainActivity.class);
                }
                finish();
            }
        } else if (mWebCloseTv == v){//关闭
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
                myWebView.goBack(); // goBack()表示返回WebView的上一页面
            } else {
                if (!StringUtils.isEmpty(advertFlag)) {
                    startActivity(MainActivity.class);
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
