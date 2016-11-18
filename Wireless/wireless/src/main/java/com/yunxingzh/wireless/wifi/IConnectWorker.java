package com.yunxingzh.wireless.wifi;

import rx.Observable;

public interface IConnectWorker {

    public Observable<WorkResult> confirm();

    public Observable<WorkResult> connect();

    public Observable<WorkResult> login();

    public Observable<WorkResult> check();

    public Observable<WorkResult> offline();

    public void destory();

    public AccessPoint getAccessPoint();

}