package com.yunxingzh.wireless.mvp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IWifiMapPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiMapPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.LocationUtils;
import com.yunxingzh.wireless.mvp.view.IWifiMapView;
import com.yunxingzh.wireless.utils.LocationUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.List;

import wireless.libs.bean.resp.WifiMapList;
import wireless.libs.bean.vo.WifiInfoVo;
import wireless.libs.bean.vo.WifiMapVo;

/**
 * Created by stephon on 2016/11/14.
 * wifi地图
 */

public class WifiMapActivity extends BaseActivity implements IWifiMapView, View.OnClickListener {

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
            // 构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.mine);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
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
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(u);
        if (wifiMapInfo.size() > 0) {
            initMarkerClickEvent();
        }
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
                ToastUtil.showMiddle(WifiMapActivity.this, R.string.location_error);
            } else {
                LogUtils.i("lsd", "location error:" + msg.what);
            }
        }
    };

    private void initMarkerClickEvent() {
        //对Marker的点击
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //获得marker中的数据
                Bundle bun = marker.getExtraInfo();
                if (bun == null) {
                    return false;
                }
                WifiMapVo info = (WifiMapVo) bun.get("info");
                if (info == null) {
                    return false;
                }
                //计算p1、p2两点之间的直线距离，单位：米
                LatLng p1LL = new LatLng(22.933103, 113.903870);
                LatLng p2LL = new LatLng(info.latitude, info.longitude);
                double distance = DistanceUtil.getDistance(p1LL, p2LL);

                //生成一个TextView用户在地图中显示InfoWindow
                LinearLayout markerLayout = new LinearLayout(getApplicationContext());
                markerLayout.setOrientation(LinearLayout.HORIZONTAL);
                markerLayout.setGravity(Gravity.CENTER);
                markerLayout.setBackgroundResource(R.drawable.wifi_info);

                TextView address = new TextView(getApplicationContext());
                address.setTextColor(getResources().getColor(R.color.gray_b4b4b4));
                address.setTextSize(10);
                address.setText(info.address);

                TextView distances = new TextView(getApplicationContext());
                distances.setTextColor(getResources().getColor(R.color.blue_00a6f9));
                distances.setTextSize(10);
                distances.setText(new DecimalFormat("#").format(distance) + "m");

                ImageView addressImg = new ImageView(getApplicationContext());
                addressImg.setImageResource(R.drawable.ico_location);
                ImageView distancesImg = new ImageView(getApplicationContext());
                distancesImg.setImageResource(R.drawable.ico_walk);

                markerLayout.addView(addressImg);
                markerLayout.addView(address);
                markerLayout.addView(distancesImg);
                markerLayout.addView(distances);
                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = baiduMap.getProjection().toScreenLocation(ll);
                p.y -= 90;
                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                //为弹出的InfoWindow添加点击事件
                mInfoWindow = new InfoWindow(markerLayout, llInfo, 0);
                if (flag) {
                    //显示InfoWindow
                    baiduMap.showInfoWindow(mInfoWindow);
                    flag = false;
                } else {
                    baiduMap.hideInfoWindow();
                    flag = true;
                }
                baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        baiduMap.hideInfoWindow();
                    }

                    @Override
                    public boolean onMapPoiClick(MapPoi mapPoi) {
                        return false;
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
