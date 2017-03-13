package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.impl.FeedBackPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IFeedBackView;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

/**
 * Created by stephen on 2016/12/23.
 */

public class FeedBackActivity extends BaseActivity implements View.OnClickListener, IFeedBackView {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv, mFeedCommitTv;
    private EditText mFeedContextEt,mFeedPhoneEt;
    private FeedBackPresenterImpl feedBackPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText(R.string.feed_back);
        mFeedContextEt = findView(R.id.feed_context_et);
        mFeedCommitTv = findView(R.id.feed_commit_tv);
        mFeedCommitTv.setOnClickListener(this);
        mFeedPhoneEt = findView(R.id.feed_phone_et);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        feedBackPresenter = new FeedBackPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            MobclickAgent.onEvent(this,"feedback_cancel");
            finish();
        } else if (mFeedCommitTv == v) {
            if (!StringUtils.isEmpty(getContent())) {
                if (getContent().contains("\"")) {
                    String newStr = getContent().replace("\"","");
                    feedBackPresenter.feedBack(newStr,getContact());
                } else {
                    feedBackPresenter.feedBack(getContent(),getContact());
                }
            } else {
                ToastUtil.showMiddle(this, R.string.input_feed_content);
            }
        }
    }

    @Override
    public void feedBackSuccess() {
        ToastUtil.showMiddle(this, R.string.commit_success);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (feedBackPresenter != null) {
            feedBackPresenter.onDestroy();
        }
    }

    public String getContent() {
        return mFeedContextEt.getText() + "";
    }

    public String getContact() {
        return mFeedPhoneEt.getText() + "";
    }
}
