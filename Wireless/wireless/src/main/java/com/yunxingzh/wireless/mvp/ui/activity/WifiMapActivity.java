package com.yunxingzh.wireless.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IWifiMapPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiMapPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IWifiMapView;
import com.yunxingzh.wireless.utils.LocationUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.List;

import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/14.
 * wifi地图
 */

public class WifiMapActivity extends BaseActivity implements IWifiMapView, View.OnClickListener, BaiduMap.OnMapStatusChangeListener,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {

    public static final int MAP_PAGER = 1;

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv, mMapLocationIv;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationUtils locationUtils;
    private double lat;
    private double lon;
    private IWifiMapPresenter iWifiMapPresenter;
    private List<WifiMapVo> wifiMapInfo;

    private boolean flag = true;
    private InfoWindow mInfoWindow;
    private boolean isFirst = true;
    private AlertView alertView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mMapLocationIv = findView(R.id.map_location_iv);
        mMapLocationIv.setOnClickListener(this);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        baiduMap = mapView.getMap();
        locationUtils = new LocationUtils(this, MAP_PAGER);
        locationUtils.startMonitor(locationHandler);
        iWifiMapPresenter = new WifiMapPresenterImpl(this);
        baiduMap.setOnMapStatusChangeListener(this);
        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mMapLocationIv == v) {//点击定位到当前位置
            LatLng point = new LatLng(lat, lon);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
            baiduMap.setMapStatus(u);
        }
    }

    public void initMap() {
        LatLng point = new LatLng(lat, lon);
        if (isFirst) {
            isFirst = false;
            //定义Maker坐标点lat,lon:22.933103,113.903870
            //设置地图缩放比例
            baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(point).zoom(16).build()));
            baiduMap.setMyLocationEnabled(true);//开启定位图层
            MyLocationData locData = new MyLocationData.Builder()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(lat)
                    .longitude(lon).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.mine);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bitmap);
            baiduMap.setMyLocationConfigeration(config);
            iWifiMapPresenter.getWifiMap(lon, lat);//获取周围热点lon,lat
        }
    }

    /***
     * 获取周围热点
     *
     * @param wifiMapVo
     */
    @Override
    public void getWifiMapSuccess(WifiMapList wifiMapVo) {
        wifiMapInfo = wifiMapVo.infos;
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (WifiMapVo info : wifiMapInfo) {
            // 位置
            latLng = new LatLng(info.latitude, info.longitude);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.hot_point);
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(bitmap).zIndex(5);
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
//        baiduMap.setMapStatus(u);

    }


    final Handler locationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                lat = locationUtils.getBaseLocation().latitude;
                lon = locationUtils.getBaseLocation().longitude;
                initMap();
            } else if (msg.what == BDLocation.TypeServerError) {
                if (isFirst) {
                    isFirst = false;
                    ToastUtil.showMiddle(WifiMapActivity.this, R.string.location_error);
                    alertView = new AlertView("温馨提示", "亲,定位失败,请打开定位权限", "取消", new String[]{"去设置"}, null, WifiMapActivity.this, AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position != AlertView.CANCELPOSITION) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        }
                    }).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            if (alertView != null) {
                                alertView.dismiss();
                            }
                        }
                    });
                    alertView.show();
                }
            } else {
                LogUtils.i("lsd", "location error:" + msg.what);
            }
        }
    };

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        // 手势操作地图，设置地图状态等操作导致地图状态开始改变。
        //地图状态改变开始时的地图状态
        if (baiduMap != null) {
            baiduMap.hideInfoWindow();
        }
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        //地图状态变化中
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        //地图状态改变结束
        Point centerPoint = mapStatus.targetScreen;//获取屏幕中心点坐标
        LatLng centerLat = baiduMap.getProjection().fromScreenLocation(centerPoint);
        if (iWifiMapPresenter != null) {
            iWifiMapPresenter.getWifiMap(centerLat.longitude, centerLat.latitude);//获取周围热点lon,lat
        }

        if (baiduMap != null && mMarker != null) {
            showWindow(mMarker);
        }
    }

    private Marker mMarker;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);//关闭定位图层
        // 在activity执行onDestroy时执行mMapView.onDestroy()
        mapView.onDestroy();
        mapView = null;
        locationUtils.stopMonitor();
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

    @Override
    public void onMapClick(LatLng latLng) {
        if (baiduMap != null) {
            baiduMap.hideInfoWindow();
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarker = marker;
        if (baiduMap != null && mMarker != null) {
            showWindow(marker);
        }
        return true;
    }

    public void showWindow(Marker marker) {
        //获得marker中的数据
        Bundle bun = marker.getExtraInfo();
        if (bun == null) {
            return;
        }
        WifiMapVo info = (WifiMapVo) bun.get("info");
        if (info == null) {
            return;
        }
        //计算p1、p2两点之间的直线距离，单位：米
        LatLng p1LL = new LatLng(lat, lon);
        LatLng p2LL = new LatLng(info.latitude, info.longitude);
        double distance = DistanceUtil.getDistance(p1LL, p2LL);

        //生成一个TextView用户在地图中显示InfoWindow
        LinearLayout markerLayout = new LinearLayout(getApplicationContext());
        markerLayout.setOrientation(LinearLayout.HORIZONTAL);
        markerLayout.setPadding(20, 0, 20, 0);
        markerLayout.setGravity(Gravity.CENTER);
        markerLayout.setBackgroundResource(R.drawable.wifi_info);

        TextView address = new TextView(getApplicationContext());
        address.setTextColor(getResources().getColor(R.color.gray_b4b4b4));
        address.setTextSize(10);
        address.setEllipsize(TextUtils.TruncateAt.END);
        address.setMaxEms(16);
        address.setSingleLine();
        address.setText(info.address);

        TextView distancesView = new TextView(getApplicationContext());
        distancesView.setTextColor(getResources().getColor(R.color.blue_00a6f9));
        distancesView.setTextSize(10);
        if (distance >= 1000) {
            distancesView.setText(new DecimalFormat("0.0").format(distance / 1000) + "km");
        } else {
            distancesView.setText(new DecimalFormat("0").format(distance) + "m");
        }

        ImageView addressImg = new ImageView(getApplicationContext());
        addressImg.setImageResource(R.drawable.ico_location);
        ImageView distancesImg = new ImageView(getApplicationContext());
        distancesImg.setImageResource(R.drawable.ico_walk);

        markerLayout.addView(addressImg);
        markerLayout.addView(address);
        markerLayout.addView(distancesImg);
        markerLayout.addView(distancesView);
        //将marker所在的经纬度的信息转化成屏幕上的坐标
        LatLng ll = marker.getPosition();
        Point p = baiduMap.getProjection().toScreenLocation(ll);
        p.y -= 90;
        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
        mInfoWindow = new InfoWindow(markerLayout, llInfo, 0);
        //显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }
}
