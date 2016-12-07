package com.yunxingzh.wireless.service;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.IConnectWorker;
import com.yunxingzh.wireless.wifi.IWifiListener;
import com.yunxingzh.wireless.wifi.WifiMachine;
import com.yunxingzh.wireless.wifi.WifiState;

import java.util.ArrayList;
import java.util.List;

public class FWServiceManager {

    private static String TAG = "FWServiceManager";

    private Context mContext;

    private WifiMachine mMachine;

    private final byte[] mLock = new byte[1];
    private ArrayList<FWServiceCallback> mCallbacks = new ArrayList<FWServiceCallback>();
    private class FWServiceCallback implements Binder.DeathRecipient {

        private IFWServiceCallback binder;

        public FWServiceCallback(IFWServiceCallback callback) {
            this.binder = callback;
            try {
                callback.asBinder().linkToDeath(this, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void binderDied() {
            synchronized (mLock) {
                mCallbacks.remove(this);
            }
        }
    }

    public FWServiceManager(Context context){
        this.mContext = context;
        this.mMachine = new WifiMachine(context);
        this.mMachine.registerWiFiListener(wifiListener);
    }

    public void dispose(){
        this.mMachine.unregisterWiFiListener(wifiListener);
        this.mMachine.release();
    }

    public void connect(AccessPoint accessPoint){
        this.mMachine.connect(accessPoint);
    }

    public void disconnect(){
        this.mMachine.disconnect();
    }

    public void scan(){
        this.mMachine.scan();
    }

    public void checkState(){
        this.mMachine.checkState();
    }

    public boolean setEnabled(boolean enable){
        return this.mMachine.setEnable(enable);
    }

    public List<AccessPoint> getList(){
        return this.mMachine.getList();
    }

    public AccessPoint getCurrent(){
        IConnectWorker worker = this.mMachine.getCurrentWorker();
        if(worker != null){
            return worker.getAccessPoint();
        }
        return null;
    }

    public int getState(){
        return this.mMachine.getState().ordinal();
    }

    public boolean isEnabled(){
        return this.mMachine.isEnabled();
    }

    public void ignore(String ssid){
        this.mMachine.ignore(ssid);
    }

    public void registerCallback(IFWServiceCallback callback){
        synchronized (mLock) {
            if (getCallback(callback) == null) {
                mCallbacks.add(new FWServiceCallback(callback));
            }
        }
    }

    public void unregisterCallback(IFWServiceCallback callback){
        synchronized (mLock) {
            mCallbacks.remove(callback);
        }
    }

    private FWServiceCallback getCallback(IFWServiceCallback binder) {
        synchronized (mLock) {
            for (FWServiceCallback callback : mCallbacks) {
                if (callback.binder == binder) {
                    return callback;
                }
            }
        }

        return null;
    }

    private IWifiListener wifiListener = new IWifiListener() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            mHandler.removeMessages(MSG_STATE_CHANGE);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_STATE_CHANGE, new_state.ordinal(), old_state.ordinal()));
        }

        @Override
        public void onListChanged(List<AccessPoint> aps) {
            mHandler.removeMessages(MSG_LIST_CHANGE);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_LIST_CHANGE, aps));
        }

        @Override
        public void onRSSIChanged(int rssi) {
            mHandler.removeMessages(MSG_RSSI_CHANGED);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RSSI_CHANGED, rssi));
        }

        @Override
        public void onAuthError(AccessPoint ap) {
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
                    dispatchWiFiStateChanged(msg.arg1, msg.arg2);
                    break;
                case MSG_LIST_CHANGE:
                    dispatchWiFiScaned((List<AccessPoint>) msg.obj);
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

    private void dispatchWiFiStateChanged(int new_state, int old_state){
        synchronized (mLock) {
            for (FWServiceCallback callback : mCallbacks) {
                try {
                    callback.binder.onStateChanged(new_state, old_state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void dispatchWiFiScaned(List<AccessPoint> aps){
        synchronized (mLock) {
            for (FWServiceCallback callback : mCallbacks) {
                try {
                    callback.binder.onListChanged(aps);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void dispatchRSSIChanged(int rssi){
        synchronized (mLock) {
            for (FWServiceCallback callback : mCallbacks) {
                try {
                    callback.binder.onRSSIChanged(rssi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void dispatchAuthError(AccessPoint ap){
        synchronized (mLock) {
            for (FWServiceCallback callback : mCallbacks) {
                try {
                    callback.binder.onAuthError(ap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
