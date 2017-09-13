package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dgwx.app.lib.common.util.StringUtil;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
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
    private String url, title;
    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        //mFragmentBar = findView(view, R.id.fragment_bar);
        mFragmentWebView = findView(view, R.id.fragment_webView);
        title = getArguments().getString(Constants.TITLE);
        Boolean noBack = getArguments().getBoolean("noBack");
        if (noBack) {
            mTitleReturnIv = findView(view, R.id.title_return_iv);
            mTitleReturnIv.setVisibility(View.INVISIBLE);
        }
        if (!StringUtils.isEmpty(title)) {
            mTitleNameTv = findView(view, R.id.title_name_tv);
            mTitleNameTv.setText(title);
        }
    }

    public void initData() {
        url = getArguments().getString(Constants.URL);

        WebViewUtil.initWebView(mFragmentWebView, mFragmentBar);
        mFragmentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return parseSelfSchema(request.getUrl().toString());
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return parseSelfSchema(url);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //mFragmentBar.setVisibility(View.GONE);
            }
        });
        if (!StringUtils.isEmpty(url)) {
            mFragmentWebView.loadUrl(url);
        }
    }

    public boolean parseSelfSchema(String url) {
        if (url.startsWith("tel:")) {//调起系统拨打电话和发送短信
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(intent);
            return true;
        } else if (url.startsWith("sms:")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(intent);
            return true;
        } else if (url.startsWith("alipays:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        } else if (url.startsWith("dgfree")) {
            Uri uri = Uri.parse(url);
            String query = uri.getEncodedQuery();
            String fragment = uri.getEncodedFragment();
            String newUrl = uri.getAuthority() + ":/" + uri.getPath();
            if (!StringUtils.isEmpty(query)) {
                newUrl += "?" + query;
            }
            if (!StringUtils.isEmpty(fragment)) {
                newUrl += "#" + fragment;
            }
            String type = url.startsWith("dgfree:") ? "零食商城" : "数码商城";
            startActivity(WebViewActivity.class, Constants.URL, newUrl, Constants.TITLE, type, Constants.FIX_TITLE, true);
            return true;
        }
        return false;
    }

    public void startActivity(Class activity, String key, String value,
                              String titleKey, String titleValue,
                              String fixTitleKey, Boolean fixTitle) {
        if (isAdded() && getActivity() != null) {
            Intent intent = new Intent(getActivity(), activity);
            intent.putExtra(key, value);
            intent.putExtra(titleKey, titleValue);
            intent.putExtra(fixTitleKey, fixTitle);
            startActivity(intent);
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
