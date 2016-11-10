package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.WebViewUtil;

/**
 * Created by stephon on 2016/11/8.
 * 视频播放
 */

public class VideoPlayActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar mProgressBar;
    private String playUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        webView = findView(R.id.webView);
        mProgressBar = findView(R.id.progress_bar);
    }

    public void initData() {
        playUrl = getIntent().getStringExtra(Constants.VIDEO_URL);

        WebViewUtil.initWebView(webView, mProgressBar);
        webView.setWebViewClient(new WebViewClient() {
            /**
             * 当前网页的链接仍在webView中跳转
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 处理ssl请求
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /**
             * 页面载入完成回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:try{autoplay();}catch(e){}");
               // view.loadUrl("javascript:(function() {alert(1);}})()");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 显示自定义视图，无此方法视频不能播放
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
        webView.loadUrl("file:///android_asset/video.html");
    }

    @Override
    protected void onPause() {
        if(null != webView) {
            webView.onPause();
        }
        super.onPause();
    }

}
