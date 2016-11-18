package com.yunxingzh.wireless.service;

import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.service.IFWServiceCallback;

interface IFWService {

    void connect(inout AccessPoint accessPoint);

    void disconnect();

    void scan();

    void checkState();

    boolean setEnabled(boolean enable);

    List<AccessPoint> getList();

    int getState();

    AccessPoint getCurrent();

    boolean isEnabled();

    void ignore(String ssid);

    void registerCallback(IFWServiceCallback callback);

    void unregisterCallback(IFWServiceCallback callback);
}