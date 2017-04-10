package com.yunxingzh.wireless.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;

import com.yunxingzh.wireless.utils.LogUtils;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class WifiObservable{

    private static final String TAG = "WifiStateObservable";

    public static Observable<WifiState> observeWifiState(final Context ctx){
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        return Observable.create(new Observable.OnSubscribe<WifiState>(){
            @Override
            public void call(final Subscriber<? super WifiState> subscriber){
                final BroadcastReceiver receiver = new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context ctx, Intent intent){
                        try{
                            String action = intent.getAction();
                            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                                if(state == WifiManager.WIFI_STATE_ENABLED || state == WifiManager.WIFI_STATE_ENABLING){
                                    subscriber.onNext(WifiState.IDLE);
                                } else if(state == WifiManager.WIFI_STATE_DISABLED || state == WifiManager.WIFI_STATE_DISABLING){
                                    subscriber.onNext(WifiState.DISABLED);
                                }

                            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)){
                                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                    NetworkInfo.State state = networkInfo.getState();
                                    if (state == NetworkInfo.State.CONNECTED) {
                                        subscriber.onNext(WifiState.CONNECTED);
                                    } else if(state == NetworkInfo.State.DISCONNECTED || state == NetworkInfo.State.DISCONNECTING){
                                        subscriber.onNext(WifiState.IDLE);
                                    }
                                }
                            } else if(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)){
                                SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                                if (state != null) {
                                    if (state == SupplicantState.INACTIVE || state == SupplicantState.INVALID
                                            || state == SupplicantState.UNINITIALIZED) {
                                        return;
                                    }

                                    if (state == SupplicantState.FOUR_WAY_HANDSHAKE
                                            || state == SupplicantState.GROUP_HANDSHAKE) {
                                        // mHandler.removeMessages(MSG_WAIT_HANGS_GROUP);
                                    }

                                    NetworkInfo.DetailedState dstate = WifiInfo.getDetailedStateOf(state);
                                    switch (dstate) {
                                        case CONNECTING:
                                            subscriber.onNext(WifiState.CONNECTING);
                                            break;
                                        case AUTHENTICATING:
                                            subscriber.onNext(WifiState.CONNECTING_AUTH);
                                            break;
                                        case OBTAINING_IPADDR:
                                            subscriber.onNext(WifiState.CONNECTING_IPADDR);
                                            break;
                                        case DISCONNECTED:
                                        case DISCONNECTING:
                                            if (error == WifiManager.ERROR_AUTHENTICATING) {
                                                subscriber.onNext(WifiState.AUTH_ERROR);
                                            }
                                            subscriber.onNext(WifiState.DISCONNECTED);
                                            break;
                                        case CONNECTED:
                                            subscriber.onNext(WifiState.CONNECTED);
                                            break;
                                    }
                                }
                            }
                        } catch(Throwable e){

                        }
                    }
                };

                ctx.registerReceiver(receiver, filter);

                subscriber.add(unsubscribeInUiThread(new Action0(){
                    @Override
                    public void call(){
                        ctx.unregisterReceiver(receiver);
                    }
                }));
            }
        });
    }


    public static Observable<List<ScanResult>> observeWifiList(final Context ctx){
        final WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        return Observable.create(new Observable.OnSubscribe<List<ScanResult>>(){
            @Override
            public void call(final Subscriber<? super List<ScanResult>> subscriber){
                final BroadcastReceiver receiver = new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context ctx, Intent intent){
                        LogUtils.e("lsd", "systemListCall");
                        subscriber.onNext(wifiManager.getScanResults());
                    }
                };

                ctx.registerReceiver(receiver, filter);

                subscriber.add(unsubscribeInUiThread(new Action0(){
                    @Override
                    public void call(){
                        ctx.unregisterReceiver(receiver);
                    }
                }));
            }
        });
    }


    public static Observable<Integer> observeWifiSignal(final Context ctx){
        final WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        return Observable.create(new Observable.OnSubscribe<Integer>(){

            @Override
            public void call(final Subscriber<? super Integer> subscriber){
                final BroadcastReceiver receiver = new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context ctx, Intent intent){
                        int rssi = 0;
                        WifiInfo info = wifiManager.getConnectionInfo();
                        if(info != null){
                            rssi = info.getRssi();
                        }
                        subscriber.onNext(rssi);
                    }
                };

                ctx.registerReceiver(receiver, filter);

                subscriber.add(unsubscribeInUiThread(new Action0(){
                    @Override
                    public void call(){
                        ctx.unregisterReceiver(receiver);
                    }
                }));
            }

        }).defaultIfEmpty(0);
    }

    private static Subscription unsubscribeInUiThread(final Action0 unsubscribe) {
        return Subscriptions.create(new Action0() {

            @Override
            public void call() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    unsubscribe.call();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Action0() {
                        @Override
                        public void call() {
                            unsubscribe.call();
                            inner.unsubscribe();
                        }
                    });
                }
            }

        });
    }

}