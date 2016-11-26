package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.NetWorkBaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.WebViewUtil;

/**
 * Created by stephon on 2016/11/10.
 * 新闻详情
 */

public class WebViewActivity extends NetWorkBaseActivity implements View.OnClickListener{

    private ImageView mWebLeftIv;
    private TextView mWebTitleTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initData();
    }

    public void initView() {
        mWebTitleTv = findView(R.id.web_title_tv);
        mWebTitleTv.setVisibility(View.VISIBLE);
        mWebLeftIv = findView(R.id.web_left_iv);
        mWebLeftIv.setOnClickListener(this);
        myWebView = findView(R.id.webView);
        myProgressBar = findView(R.id.progress_bar);
    }

    public void initData() {
        mUrl = getIntent().getStringExtra(Constants.URL);
        title = getIntent().getStringExtra(Constants.TITLE);
        mWebTitleTv.setText(title);
        WebViewUtil.initWebView(myWebView, myProgressBar);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view,String url){
                myProgressBar.setVisibility(View.GONE);
            }
    });
        myWebView.loadUrl(mUrl);
    }

    @Override
    public void onClick(View v) {
        if (mWebLeftIv == v ){
            finish();
        }
    }
}
