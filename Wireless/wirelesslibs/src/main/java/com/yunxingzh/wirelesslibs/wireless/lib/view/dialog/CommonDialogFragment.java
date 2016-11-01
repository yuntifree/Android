package com.yunxingzh.wirelesslibs.wireless.lib.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunxingzh.wirelesslibs.R;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

/**
 * Created by carey on 2016/6/12 0012.
 */
public class CommonDialogFragment extends DialogFragment {
    public static final String ARG_TITLE = "arg_title";
    public static final String ARG_MESSAGE = "arg_message";
    public static final String ARG_POSITIVE = "arg_positive";
    public static final String ARG_NEGATIVE = "arg_negative";
    public static final String ARG_CUSTOM_VIEW = "arg_custom_view";

    private TextView mTitleTv;
    private TextView mMessageTv;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private View mCloseIv;
    private LinearLayout mContainerLay;

    private View mCustomView;

    public <T extends View> T findView(int resId) {
        return (T) mCustomView.findViewById(resId);
    }

    public static CommonDialogFragment newInstance(Context context, int customView) {
        return newInstance(context, null, null, customView, null, null);
    }

    public static CommonDialogFragment newInstance(Context context, String title, int customView) {
        return newInstance(context, title, null, customView, null, null);
    }

    public static CommonDialogFragment newInstance(Context context, String title, int customView, String positiveBtn, String negativeBtn) {
        return newInstance(context, title, null, customView, positiveBtn, negativeBtn);
    }

    public static CommonDialogFragment newInstance(Context context, String title, String message, String positiveBtn, String negativeBtn) {
        return newInstance(context, title, message, 0, positiveBtn, negativeBtn);
    }

    public static CommonDialogFragment newInstance(Context context, String title, String message, int customView, String positiveBtn, String negativeBtn) {
        CommonDialogFragment fragment = new CommonDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE, positiveBtn);
        args.putString(ARG_NEGATIVE, negativeBtn);
        fragment.setArguments(args);
        if (customView > 0) {
            fragment.mCustomView = View.inflate(context, customView, null);
        }
        return fragment;
    }

    private CommonDialogListener mListener;

    public interface CommonDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public void setCommonDialogListener(CommonDialogListener listener){
        mListener = listener;
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (activity instanceof CommonDialogListener) {
//            mListener = (CommonDialogListener) activity;
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_No_Border);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);
//        int customViewId = getArguments().getInt(ARG_CUSTOM_VIEW);
        String positiveBtn = getArguments().getString(ARG_POSITIVE);
        String negativeBtn = getArguments().getString(ARG_NEGATIVE);

        View contentView = View.inflate(getActivity(), R.layout.common_dialog, null);
        mTitleTv = (TextView) contentView.findViewById(R.id.dialog_title_tv);
        mMessageTv = (TextView) contentView.findViewById(R.id.dialog_message_tv);
        mContainerLay = (LinearLayout) contentView.findViewById(R.id.dialog_container_ll);
        mPositiveBtn = (Button) contentView.findViewById(R.id.dialog_positive_btn);
        mNegativeBtn = (Button) contentView.findViewById(R.id.dialog_negative_btn);
        mCloseIv = contentView.findViewById(R.id.dialog_close_iv);

        if (StringUtils.isEmpty(title)) {
            mTitleTv.setVisibility(View.GONE);
        } else {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(title);
        }

        if (StringUtils.isEmpty(message)) {
            mMessageTv.setVisibility(View.GONE);
        } else {
            mMessageTv.setVisibility(View.VISIBLE);
            mMessageTv.setText(message);
        }

        if (mCustomView != null) {
            if (mCustomView.getParent() != null) {    //fix bug dialog多次显示 异常崩溃
                ((ViewGroup) mCustomView.getParent()).removeView(mCustomView);
            }
            mContainerLay.setVisibility(View.VISIBLE);
            mContainerLay.addView(mCustomView);
        } else {
            mContainerLay.setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(positiveBtn)) {
            mPositiveBtn.setVisibility(View.GONE);
        } else {
            mPositiveBtn.setVisibility(View.VISIBLE);
            mPositiveBtn.setText(positiveBtn);
            mPositiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onDialogPositiveClick(CommonDialogFragment.this);
                    }
                }
            });
        }

        if (StringUtils.isEmpty(negativeBtn)) {
            if (StringUtils.isEmpty(message)) {
                mNegativeBtn.setVisibility(View.GONE);
            } else {
                mNegativeBtn.setVisibility(View.VISIBLE);
                mNegativeBtn.setText(message);
                mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onDialogNegativeClick(CommonDialogFragment.this);
                        }
                    }
                });
            }
        } else {
            mNegativeBtn.setVisibility(View.VISIBLE);
            mNegativeBtn.setText(negativeBtn);
            mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDialogNegativeClick(CommonDialogFragment.this);
                    }
                }
            });
        }

        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return contentView;
    }
}
