package com.yunxingzh.wireless.service;

import com.yunxingzh.wireless.wifi.AccessPoint;

interface IFWServiceCallback {
    void onStateChanged(int new_state, int old_state);
    void onListChanged(inout List<AccessPoint> accessPoints);
    void onRSSIChanged(int rssi);
    void onAuthError(inout AccessPoint ap);
}
