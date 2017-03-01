package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.config.MineHeadImg;
import com.yunxingzh.wireless.mview.ClearEditText;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IDefHeadPresenter;
import com.yunxingzh.wireless.mvp.presenter.IMinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.DefHeadPresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.MinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IDefHeadView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    private IMinePresenter iMinePresenter;
    private ClearEditText mNickInputEt;
    private Iterator<String> nickIterator;
    private Set<String> nickSets;
    private int refresh = 0;//刷新重试的次数

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
        iMinePresenter = new MinePresenterImpl(this);
        iDefHeadPresenter.getRandNick();
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mNickRefreshTv == v) {
            AppUtils.animation(mNickRefreshTv);
            if (!StringUtils.isEmpty(mNickInputEt.getText().toString())){
                mNickInputEt.setText("");
            }
            if (nickIterator != null && nickIterator.hasNext() && refresh < 10) {
                refresh ++;
                mNickInputEt.setHint(nickIterator.next());
            } else {
                nickIterator.remove();
                nickSets.clear();
                refresh = 0;
                iDefHeadPresenter.getRandNick();
            }
        } else if (mNickQueryTv == v) {
            if (!StringUtils.isEmpty(mNickInputEt.getText().toString())) {
                iMinePresenter.updateUserInfo("", mNickInputEt.getText().toString());
            } else if (!StringUtils.isEmpty(mNickInputEt.getHint().toString())) {
                iMinePresenter.updateUserInfo("", mNickInputEt.getHint().toString());
            } else {
                ToastUtil.showMiddle(this, "请输入昵称");
            }
        }
    }

    @Override
    public void getRandNickSuccess(NickNameList nickNameList) {
        if (nickNameList != null) {
            nickSets = nickNameList.nicknames;
            nickIterator = nickSets.iterator();
            mNickInputEt.setHint(nickIterator.next());
        }
    }

    @Override
    public void updateUserInfoSuccess() {
        ToastUtil.showMiddle(this, "恭喜，修改成功");
        String nickName = "";
        if (!StringUtils.isEmpty(mNickInputEt.getText().toString())) {
            nickName = mNickInputEt.getText().toString();
        } else if (!StringUtils.isEmpty(mNickInputEt.getHint().toString())) {
            nickName = mNickInputEt.getHint().toString();
        }
        MainApplication.get().setNick(nickName);
        EventBus.getDefault().post(new MineHeadImg(Constants.NICK_NAME_FLAG, nickName));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nickSets != null) {
            nickIterator.remove();
            nickSets.clear();
        }
        if (iDefHeadPresenter != null && iMinePresenter != null) {
            iDefHeadPresenter.onDestroy();
            iMinePresenter.onDestroy();
        }
    }

    @Override
    public void getDefHeadSuccess(DefHeadList defHeadList) {
    }
}
