package com.yunxingzh.wireless.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Stephen on 2016/9/20.
 */
public class WebViewUtil {


    public static void initWebView(WebView webView, final ProgressBar bar){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);// 是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);// 是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);// 是否显示缩放按钮，默认false
        webSettings.setDisplayZoomControls(false);//不显示webview缩放按钮
        webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);// 是否使用缓存
        webSettings.setDomStorageEnabled(true);// DOM Storage
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
    }
}
