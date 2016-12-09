package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.ToastUtil;
import com.yunxingzh.wireless.utils.WebViewUtil;

/**
 * Created by stephon on 2016/11/10.
 * 新闻详情
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mTitleRightLay,mTitleLeftLay;
    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;
    private WebView myWebView;
    private ProgressBar myProgressBar;
    private String mUrl;
    private String title;
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
        fromNews = getIntent().getBooleanExtra(Constants.FROM_NEWS,false);
        if (fromNews){
            mTitleRightLay.setVisibility(View.INVISIBLE);
            mTitleNameTv.setLayoutParams(setParams(50));
            mTitleLeftLay.setLayoutParams(setParams(0));
        }
        mUrl = getIntent().getStringExtra(Constants.URL);
        title = getIntent().getStringExtra(Constants.TITLE);
        mTitleNameTv.setText(title);
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
        if (mTitleReturnIv == v ){
            finish();
        }
    }

    public LinearLayout.LayoutParams setParams(int right){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 0;
        lp.setMargins(0,0,right,0);
        lp.gravity = Gravity.CENTER;
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        return lp;
    }
}
