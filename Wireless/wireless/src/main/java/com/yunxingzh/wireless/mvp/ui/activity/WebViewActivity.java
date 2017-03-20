package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
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
import com.yunxingzh.wireless.utils.WebViewUtil;

import java.lang.reflect.Field;

/**
 * Created by stephon on 2016/11/10.
 * 新闻详情
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mTitleReturnIv, mWebCloseTv;
    private TextView mTitleNameTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String mTitle;
    private String advertFlag;
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
        mWebCloseTv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.web_name_tv);
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
        advertFlag = getIntent().getStringExtra(Constants.ADVERT_FLAG);
        if (!StringUtils.isEmpty(mTitle)) {
            mTitleNameTv.setText(mTitle);
        }
        WebViewUtil.initWebView(myWebView, myProgressBar);
        WebSettings settings = myWebView.getSettings();
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return false;
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {//调起系统拨打电话和发送短信
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("sms:")) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myProgressBar.setVisibility(View.GONE);
                if (mWebCloseTv.getVisibility() == View.INVISIBLE){
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
                if (!StringUtils.isEmpty(title)) {
                    mTitle = title;
                    mTitleNameTv.setText(title);
                } else if (!StringUtils.isEmpty(mTitle)) {
                    mTitleNameTv.setText(mTitle);
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
            if (!StringUtils.isEmpty(advertFlag)) {
                startActivity(MainActivity.class);
            }
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

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        myWebView.onPause();
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        myWebView.onResume();
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        super.onResume();
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

    public void destroyWebView(){
        if (myWebView != null) {
            myWebView.setVisibility(View.GONE);
            releaseAllWebViewCallback();
            myWebView.removeAllViews();
            myWebView.destroy();
            myWebView = null;
        }
    }

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
