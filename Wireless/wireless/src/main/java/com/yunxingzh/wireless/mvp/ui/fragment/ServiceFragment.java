package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.ServiceVo;

/**
 * Created by stephon_ on 2016/11/1.
 * 服务
 */

public class ServiceFragment extends BaseFragment implements IServiceView{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

    }

    public void initData() {

    }

    @Override
    public void getServiceSuccess(ServiceVo serviceVo) {

    }
}
