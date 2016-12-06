package com.yunxingzh.wireless.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.yunxingzh.wireless.mvp.ui.utils.LogUtils;
import com.yunxingzh.wireless.wifi.AccessPoint;

import java.util.List;

public class FWService extends Service {

    private static final String TAG = "FWService";

    private FWServiceManager mWifiServiceManager;

    @Override
    public IBinder onBind(Intent intent) {
        return new FWServiceStub();
    }

    private class FWServiceStub extends IFWService.Stub{
        @Override
        public void connect(AccessPoint accessPoint) throws RemoteException {
            mWifiServiceManager.connect(accessPoint);
        }

        @Override
        public void disconnect() throws RemoteException {
            mWifiServiceManager.disconnect();
        }

        @Override
        public void scan() throws RemoteException {
            mWifiServiceManager.scan();
        }

        @Override
        public void checkState() throws RemoteException {
            mWifiServiceManager.checkState();
        }

        @Override
        public boolean setEnabled(boolean enable) throws RemoteException {
            return mWifiServiceManager.setEnabled(enable);
        }

        @Override
        public List<AccessPoint> getList() throws RemoteException {
            return mWifiServiceManager.getList();
        }

        @Override
        public AccessPoint getCurrent() throws RemoteException {
            return mWifiServiceManager.getCurrent();
        }

        @Override
        public int getState() throws RemoteException {
            return mWifiServiceManager.getState();
        }

        @Override
        public boolean isEnabled() throws RemoteException {
            return mWifiServiceManager.isEnabled();
        }

        @Override
        public void ignore(String ssid) throws RemoteException {
            mWifiServiceManager.ignore(ssid);
        }

        @Override
        public void registerCallback(IFWServiceCallback callback) throws RemoteException {
            mWifiServiceManager.registerCallback(callback);
        }

        @Override
        public void unregisterCallback(IFWServiceCallback callback) throws RemoteException {
            mWifiServiceManager.unregisterCallback(callback);
        }
    }

    @Override
    public void onCreate() {
        LogUtils.d(TAG, "onCreate");
        mWifiServiceManager = new FWServiceManager(this);
    }

    @Override
    public void onDestroy() {
        if (mWifiServiceManager != null) {
            mWifiServiceManager.dispose();
        }
        super.onDestroy();
    }
}
