package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;
import com.yunxingzh.wireless.utils.WebViewUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by stephon on 2016/11/10.
 * 新闻详情
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mTitleRightLay, mTitleLeftLay;
    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String mTitle;
    private boolean fromNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        myWebView = findView(R.id.webView);
        myProgressBar = findView(R.id.progress_bar);
        mTitleRightLay = findView(R.id.title_right_lay);
        mTitleLeftLay = findView(R.id.title_left_lay);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        fromNews = getIntent().getBooleanExtra(Constants.FROM_NEWS, false);
        if (fromNews) {
            mTitleRightLay.setVisibility(View.INVISIBLE);
            mTitleNameTv.setLayoutParams(setParams(50));
            mTitleLeftLay.setLayoutParams(setParams(0));
        }
        mUrl = getIntent().getStringExtra(Constants.URL);
        mTitle = getIntent().getStringExtra(Constants.TITLE);
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
        if (mTitleReturnIv == v) {
            if (myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                startActivity(MainActivity.class);
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
                myWebView.goBack(); // goBack()表示返回WebView的上一页面
            } else {
                startActivity(MainActivity.class);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public LinearLayout.LayoutParams setParams(int right) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 0;
        lp.setMargins(0, 0, right, 0);
        lp.gravity = Gravity.CENTER;
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        return lp;
    }

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
