package com.yunxingzh.wireless.mview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yunxingzh.wireless.R;

/**
 * Created by stephen on 2016/12/21.
 * 网络异常界面
 */

public class NetErrorLayout {

    private Context context;
    private OnNetErrorClickListener onNetErrorClickListener;

    public NetErrorLayout(Context context) {
        this.context = context;
    }

    public View netErrorLay(int color) {
        View netView = LayoutInflater.from(context).inflate(R.layout.wifi_closed, null);
        netView.setBackgroundColor(color);
        TextView openTv = (TextView) netView.findViewById(R.id.net_open_tv);
        TextView contentTv = (TextView) netView.findViewById(R.id.net_content_tv);
        TextView refreshBtn = (TextView) netView.findViewById(R.id.open_wifi_btn);
        openTv.setVisibility(View.GONE);
        contentTv.setText(R.string.network_error);
        refreshBtn.setText(R.string.refresh_net);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNetErrorClickListener.netErrorClick();
            }
        });
        return netView;
    }

    public interface OnNetErrorClickListener {
        void netErrorClick();
    }

    public void setOnNetErrorClickListener(OnNetErrorClickListener onNetErrorClickListener) {
        this.onNetErrorClickListener = onNetErrorClickListener;
    }
}
