package com.yunxingzh.wireless.mvp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.config.MainApplication;
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

    private String pageName;
    public BaseFragment() {
        pageName = getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void needLogin() {
        if (MainApplication.get().needLogin()) {
            startActivity(new Intent());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() == null) {
            return;
        }
        if(getUserVisibleHint()){
            onVisibilityChangedToUser(true, false);
        }
       // MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(isResumed()){
            onVisibilityChangedToUser(hidden, true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isResumed()){
            onVisibilityChangedToUser(isVisibleToUser, true);
        }
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     * @param isVisibleToUser true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod){
        if(isVisibleToUser){
            if(pageName != null){
                MobclickAgent.onPageStart(pageName);
                Log.i("UmengPageTrack", pageName + " - display - "+(isHappenedInSetUserVisibleHintMethod?"setUserVisibleHint":"onResume"));
            }
        }else{
            if(pageName != null){
                MobclickAgent.onPageEnd(pageName);
                Log.w("UmengPageTrack", pageName + " - hidden - "+(isHappenedInSetUserVisibleHintMethod?"setUserVisibleHint":"onPause"));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            getView().setOnTouchListener(this);
        }
        if(getUserVisibleHint()){
            onVisibilityChangedToUser(true, false);
        }
       // MobclickAgent.onPageStart(getClass().getName());
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
