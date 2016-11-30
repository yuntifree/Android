package com.yunxingzh.wireless.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.yunxingzh.wireless.mvp.ui.fragment.WirelessFragment;
import com.yunxingzh.wireless.utility.NetUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

/**
 * Created by stephon on 2016/11/30.
 * 实时监听网络变化
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt evevt = WirelessFragment.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 接口回调传过去状态的类型
            boolean con = NetUtils.isWifi(context);
            evevt.onNetChange(con);
        }
    }

    public interface NetEvevt {
        void onNetChange(boolean netMobile);
    }
}
