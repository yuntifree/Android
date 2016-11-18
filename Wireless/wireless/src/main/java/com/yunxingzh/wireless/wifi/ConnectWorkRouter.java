package com.yunxingzh.wireless.wifi;


import android.content.Context;
import android.net.wifi.WifiManager;

public class ConnectWorkRouter {

    public static IConnectWorker get(Context ctx, WifiManager mgr, AccessPoint ap){
        return new DefaultConnectWorker(ctx, mgr, ap);
    }
}
