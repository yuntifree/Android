package com.yunxingzh.wireless.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;

import rx.Observable;
import rx.Subscriber;

public class DefaultConnectWorker implements IConnectWorker {

    private static String TAG = "DefaultConnectWorker";

    private Context mContext;
    private WifiManager mWifiManager;
    private AccessPoint mAccessPoint;

    public DefaultConnectWorker(Context ctx, WifiManager mgr, AccessPoint ap){
        mContext = ctx;
        mWifiManager = mgr;
        mAccessPoint = ap;
    }

    @Override
    public Observable<WorkResult> confirm(){
        return Observable.create(new Observable.OnSubscribe<WorkResult>(){
            @Override
            public void call(final Subscriber<? super WorkResult> subscriber){
                subscriber.onNext(WorkResult.SUCCESS);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<WorkResult> connect(){
        return Observable.create(new Observable.OnSubscribe<WorkResult>(){
            @Override
            public void call(final Subscriber<? super WorkResult> subscriber){
                if(mAccessPoint == null){
                    subscriber.onNext(new WorkResult(1, "ap was null"));
                    subscriber.onCompleted();
                    return;
                }
                if(WifiConnecter.connectToAccessPoint(mContext, mWifiManager, mAccessPoint)){
                    subscriber.onNext(WorkResult.SUCCESS);
                } else {
                    subscriber.onNext(new WorkResult(1, "ap config was error"));
                }
                subscriber.onCompleted();

            }
        });
    }

    @Override
    public Observable<WorkResult> login(){
        return Observable.create(new Observable.OnSubscribe<WorkResult>(){
            @Override
            public void call(final Subscriber<? super WorkResult> subscriber){
                subscriber.onNext(WorkResult.SUCCESS);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<WorkResult> check(){
        return Observable.create(new Observable.OnSubscribe<WorkResult>(){
            @Override
             public void call(final Subscriber<? super WorkResult> subscriber){
                subscriber.onNext(WorkResult.SUCCESS);
                subscriber.onCompleted();
                //TODO: security check
            }
        });
    }

    @Override
    public Observable<WorkResult> offline(){
        return Observable.create(new Observable.OnSubscribe<WorkResult>(){
            @Override
            public void call(final Subscriber<? super WorkResult> subscriber){
                subscriber.onNext(WorkResult.SUCCESS);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void destory(){

    }

    @Override
    public AccessPoint getAccessPoint() {
        return mAccessPoint;
    }
}