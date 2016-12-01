package com.yunxingzh.wireless.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineNewsFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineVideoFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.ServiceFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.WirelessFragment;
import com.yunxingzh.wireless.utility.NetUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by stephon on 2016/11/30.
 * 实时监听网络变化
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt wirelessEvevt = WirelessFragment.wirelessEvevt;
//    public NetEvevt newsEvevt = HeadLineNewsFragment.newsEvevt;
//    public NetEvevt videoEvevts = HeadLineVideoFragment.videoEvevts;
//    public NetEvevt serviceEvevts = ServiceFragment.serviceEvevts;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 接口回调传过去状态的类型
            boolean con = NetUtils.isWifi(context);
            wirelessEvevt.onNetChange(con);
            EventBus.getDefault().post(new EventBusType(0,0,Constants.NET_CHANGED));
//            if (wirelessEvevt != null){
//                wirelessEvevt.onNetChange(con);
//            }
//            if (newsEvevt != null){
//                newsEvevt.onNetChange(con);
//            }
//            if (videoEvevts != null){
//                videoEvevts.onNetChange(con);
//            }
//            if (serviceEvevts != null){
//                serviceEvevts.onNetChange(con);
//            }
        }
    }

    public interface NetEvevt {
        void onNetChange(boolean netMobile);
    }
}
