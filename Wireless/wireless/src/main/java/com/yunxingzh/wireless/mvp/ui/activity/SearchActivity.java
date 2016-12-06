package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.ClearEditText;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.StringUtils;

/**
 * Created by stephon on 2016/11/17.
 * 搜索
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ClearEditText mSearchEt;
    private TextView mSearchBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    public void initView() {
        mSearchEt = findView(R.id.search_et);
        mSearchEt.addTextChangedListener(this);
        mSearchBtn = findView(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);
    }

    public void initData() {
        //定时弹出软键盘
        StringUtils.popUpKeyboard(mSearchEt);
    }

    @Override
    public void onClick(View v) {
        if (mSearchBtn == v) {
            if (mSearchBtn.getText().equals("取消")) {
                finish();
            } else if (mSearchBtn.getText().equals("搜索")) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constants.URL, "https://m.baidu.com/s?word=" + mSearchEt.getText().toString() + "");
                intent.putExtra(Constants.TITLE, "百度");
                startActivity(intent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mSearchEt.getText().length() != 0) {
            mSearchBtn.setText(R.string.search);
        } else {
            mSearchBtn.setText(R.string.cancel);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
