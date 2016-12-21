package com.yunxingzh.wireless.mview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

/**
 * Created by stephen on 2016/12/21.
 */

public class NetErrorLayout extends View {

    private static Context context;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static LinearLayout parentLay;

    public NetErrorLayout(Context context, SwipeRefreshLayout swipeRefreshLayout, LinearLayout parentLay) {
        super(context);
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.parentLay = parentLay;
    }

    public static void netErrorLay() {
//        if (!NetUtils.isNetworkAvailable(context)) {
//            View netView = LayoutInflater.from(context).inflate(R.layout.wifi_closed, null);
//            swipeRefreshLayout.setVisibility(View.GONE);
//            parentLay.setVisibility(View.VISIBLE);
//            TextView openTv = (TextView) netView.findViewById(R.id.net_open_tv);
//            TextView contentTv = (TextView) netView.findViewById(R.id.net_content_tv);
//            TextView refreshBtn = (TextView) netView.findViewById(R.id.open_wifi_btn);
//            openTv.setVisibility(View.GONE);
//            contentTv.setText(R.string.network_error);
//            refreshBtn.setText(R.string.refresh_net);
//            parentLay.addView(netView);
//            refreshBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!NetUtils.isNetworkAvailable(context)) {
//                        ToastUtil.showMiddle(context, R.string.net_set);
//                    } else {
//                        parentLay.setVisibility(View.GONE);
//                        swipeRefreshLayout.setVisibility(View.VISIBLE);
//                        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
//                    }
//                }
//            });
//        }
    }
}
