package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.adapter.AccessPointAdapter;
import com.yunxingzh.wireless.utility.Logg;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WiFiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "WiFiFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AccessPointAdapter mAdapter;
    private View mDisabledView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mDisabledView = view.findViewById(R.id.view_disabled);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AccessPointAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mHandler.removeMessages(MSG_REFRESH_LIST);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));

        FWManager.getInstance().addWifiObserver(wifiObserver);

        setEnabled(FWManager.getInstance().isEnabled());
    }

    @Override
    public void onRefresh() {
        FWManager.getInstance().scan();
        Observable.timer(1500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mHandler.removeMessages(MSG_REFRESH_LIST);
                mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        FWManager.getInstance().removeWifiObserver(wifiObserver);
    }

    private FWManager.WifiObserver wifiObserver = new FWManager.WifiObserver() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            Logg.d(TAG, "onStateChanged");
            setEnabled(new_state != WifiState.DISABLED);
            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
        }

        @Override
        public void onListChanged(List<AccessPoint> accessPoints) {
            Logg.d(TAG, "onListChanged");
            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 0));
        }

        @Override
        public void onRSSIChanged(int rssi) {
            Logg.d(TAG, "onRSSIChanged");
            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
        }

        @Override
        public void onAuthError(AccessPoint ap) {
            Logg.d(TAG, "onAuthError");
            mHandler.removeMessages(MSG_AUTH_ERROR);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_AUTH_ERROR, ap));
        }
    };

    private static final int MSG_REFRESH_LIST = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_LIST:
                    refreshList(msg.arg1);
                    break;
                case MSG_AUTH_ERROR:
                    //DialogActivity.showInuptPWD(getContext(), (AccessPoint)msg.obj, true);
                    break;
            }
        }
    };

    private void refreshList(int forceRefresh){
        WifiState state = FWManager.getInstance().getState();
        AccessPoint current;
        if (state == WifiState.IDLE || state == WifiState.DISABLED
                || state == WifiState.DISCONNECTED || state == WifiState.UNKOWN) {
            current = null;
        } else {
            current = FWManager.getInstance().getCurrent();
        }
        List<AccessPoint> list = FWManager.getInstance().getList();
        if(mAdapter != null){
            mAdapter.setData(state, current, list, forceRefresh == 1 ? true : false);
        }
    }

    private void setEnabled(boolean enable){
        if(enable){
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mDisabledView.setVisibility(View.GONE);
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mDisabledView.setVisibility(View.VISIBLE);
        }
    }
}