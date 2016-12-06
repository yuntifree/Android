package com.yunxingzh.wireless.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.yunxingzh.wireless.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WifiMachine {

    private static String TAG = "WifiMachine";

    private Context mContext;
    private WifiState mState;
    private HashMap<String, AccessPoint> mAccessPoints;
    private IConnectWorker mCurrentWorker;
    private ArrayList<IWifiListener> mListeners;

    private AccessPoint mPendingPoint;

    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;

    private Subscription wifiStateSubscription;
    private Subscription wifiListSubscription;
    private Subscription wifiSignalSubscription;

    public WifiMachine(Context context){
        mContext = context;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiStateSubscription = WifiObservable.observeWifiState(context)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<WifiState>() {
                    @Override
                    public void call(final WifiState state) {
                        if(state == WifiState.AUTH_ERROR){
                            systemAuthError();
                        } else {
                            systemStateChanged(state);
                        }
                    }
                });
        wifiListSubscription = WifiObservable.observeWifiList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<List<ScanResult>>() {
                    @Override
                    public void call(List<ScanResult> scanResults) {
                        systemListChanged(scanResults);
                    }
                });
        wifiSignalSubscription = WifiObservable.observeWifiSignal(context)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer rssi) {
                        systemRssiChanged(rssi);
                    }
                });
        //Get current wifi state
        mState = getSysState();
        //Get current wifi list
        mAccessPoints = new HashMap<String, AccessPoint>();
        try {
            systemListChanged(mWifiManager.getScanResults());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void release(){
        if (wifiStateSubscription != null && !wifiStateSubscription.isUnsubscribed()) {
            wifiStateSubscription.unsubscribe();
        }
        if (wifiListSubscription != null && !wifiListSubscription.isUnsubscribed()){
            wifiListSubscription.unsubscribe();
        }
        if (wifiSignalSubscription != null && !wifiSignalSubscription.isUnsubscribed()){
            wifiSignalSubscription.unsubscribe();
        }
    }

    public boolean isEnabled(){
        try{
            return mWifiManager.isWifiEnabled();
        } catch (Throwable e){
            return false;
        }
    }

    public boolean isConnected(){
        try{
            if(mWifiManager.isWifiEnabled()){
                NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                return networkInfo.isConnected();
            }
        } catch(Throwable e){

        }
        return false;
    }

    public boolean setEnable(boolean bool){
        return mWifiManager.setWifiEnabled(bool);
    }

    public WifiState getState() {
        return mState;
    }

    public HashMap<String, AccessPoint> getListMap(){
        return mAccessPoints;
    }

    public List<AccessPoint> getList(){
        List<AccessPoint> aps = new ArrayList<AccessPoint>();

        for (String ssid : mAccessPoints.keySet()) {
            if(!TextUtils.isEmpty(ssid))
                aps.add(mAccessPoints.get(ssid));
        }

        Collections.sort(aps, AccessPoint.Comparator());
        return aps;
    }

    public IConnectWorker getCurrentWorker(){
        return mCurrentWorker;
    }

    //sync system and app state
    public void checkState(){
        WifiState sysState = getSysState();
        LogUtils.d(TAG, "checkState: QU(" + String.valueOf(mState) + ") VS SYS(" + sysState + ")");
        if(sysState == mState) return;

        if(sysState == WifiState.DISCONNECTED || sysState == WifiState.IDLE){
            setActionState(WifiState.IDLE);
        } else if(sysState == WifiState.DISABLED){
            setActionState(sysState);
        } else if(sysState == WifiState.CONNECTING || sysState == WifiState.CONNECTING_AUTH || sysState == WifiState.CONNECTING_IPADDR){
            setActionState(sysState);
        } else if(sysState == WifiState.CONNECTED){
            if(mState != WifiState.CHECKING && mState != WifiState.LOGINING && mState != WifiState.OFFLINEING){
                doSystemConnected();
            }
        }
    }

    public WifiState getSysState() {
        if (!isEnabled()) {
            return WifiState.DISABLED;
        }
        if (isConnected()) {
            return WifiState.CONNECTED;
        }

        WifiInfo info = WifiUtility.getWifiInfo(mWifiManager);
        if (info == null) {
            return WifiState.IDLE;
        }

        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(info.getSupplicantState());

        switch (state) {
            case DISCONNECTING:
            case DISCONNECTED:
                return WifiState.DISCONNECTED;
            case AUTHENTICATING:
                return WifiState.CONNECTING_AUTH;
            case OBTAINING_IPADDR:
                return WifiState.CONNECTING_IPADDR;
            case CONNECTING:
                return WifiState.CONNECTING;
            case CONNECTED:
                return WifiState.CONNECTED;
        }
        return WifiState.IDLE;
    }

    public void connect(AccessPoint ap){
        if (ap == null) {
            LogUtils.d(TAG, "connect failed: ap is null");
            return;
        }

        if (mCurrentWorker != null) {
            mPendingPoint = ap;
            LogUtils.d(TAG, "last connected need offline: " + mCurrentWorker.getAccessPoint().toString());
            setActionState(WifiState.WAITING_DISCONNECT_LAST);
            mCurrentWorker.offline()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.immediate())
                    .timeout(1, TimeUnit.SECONDS)
                    .subscribe(offlineObserver);
        } else {
            mCurrentWorker = createConnectWoker(ap);
            LogUtils.d(TAG, "connect " + ap.toString() + " and waiting cloud confirm");
            setActionState(WifiState.WAITING_SERVER_CONFIRM);
            mCurrentWorker.confirm()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.immediate())
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribe(confirmedObserver);
            mPendingPoint = null;
        }
    }

    public void disconnect(){
        if (mCurrentWorker == null) return;
        setActionState(WifiState.OFFLINEING);
        mCurrentWorker.offline()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .timeout(1, TimeUnit.SECONDS)
                .subscribe(offlineObserver);
    }

    public void scan(){
        mWifiManager.startScan();
    }

    public void ignore(String ssid) {
        WifiUtility.removeConfigure(mWifiManager, ssid);
        if(mAccessPoints.containsKey(ssid)){
            AccessPoint ap = mAccessPoints.get(ssid);
            if(ap != null) ap.setConfig(null);
        }
    }

    public void registerWiFiListener(IWifiListener wiFiListener) {
        if (mListeners == null) {
            mListeners = new ArrayList<IWifiListener>();
        }

        if (!mListeners.contains(wiFiListener)) {
            mListeners.add(wiFiListener);
        }
    }

    public void unregisterWiFiListener(IWifiListener wiFiListener) {
        if (mListeners != null) {
            mListeners.remove(wiFiListener);
        }
    }



    private Observer<WorkResult> offlineObserver = new Observer<WorkResult>() {
        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) {
            LogUtils.d(TAG, "offline error: " + e.getMessage());
        }

        @Override
        public void onNext(WorkResult workResult) {
            LogUtils.d(TAG, "offline success!");
            if (mState != WifiState.WAITING_DISCONNECT_LAST) {
                setActionState(WifiState.IDLE);
                destroy();
                mWifiManager.disconnect();
            } else {
                mWifiManager.disconnect();
                doSystemDisconnected();
            }
        }
    };

    private Observer<WorkResult> confirmedObserver = new Observer<WorkResult>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            LogUtils.d(TAG, "confirm error: " + e.getMessage());
        }

        @Override
        public void onNext(WorkResult workResult) {
            LogUtils.d(TAG, "confirm success!");
            if(mState == WifiState.DISABLED || mCurrentWorker == null) return;
            if (workResult.success()) {
                setActionState(WifiState.CONNECTING);
                mCurrentWorker.connect()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.immediate())
                    .timeout(20, TimeUnit.SECONDS)
                    .subscribe(connectedObserver);
            } else {
                setActionState(WifiState.IDLE);
                AccessPoint ap = mCurrentWorker.getAccessPoint();
                //TODO: handler error result
                destroy();
            }
        }
    };

    private Observer<WorkResult> connectedObserver = new Observer<WorkResult>() {
        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) {
            LogUtils.d(TAG, "connect error: " + e.getMessage());
        }

        @Override
        public void onNext(WorkResult workResult) {
            if(!workResult.success()){
                setActionState(WifiState.IDLE);
                AccessPoint ap = mCurrentWorker.getAccessPoint();
                //TODO: handler error result
                destroy();
            }
        }
    };

    private Observer<WorkResult> loginedObserver = new Observer<WorkResult>() {
        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) {
            LogUtils.d(TAG, "login error: " + e.getMessage());
        }

        @Override
        public void onNext(WorkResult workResult) {
            LogUtils.d(TAG, "login success");
            if(mState == WifiState.DISABLED || mCurrentWorker == null) return;
            if (workResult.success()) {
                setActionState(WifiState.CHECKING);

                mCurrentWorker.check()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.immediate())
                    .timeout(5, TimeUnit.SECONDS)
                    .subscribe(checkedObserver);
            } else {
                setActionState(WifiState.CONNECTED);
                AccessPoint ap = mCurrentWorker.getAccessPoint();
                //TODO: handler error result
            }
        }
    };

    private Observer<WorkResult> checkedObserver = new Observer<WorkResult>() {
        @Override
        public void onCompleted() { }

        @Override
        public void onError(Throwable e) {
            LogUtils.d(TAG, "check error: " + e.getMessage());
        }

        @Override
        public void onNext(WorkResult workResult) {
            LogUtils.d(TAG, "check success");
            if(mState == WifiState.DISABLED || mCurrentWorker == null) return;
            setActionState(WifiState.CONNECTED);
        }
    };

    private void systemAuthError(){
        AccessPoint ap = null;
        if(mCurrentWorker != null) ap = mCurrentWorker.getAccessPoint();
        if(ap != null) {
            ignore(ap.ssid);
            dispatchAuthError(ap);
        }
    }

    private void systemStateChanged(WifiState state){
        LogUtils.d(TAG, "systemStateChanged: " + state + " with WifiState: " + mState);
        if (mState == WifiState.CONNECTING && state == WifiState.IDLE) {
            LogUtils.d(TAG, "when connecting, drop idle event");
            return;
        }
        if ((mState == WifiState.LOGINING || mState == WifiState.CHECKING)
                && (state == WifiState.CONNECTING || state == WifiState.CONNECTING_AUTH || state == WifiState.CONNECTING_IPADDR || state == WifiState.CONNECTED)) {
            LogUtils.d(TAG, "when logining or checking, drop connected and connecting event");
            return;
        }

        if(mState == WifiState.CONNECTED && (state == WifiState.CONNECTING_AUTH || state == WifiState.CONNECTING_IPADDR)){
            return;
        }

        if (mState == WifiState.CONNECTED && state == WifiState.CONNECTED) {
            if (mCurrentWorker == null) {
                doSystemConnected();
            }else {
                LogUtils.d(TAG, "when connected and saved, drop connected event");
            }
            return;
        }

        if (state == WifiState.CONNECTED) {
            doSystemConnected();
        } else if (state == WifiState.DISCONNECTED) {
            doSystemDisconnected();
        } else if(state == WifiState.IDLE){
            if(mState == WifiState.CONNECTED){
                destroy();
            }
        } else if(state == WifiState.DISABLED){
            if (mCurrentWorker != null) {
                mCurrentWorker.destory();
            }
            mCurrentWorker = null;
            mPendingPoint = null;
            setActionState(WifiState.DISABLED);
        } else {
            if (mCurrentWorker == null) {
                mCurrentWorker = createCurrentWorker();
            }
            setActionState(state);
        }
    }

    private void systemListChanged(List<ScanResult> scanResults){
//        LogUtils.d(TAG, "systemListChanged");
        mAccessPoints.clear();
        if(scanResults != null){
            for(ScanResult scanResult : scanResults){
                AccessPoint ap = new AccessPoint(scanResult);
                if(ap != null)
                    mAccessPoints.put(ap.ssid, ap);
            }
        }

        List<WifiConfiguration> configurations = mWifiManager.getConfiguredNetworks();
        if(configurations != null){
            for(WifiConfiguration conf : configurations){
                AccessPoint ap = mAccessPoints.get(WifiUtility.removeDoubleQuotes(conf.SSID));
                if(ap != null) ap.setConfig(conf);
            }
        }

        dispatchListChanged(getList());
    }

    private void systemRssiChanged(Integer rssi){
//        LogUtils.d(TAG, "systemRssiChanged");
        dispatchRSSIChanged(rssi);
    }



    private void setActionState(WifiState state){
        if (state == mState && state != mState.CONNECTED) return;

        LogUtils.d(TAG, "setActionState：" + String.valueOf(mState) + " ->" + String.valueOf(state));
        WifiState old_state = mState;
        mState = state;

        dispatchStateChanged(mState, old_state);
    }

    private void doSystemConnected() {
        if (mCurrentWorker == null) {
            mCurrentWorker = createCurrentWorker();
        } else {
            AccessPoint ap = mCurrentWorker.getAccessPoint();
            WifiInfo info = WifiUtility.getWifiInfo(mWifiManager);
            if (info != null && !ap.ssid.equals(WifiUtility.removeDoubleQuotes(info.getSSID()))) {
                destroy();
                mCurrentWorker = createCurrentWorker();
            }
        }

        LogUtils.d(TAG, "doSystemConnected " + mCurrentWorker);
        if (mCurrentWorker != null) {
            setActionState(WifiState.LOGINING);
            mCurrentWorker.login()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.immediate())
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribe(loginedObserver);
        }
    }

    private void doSystemDisconnected(){
        LogUtils.d(TAG, "doSystemDisconnected " + mState);
        if (mState == WifiState.WAITING_DISCONNECT_LAST) {
            if (mCurrentWorker != null) {
                mCurrentWorker.destory();
                mCurrentWorker = null;
            }

            if (mPendingPoint != null) {
                mCurrentWorker = createConnectWoker(mPendingPoint);
                LogUtils.d(TAG, "new connect ap: " + mPendingPoint + " mCurrentStrategy: " + mCurrentWorker);
                setActionState(WifiState.WAITING_SERVER_CONFIRM);
                mCurrentWorker.confirm()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .timeout(10, TimeUnit.SECONDS)
                        .subscribe(confirmedObserver);
                mPendingPoint = null;
            } else {
                setActionState(WifiState.IDLE);
            }
        } else {
            destroy();
        }
    }

    private void destroy(){
        if(mCurrentWorker != null){
            mCurrentWorker.destory();
        }

        mCurrentWorker = null;
        mPendingPoint = null;
        setActionState(WifiState.IDLE);
    }

    //create worker from system was connecting or had connected
    private IConnectWorker createCurrentWorker() {
        if(getSysState() == WifiState.IDLE) return null;

        WifiInfo info = WifiUtility.getWifiInfo(mWifiManager);
        NetworkInfo.DetailedState detailed = WifiUtility.getDetailedState(mConnectivityManager);
        if (info == null || detailed == null) return null;

        if(mAccessPoints.isEmpty()){
            try {
                systemListChanged(mWifiManager.getScanResults());
            } catch (Throwable e) {

            }
        }

        AccessPoint ap = mAccessPoints.get(WifiUtility.removeDoubleQuotes(info.getSSID()));
        if(ap != null){
            ap.wifiInfo = info;
            ap.detailedState = detailed;
            return ConnectWorkRouter.get(mContext, mWifiManager, ap);
        }

        return null;
    }

    private IConnectWorker createConnectWoker(AccessPoint ap){
        return ConnectWorkRouter.get(mContext, mWifiManager, ap);
    }

    private void dispatchRSSIChanged(int rssi){
        if (mListeners != null) {
            ArrayList<IWifiListener> listeners = new ArrayList<IWifiListener>(mListeners);
            for (IWifiListener mListener : listeners) {
                mListener.onRSSIChanged(rssi);
            }
        }
    }

    private void dispatchAuthError(AccessPoint ap){
        if (mListeners != null) {
            ArrayList<IWifiListener> listeners = new ArrayList<IWifiListener>(mListeners);
            for (IWifiListener mListener : listeners) {
                mListener.onAuthError(ap);
            }
        }
    }

    private void dispatchListChanged(List<AccessPoint> aps){
        if (mListeners != null) {
            ArrayList<IWifiListener> listeners = new ArrayList<IWifiListener>(mListeners);
            for (IWifiListener mListener : listeners) {
                mListener.onListChanged(aps);
            }
        }
    }

    private void dispatchStateChanged(WifiState new_state, WifiState old_state){
        if (mListeners != null) {
            ArrayList<IWifiListener> listeners = new ArrayList<IWifiListener>(mListeners);
            for (IWifiListener mListener : listeners) {
                mListener.onStateChanged(new_state, old_state);
            }
        }
    }
}