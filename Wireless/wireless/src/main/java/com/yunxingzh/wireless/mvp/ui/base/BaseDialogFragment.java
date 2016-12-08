package com.yunxingzh.wireless.mvp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.mvp.view.IBaseView;
import com.yunxingzh.wireless.utils.ToastUtil;

/**
 * Created by Carey on 2016/5/28.
 */
public class BaseDialogFragment extends DialogFragment implements IBaseView {

    protected <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }


    protected int mLoadingCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void showError(int error) {
        ToastUtil.showError(getActivity(), error);
    }

}
