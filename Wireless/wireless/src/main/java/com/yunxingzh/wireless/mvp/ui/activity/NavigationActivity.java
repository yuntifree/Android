package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.net.URISyntaxException;

import wireless.libs.bean.vo.WifiMapVo;

/**
 * Created by stephen on 2017/3/9.
 * 导航
 */

public class NavigationActivity extends BaseActivity implements View.OnClickListener {

    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView mTitleNameTv, mNavAddressTv;
    private ImageView mTitleReturnIv, mNavIv;
    private WifiMapVo wifiMapVo;
    private LinearLayout mNavLay;
    private double myLat;
    private double myLon;

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
        mNavLay = findView(R.id.nav_lay);
        mNavLay.setVisibility(View.VISIBLE);
        mNavIv = findView(R.id.nav_iv);
        mNavIv.setOnClickListener(this);
        mNavIv.setVisibility(View.VISIBLE);
        mNavAddressTv = findView(R.id.nav_address_tv);
    }

    public void initData() {
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        wifiMapVo = (WifiMapVo) getIntent().getSerializableExtra("wifiMapInfo");
        myLat = getIntent().getDoubleExtra("myLat", 0.0);
        myLon = getIntent().getDoubleExtra("myLon", 0.0);
        // 不显示缩放比例尺
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);
        baiduMap.setMyLocationEnabled(true);//开启定位图层
        if (wifiMapVo != null) {
            mNavAddressTv.setText(wifiMapVo.address);
            LatLng point = new LatLng(wifiMapVo.latitude, wifiMapVo.longitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
            baiduMap.setMapStatus(u);

            MyLocationData locData = new MyLocationData.Builder()
                    .direction(0)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(wifiMapVo.latitude)
                    .longitude(wifiMapVo.longitude).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.mine_location);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bitmap);
            baiduMap.setMyLocationConfigeration(config);
        } else {
            mNavIv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        } else if (mNavIv == v) {//选择地图导航
            Intent intent = new Intent();
            try {
                if (AppUtils.checkApkExist(this, "com.baidu.BaiduMap")) {
                    //移动APP调起百度地图
                    intent.setData(Uri.parse("baidumap://map/direction?region=" + wifiMapVo.address + "&origin=" + myLat + "," + myLon +
                            "&destination=" + wifiMapVo.latitude + "," + wifiMapVo.longitude + "&mode=walking"));
                    startActivity(intent); //启动调用
                } else if (AppUtils.checkApkExist(this, "com.autonavi.minimap")) {
                    //移动APP调起高德地图
                    intent.setData(Uri.parse("androidamap://keywordNavi?sourceApplication=东莞无限&keyword=" + wifiMapVo.address + "&style=2"));
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } else if (AppUtils.checkApkExist(this, "com.google.android.apps.maps")) {
                    //移动APP调起谷歌地图
                    intent.setData(Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + wifiMapVo.address + "&saddr=" + myLat + "," + myLon + "&daddr=" + wifiMapVo.latitude + "," + wifiMapVo.longitude));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                } else {
                    //调起腾讯地图web
                    Intent inte = new Intent(this, WebViewActivity.class);
                    inte.putExtra(Constants.URL, "http://apis.map.qq.com/uri/v1/routeplan?type=walk&from=我的位置&fromcoord=" + myLat + "," + myLon + "&to=" + wifiMapVo.address + "&tocoord=" + wifiMapVo.latitude + "," + wifiMapVo.longitude + "&policy=1&referer=东莞无限");
                    startActivity(inte);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);//关闭定位图层
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
    }
}
