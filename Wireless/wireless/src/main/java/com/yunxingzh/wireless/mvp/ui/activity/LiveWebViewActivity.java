package com.yunxingzh.wireless.mvp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.BuildConfig;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

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

    private FrameLayout mOverFrameLay;
    private View mLiveOverView;
    private LinearLayout mLiveOverLay;
    private TextView mLiveNumTv;
    private int recLen = 3;
    private Handler handler = new Handler();
    private Animation animation;

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
        //直播倒计时遮罩
        mOverFrameLay = findView(R.id.over_lay);
        mLiveOverView = LayoutInflater.from(this).inflate(R.layout.live_over_layout, null);
        mLiveOverLay = (LinearLayout) mLiveOverView.findViewById(R.id.live_over_lay);
        mLiveNumTv = (TextView) mLiveOverView.findViewById(R.id.live_num_tv);
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

        animation = AnimationUtils.loadAnimation(LiveWebViewActivity.this, R.anim.live_count);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("huajiao")) {
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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

                if (view.canGoBack()) {//进入了第二个界面
                    mLiveOverLay.setVisibility(View.VISIBLE);
                    mLiveNumTv.startAnimation(animation);

                    ViewGroup parent = (ViewGroup) mLiveOverLay.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                    }
                    mOverFrameLay.addView(mLiveOverLay);

                    handler.postDelayed(runnable, 1000);

                    Timer timer = new Timer();
                    TimerTask taskThree = new TimerTask() {
                        @Override
                        public void run() {
                            MobclickAgent.onEvent(LiveWebViewActivity.this, "stream_view_3min");
                        }
                    };
                    timer.schedule(taskThree, 3000 * 60 * 3);//三分钟上报友盟
                    TimerTask taskFive = new TimerTask() {
                        @Override
                        public void run() {
                            MobclickAgent.onEvent(LiveWebViewActivity.this, "stream_view_5min");
                        }
                    };
                    timer.schedule(taskFive, 3000 * 60 * 5);//五分钟上报友盟
                }
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
                myWebView.loadUrl("javascript:(function() {" +
                        "$('.js_hj_download,.recommendArea,.qrcode,.open_huajiao,.tool-bar').hide();" +
                        "$('.popup-dialog').remove();" +
                        "})()");
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

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen != 1) {
                recLen--;
                mLiveNumTv.startAnimation(animation);
                mLiveNumTv.setText(recLen + "");
                handler.postDelayed(this, 1000);
            } else {
                mLiveOverLay.setVisibility(View.GONE);
            }
        }
    };

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
        mOverFrameLay.removeView(mLiveOverView);
        mLiveNumTv.clearAnimation();
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

    public void destroyWebView() {
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
