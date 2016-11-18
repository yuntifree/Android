package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.IWifiMapPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiMapPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.LocationUtils;
import com.yunxingzh.wireless.mvp.view.IWifiMapView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiMapVo;

import java.util.List;

/**
 * Created by stephon on 2016/11/14.
 * wifi地图
 */

public class WifiMapActivity extends BaseActivity implements IWifiMapView, View.OnClickListener {

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationUtils locationUtils;
    private double lat;
    private double lon;
    private IWifiMapPresenter iWifiMapPresenter;
    private List<WifiMapVo.WifiMapData.WifiMapInfo> wifiMapInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_wifi_map);
        initView();
        initData();
    }

    public void initView() {
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleNameTv.setText(R.string.hot);
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mapView = findView(R.id.baidu_mv);
    }

    public void initData() {
        locationUtils = LocationUtils.getInstance(this);
        new Thread(new GetLocationThread()).start();
        iWifiMapPresenter = new WifiMapPresenterImpl(this);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v){
            finish();
        }
    }

    @Override
    public void getWifiMapSuccess(WifiMapVo wifiMapVo) {
        wifiMapInfo = wifiMapVo.getData().getInfos();
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (WifiMapVo.WifiMapData.WifiMapInfo info : wifiMapInfo) {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.hot_point);
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(bitmap).zIndex(5);
            baiduMap.addOverlay(overlayOptions);
      //      marker = (Marker) (baiduMap.addOverlay(overlayOptions));
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("info", info);
//            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(u);
    }

    public class GetLocationThread implements Runnable {
        @Override
        public void run() {
            try {
                locationUtils.startMonitor();//开始定位
                Thread.sleep(1000);
                Message message = new Message();
                message.what = 1;
                locationHandler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    final Handler locationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    baiduMap = mapView.getMap();
                    lat = locationUtils.getBaseLocation().latitude;
                    lon = locationUtils.getBaseLocation().longitude;
                    //定义Maker坐标点
                    LatLng point = new LatLng(lat,lon);
                    //设置地图缩放比例
                    baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));

                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
                    baiduMap.animateMapStatus(status);
                    // 构建Marker图标
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.mine);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    baiduMap.addOverlay(option);
                    iWifiMapPresenter.getWifiMap(lon,lat);//获取周围热点
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()
        mapView.onPause();
    }
}
