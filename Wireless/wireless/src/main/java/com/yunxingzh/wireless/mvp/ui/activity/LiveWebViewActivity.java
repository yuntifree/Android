package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunxingzh.wireless.BuildConfig;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by stephen on 2017/2/14.
 * 直播webview
 */

public class LiveWebViewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv, mWebCloseTv;
    private TextView mTitleNameTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String mTitle;
    private View mWebLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        mWebCloseTv = findView(R.id.web_close_tv);
        mTitleNameTv = findView(R.id.web_name_tv);
        mWebCloseTv.setOnClickListener(this);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleReturnIv = findView(R.id.web_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        myWebView = findView(R.id.webView);
        myProgressBar = findView(R.id.progress_bar);
        mWebLine = findView(R.id.web_line);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        mUrl = getIntent().getStringExtra(Constants.URL);
        mTitle = getIntent().getStringExtra(Constants.TITLE);
        if (!StringUtils.isEmpty(mTitle)) {
            mTitleNameTv.setText(mTitle);
        }
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setAppCacheEnabled(true);// 是否使用缓存
        webSettings.setDomStorageEnabled(true);// DOM Storage
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("huajiao")) {
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                myProgressBar.setVisibility(View.GONE);
                if (mWebCloseTv.getVisibility() == View.INVISIBLE) {
                    if (myWebView.canGoBack()) {
                        mWebCloseTv.setVisibility(View.VISIBLE);
                        mWebLine.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!myWebView.canGoBack()) {
                        mWebCloseTv.setVisibility(View.INVISIBLE);
                        mWebLine.setVisibility(View.INVISIBLE);
                    }
                }

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (view.canGoBack()) {
                            view.loadUrl("javascript:(function() {" +
                                    "$('.js_hj_download,.recommendArea,.qrcode,.open_huajiao,.tool-bar').hide();" +
                                    "$('.popup-dialog').remove();" +
                                    "})()");
                        }
                    }
                }, 1500);
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtils.isEmpty(mTitle) && !StringUtils.isEmpty(title)) {
                    mTitle = title;
                    mTitleNameTv.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {//进度100
                    myProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == myProgressBar.getVisibility()) {
                        myProgressBar.setVisibility(View.VISIBLE);
                    }
                    myProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        myWebView.loadUrl(mUrl);
    }

    @Override
    public void onClick(View v) {
        if (mWebCloseTv == v || mTitleReturnIv == v) {//关闭
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // 直接关闭播放器
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void startActivity(Class activity) {
//        Intent intent = new Intent(this, activity);
//        startActivity(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    @Override
    protected void onResume() {
        if (null != myWebView) {
            myWebView.onResume();
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != myWebView) {
            myWebView.onPause();
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(false);
        }
        super.onPause();
    }

    public void destroyWebView(){
        if (myWebView != null) {
            myWebView.setVisibility(View.GONE);
            releaseAllWebViewCallback();
            myWebView.removeAllViews();
            myWebView.destroy();
            myWebView = null;
        }
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
}
