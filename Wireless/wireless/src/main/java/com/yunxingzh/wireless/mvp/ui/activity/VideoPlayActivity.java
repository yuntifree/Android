package com.yunxingzh.wireless.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yunxingzh.wireless.BuildConfig;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.WebViewUtil;

import java.lang.reflect.Field;

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

    @SuppressLint("JavascriptInterface")
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

        webView.addJavascriptInterface(new JSClass(), "JSHost");

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
                // 页面做了跳转，loadCount最后一次再注入脚本
                //loadCount++;
                //if (loadCount > 1)
                {
                    view.loadUrl("javascript:(function() { " +
                            "document.getElementById('youkuplayer').style.height = document.documentElement.clientHeight + 'px';" +
                            "var videos = document.getElementsByTagName('video'); " +
                                "for(var i=0;i<videos.length;i++){" +
                                   "videos[i].addEventListener('ended', function () { JSHost.closeWindow(); }, false);"+
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
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(false);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(null != webView) {
            webView.onResume();
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.setVisibility(View.GONE);
            releaseAllWebViewCallback();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public void releaseAllWebViewCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JSClass extends Object {
        @JavascriptInterface
        public void closeWindow() {
            finish();
        }
    }
}
