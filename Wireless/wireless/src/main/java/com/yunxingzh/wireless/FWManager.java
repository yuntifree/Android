package com.yunxingzh.wireless;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.utils.LogUtils;
import com.yunxingzh.wireless.service.FWService;
import com.yunxingzh.wireless.service.IFWService;
import com.yunxingzh.wireless.service.IFWServiceCallback;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FWManager {
    private static final String TAG = "FWManager";

    private static FWManager instance;

    public static FWManager init(Context context) {
        if (instance == null) {
            instance = new FWManager(context);
        }
        return instance;
    }

    public static FWManager getInstance() {
        return instance;
    }

    private class RestartServiceRunnable implements Runnable {
        private Context mContext;

        public RestartServiceRunnable(Context context) {
            mContext = context;
        }

        @Override
        public void run() {
            LogUtils.d(TAG, "rebind service");
            try {
                mContext.startService(getServiceIntent(mContext));
                bindService(mContext);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private Intent getServiceIntent(Context context) {
        return new Intent(context, FWService.class);
    }

    public boolean isServiceBind() {
        return mService != null;
    }

    public void bindService(Context context) {
        try {
            context.bindService(getServiceIntent(context), sServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private IFWService mService;
    private RestartServiceRunnable sRestartServiceRunnable;

    private Context mContext;

    private ServiceConnection sServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IFWService.Stub.asInterface(service);
            onAttached();
            LogUtils.d(TAG, "service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d(TAG, "service disconnected");
            mService = null;
            mHandler.removeCallbacks(sRestartServiceRunnable);
            mHandler.post(sRestartServiceRunnable);
        }
    };

    public interface BinderObserver {
        public void onAttached();
    }

    private ArrayList<BinderObserver> mBinderObservers = new ArrayList<BinderObserver>();

    public void addBinderObserver(BinderObserver binderObserver) {
        if (!mBinderObservers.contains(binderObserver)) {
            mBinderObservers.add(binderObserver);
            if (mService != null) {
                binderObserver.onAttached();
            }
        }
    }

    public void removeBinderObserver(BinderObserver binderObserver) {
        if (!mBinderObservers.contains(binderObserver)) {
            mBinderObservers.remove(binderObserver);
        }
    }

    public interface WifiObserver {
        public void onStateChanged(WifiState new_state, WifiState old_state);

        public void onListChanged(List<AccessPoint> accessPoints);

        public void onRSSIChanged(int rssi);

        public void onAuthError(AccessPoint ap);
    }

    private ArrayList<WifiObserver> mWifiObservers = new ArrayList<WifiObserver>();

    public void addWifiObserver(WifiObserver wifiObserver) {
        if (!mWifiObservers.contains(wifiObserver)) {
            mWifiObservers.add(wifiObserver);
        }
    }

    public void removeWifiObserver(WifiObserver wifiObserver) {
        if (mWifiObservers.contains(wifiObserver)) {
            mWifiObservers.remove(wifiObserver);
        }
    }

    FWManager(Context context) {
        if (sRestartServiceRunnable == null)
            sRestartServiceRunnable = new RestartServiceRunnable(context);
    }


    private void onAttached() {
        if (!MyApplication.isUIApplication(mContext))
            return;

        if (mService == null)
            return;

        try {
            mService.registerCallback(mFWServiceCallbackStub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (BinderObserver listener : mBinderObservers) {
            listener.onAttached();
        }
    }


    public void connect(AccessPoint accessPoint) {
        if (mService == null) {
            LogUtils.w(TAG, "connect when service is null");
            return;
        }
        if (accessPoint == null) {
            LogUtils.w(TAG, "connect when accesspoint is null");
            return;
        }

        try {
            mService.connect(accessPoint);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (mService == null) {
            LogUtils.w(TAG, "disconnect when service is null");
            return;
        }

        try {
            mService.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void scan() {
        if (mService == null) {
            LogUtils.w(TAG, "scan when service is null");
            return;
        }

        try {
            mService.scan();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void checkState() {
        if (mService == null) {
            LogUtils.w(TAG, "checkState when service is null");
            return;
        }

        try {
            mService.checkState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean setEnabled(boolean enable) {
        if (mService == null) {
            LogUtils.w(TAG, "setEnabled when service is null");
            return false;
        }

        try {
            return mService.setEnabled(enable);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AccessPoint> getList() {
        if (mService == null) {
            LogUtils.w(TAG, "getList when service is null");
            return Collections.emptyList();
        }

        try {
            return mService.getList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public AccessPoint getCurrent() {
        if (mService == null) {
            LogUtils.w(TAG, "getList when service is null");
            return null;
        }

        try {
            return mService.getCurrent();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WifiState getState() {
        if (mService == null) {
            LogUtils.w(TAG, "getState when service is null");
            return WifiState.UNKOWN;
        }

        try {
            return WifiState.parse(mService.getState());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return WifiState.UNKOWN;
    }

    public boolean isEnabled() {
        if (mService == null) {
            LogUtils.w(TAG, "isEnabled when service is null");
            return true;
        }

        try {
            return mService.isEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void ignore(String ssid) {
        if (mService == null) {
            LogUtils.w(TAG, "ingore when service is null");
            return;
        }

        try {
            mService.ignore(ssid);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private IFWServiceCallback.Stub mFWServiceCallbackStub = new IFWServiceCallback.Stub() {

        @Override
        public void onStateChanged(int new_state, int old_state) throws RemoteException {
            mHandler.removeMessages(MSG_STATE_CHANGE);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_STATE_CHANGE, new_state, old_state));
        }

        @Override
        public void onListChanged(List<AccessPoint> accessPoints) throws RemoteException {
            mHandler.removeMessages(MSG_LIST_CHANGE);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_LIST_CHANGE, accessPoints));
        }

        @Override
        public void onRSSIChanged(int rssi) throws RemoteException {
            mHandler.removeMessages(MSG_RSSI_CHANGED);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RSSI_CHANGED, rssi));
        }

        @Override
        public void onAuthError(AccessPoint ap) throws RemoteException {
            mHandler.removeMessages(MSG_AUTH_ERROR);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_AUTH_ERROR, ap));
        }
    };

    private static final int MSG_STATE_CHANGE = 0;
    private static final int MSG_LIST_CHANGE = 1;
    private static final int MSG_RSSI_CHANGED = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE_CHANGE:
                    dispatchStateChanged(msg.arg1, msg.arg2);
                    break;
                case MSG_LIST_CHANGE:
                    dispatchListChanged((List<AccessPoint>) msg.obj);
                    break;
                case MSG_RSSI_CHANGED:
                    dispatchRSSIChanged(msg.arg1);
                    break;
                case MSG_AUTH_ERROR:
                    dispatchAuthError((AccessPoint) msg.obj);
                    break;
            }
        }
    };

    private void dispatchRSSIChanged(int rssi) {
        if (mWifiObservers != null) {
            for (WifiObserver listener : mWifiObservers) {
                listener.onRSSIChanged(rssi);
            }
        }
    }

    private void dispatchAuthError(AccessPoint ap) {
        if (mWifiObservers != null) {
            for (WifiObserver listener : mWifiObservers) {
                listener.onAuthError(ap);
            }
        }
    }

    private void dispatchListChanged(List<AccessPoint> accessPoints) {
        if (mWifiObservers != null) {
            for (WifiObserver listener : mWifiObservers) {
                listener.onListChanged(accessPoints);
            }
        }
    }

    private void dispatchStateChanged(int new_state, int old_state) {
        if (mWifiObservers != null) {
            for (WifiObserver listener : mWifiObservers) {
                listener.onStateChanged(WifiState.parse(new_state), WifiState.parse(old_state));
            }
        }
    }
}
