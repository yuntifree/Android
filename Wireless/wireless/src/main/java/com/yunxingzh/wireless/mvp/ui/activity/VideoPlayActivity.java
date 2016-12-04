package com.yunxingzh.wireless.mvp.ui.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
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

    private View title;
    private WebView webView;
    private ProgressBar mProgressBar;
    private String playUrl;
    private int loadCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        webView = findView(R.id.webView);
        title = findView(R.id.title);
        title.setVisibility(View.GONE);
        mProgressBar = findView(R.id.progress_bar);
    }

    public void initData() {
        playUrl = getIntent().getStringExtra(Constants.VIDEO_URL);
        WebViewUtil.initWebView(webView, mProgressBar);
        loadCount = 0;
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
                //super.onPageFinished(view, url);
                //view.loadUrl("javascript:try{autoplay();}catch(e){}");
                loadCount++;
                if (loadCount > 1) {
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('video'); " +
                                "for(var i=0;i<videos.length;i++){" +
                                    "videos[i].play();" +
                             "videos[i].addEventListener('ended', function () {  window.close(); alert('11111111')  }, false);"+
                            "}" +
                            "})()");
                }
                mProgressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(playUrl);
    }

    @Override
    protected void onPause() {
        if(null != webView) {
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}
