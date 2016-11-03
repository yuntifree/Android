package com.yunxingzh.wireless.mvp.ui.utils;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Stephen on 2016/9/20.
 */
public class WebViewUtil {

    private final static int DOWN_LOAD_OVER = 100;

    public static void initWebView(WebView webView,final ProgressBar bar){
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webView.getSettings().setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView.getSettings().setSupportZoom(true);// 是否可以缩放，默认true
        webView.getSettings().setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webView.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webView.getSettings().setAppCacheEnabled(true);// 是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);// DOM Storage
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == DOWN_LOAD_OVER) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
    }
}
