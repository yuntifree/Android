package com.yunxingzh.wireless.wifi;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.List;

public class WifiUtility{

    public static String removeDoubleQuotes(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }

        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }


    public static WifiInfo getWifiInfo(WifiManager mgr){
        WifiInfo wifiInfo = null;

        try {
            wifiInfo = mgr.getConnectionInfo();
        } catch (Throwable e) {
            return null;
        }

        if (wifiInfo == null || wifiInfo.getNetworkId() == -1 || "00:00:00:00:00:00".equals(wifiInfo.getBSSID())) {
            return null;
        }

        String ssId = wifiInfo.getSSID();
        if (TextUtils.isEmpty(ssId) || "<unknown ssid>".contains(ssId) || "0x".contains(ssId))
            return null;

        return wifiInfo;
    }

    public static NetworkInfo.DetailedState getDetailedState(ConnectivityManager connMgr){
        try {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo.getDetailedState();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void removeConfigure(WifiManager mgr, String ssid){
        if (ssid == null) return;
        List<WifiConfiguration> list = null;
        try {
            list = mgr.getConfiguredNetworks();
            if (list != null) {
                for (WifiConfiguration w : list) {
                    if (w.SSID == null) {
                        continue;
                    }
                    if (removeDoubleQuotes(w.SSID).equals(ssid)) {
                        mgr.removeNetwork(w.networkId);
                        mgr.saveConfiguration();
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}