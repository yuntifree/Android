package com.yunxingzh.wireless.mvp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.ui.activity.HttpErrorActivity;
import com.yunxingzh.wireless.mvp.view.IBaseView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;


/**
 * Created by Carey on 2016/5/28.
 */
public class BaseFragment extends Fragment implements IBaseView, View.OnTouchListener {

    protected int mLoadingCount = 0;

    protected <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void needLogin() {
        if (MainApplication.sApplication.needLogin()) {
            startActivity(new Intent());
        }
    }

    public void needNetWork() {
        if (!NetUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            startActivity(new Intent(getActivity(), HttpErrorActivity.class));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            getView().setOnTouchListener(this);
        }
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void showError(int error) {
        ToastUtil.showError(getActivity(), error);
    }

}
