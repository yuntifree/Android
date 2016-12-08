package com.yunxingzh.wireless.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.activity.MainActivity;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.IConnectWorker;
import com.yunxingzh.wireless.wifi.IWifiListener;
import com.yunxingzh.wireless.wifi.WifiMachine;
import com.yunxingzh.wireless.wifi.WifiState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class FWServiceManager {

    private static String TAG = "FWServiceManager";

    private Context mContext;

    private WifiMachine mMachine;

    private final byte[] mLock = new byte[1];

    // 通知栏
    private static int mDay = 0;
    private final static int MAX_NOTIFY_COUNT = 1;
    private static int mNotifyCount = 0;


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
        // 格式化初始时间
        mDay = getToday();
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
            // 判断通知栏
            for (AccessPoint ap : aps) {
                if (ap.ssid.equals(Constants.SSIDDEV)) {
                    checkNotify(ap);
                    break;
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

    /**
     * 发现指定WiFi时，判断通知栏逻辑
     *
     */
    private void checkNotify(AccessPoint ap) {
        // 当前连接的不是指定ap
        if (ap != getCurrent()) {
            int day = getToday();
            if (day > mDay) {
                mDay = day;
                mNotifyCount = 0;
            }
            mNotifyCount++;
            if (mNotifyCount <= MAX_NOTIFY_COUNT) {
                createInform();
            } else {
                // 防止持续++，导致溢出
                mNotifyCount = MAX_NOTIFY_COUNT + 1;
            }
        }
    }

    /**
     * 获取格式的日期
     * @return yyyyMMdd
     */
    private int getToday() {
        int ret = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            ret = Integer.parseInt(format.format(new Date()));
        } catch (RuntimeException e) {
        }
        return ret;
    }

    /**
     * 创建通知栏
     */
    public void createInform() {
        LogUtils.d(TAG, "~~inform~~ call createInform");
        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        //创建一个通知
        Notification noti = new Notification.Builder(mContext)
                .setContentTitle("发现东莞无线免费WiFi, 点击连接~")
                .setContentText("一键连接东莞无线~")
                .setSmallIcon(R.drawable.logo_small)
                .build();
        //用NotificationManager的notify方法通知用户生成标题栏消息通知
        NotificationManager nManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(100, noti);//id是应用中通知的唯一标识
        //如果拥有相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
    }
}
