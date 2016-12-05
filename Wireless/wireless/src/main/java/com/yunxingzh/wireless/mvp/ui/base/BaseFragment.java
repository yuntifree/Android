package com.yunxingzh.wireless.mvp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.activity.HttpErrorActivity;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IBaseView;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.view.dialog.LoadingDialogFragment;


/**
 * Created by Carey on 2016/5/28.
 */
public class BaseFragment extends Fragment implements IBaseView, View.OnTouchListener {

    protected LoadingDialogFragment mLoadingDialog;

    protected int mLoadingCount = 0;

    protected <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //听云
        NBSAppAgent.setLicenseKey("87fb7caacc08462a8aecd82cb1c6d4fd").withLocationServiceEnabled(true).start(getActivity().getApplicationContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void needLogin() {
        if (MyApplication.sApplication.needLogin()) {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialogFragment.newInstance("");
        }
        if (mLoadingCount == 0) {
            mLoadingDialog.show(getActivity().getSupportFragmentManager(), "loading_dialog");
        }
        mLoadingCount++;
    }

    @Override
    public void hideProgress() {
        mLoadingCount--;
        if (mLoadingDialog != null && mLoadingCount <= 0) {
            mLoadingDialog.dismiss();
            mLoadingCount = 0;
        }
    }

    @Override
    public void showError(int error) {
        ToastUtil.showError(getActivity(), error);
    }

}
