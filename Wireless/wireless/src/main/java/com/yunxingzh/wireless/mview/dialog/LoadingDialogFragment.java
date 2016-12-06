package com.yunxingzh.wireless.mview.dialog;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.StringUtils;

/**
 * Created by carey on 2016/8/17 0017.
 */
public class LoadingDialogFragment extends DialogFragment {

    public static final String ARG_MESSAGE = "arg_message";

    private View mRootView;
    private ImageView mLoadingIv;
    private TextView mMessageTv;

    public static LoadingDialogFragment newInstance(String message) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_No_Border);
        setCancelable(false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.progress_loading, container, false);
            mMessageTv = (TextView) mRootView.findViewById(R.id.message);
            mLoadingIv = (ImageView) mRootView.findViewById(R.id.loading);
            // 获取ImageView上的动画背景
            AnimationDrawable spinner = (AnimationDrawable) mLoadingIv.getBackground();
            // 开始动画
            spinner.start();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }

        String message = getArguments().getString(ARG_MESSAGE);
        if (StringUtils.isEmpty(message)) {
            mMessageTv.setVisibility(View.GONE);
        } else {
            mMessageTv.setVisibility(View.VISIBLE);
            mMessageTv.setText(message);
        }
        return mRootView;
    }
}
