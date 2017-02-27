package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.ClearEditText;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.DefHeadPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;

import wireless.libs.bean.resp.DefHeadList;
import wireless.libs.bean.resp.NickNameList;

/**
 * Created by stephen on 2017/2/25.\
 * 修改昵称
 */

public class NickNameActivity extends BaseActivity implements IDefHeadView, View.OnClickListener {

    private ImageView mTitleReturnIv;
    private TextView mTitleNameTv, mNickQueryTv, mNickRefreshTv;
    private IDefHeadPresenter iDefHeadPresenter;
    private ClearEditText mNickInputEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        initView();
        initData();
    }

    public void initView() {
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setText("昵称");
        mNickRefreshTv = findView(R.id.nick_refresh_tv);
        mNickRefreshTv.setOnClickListener(this);
        mNickQueryTv = findView(R.id.nick_query_tv);
        mNickQueryTv.setOnClickListener(this);
        mNickInputEt = findView(R.id.nick_input_et);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        iDefHeadPresenter = new DefHeadPresenterImpl(this);
        iDefHeadPresenter.getRandNick();
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mNickRefreshTv == v) {

        } else if (mNickQueryTv == v) {

        }
    }

    @Override
    public void getRandNickSuccess(NickNameList nickNameList) {
        //mNickInputEt.setHint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDefHeadPresenter != null) {
            iDefHeadPresenter.onDestroy();
        }
    }

    @Override
    public void getDefHeadSuccess(DefHeadList defHeadList) {}
}
