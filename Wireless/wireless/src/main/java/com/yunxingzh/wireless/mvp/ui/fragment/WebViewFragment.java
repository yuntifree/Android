package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.WebViewUtil;

/**
 * Created by stephen on 2017/1/2.
 *  头条分类html页
 */

public class WebViewFragment extends BaseFragment {

    private ProgressBar mFragmentBar;
    private WebView mFragmentWebView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mFragmentBar = findView(view, R.id.fragment_bar);
        mFragmentWebView = findView(view, R.id.fragment_webView);
    }

    public void initData() {
        url = getArguments().getString(Constants.URL);
        WebViewUtil.initWebView(mFragmentWebView, mFragmentBar);
        mFragmentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mFragmentBar.setVisibility(View.GONE);
            }
        });
        if (!StringUtils.isEmpty(url)) {
            mFragmentWebView.loadUrl(url);
        }
    }

    @Override
    public void onDestroy() {
        if (mFragmentWebView != null) {
            mFragmentWebView.removeAllViews();
            mFragmentWebView.destroy();
            mFragmentWebView = null;
        }
        super.onDestroy();
    }
}
