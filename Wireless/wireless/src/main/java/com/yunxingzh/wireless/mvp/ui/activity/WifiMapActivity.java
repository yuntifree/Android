package com.yunxingzh.wireless.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;

/**
 * Created by stephon on 2016/11/14.
 */

public class WifiMapActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv;
    private MapView mapView;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_wifi_manager);
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
        baiduMap = mapView.getMap();
        //定义Maker坐标点
       // LatLng point = new LatLng(Double.parseDouble(wei), Double.parseDouble(jing));
        //MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(point);
       // baiduMap.animateMapStatus(status);
        //构建Marker图标
      //  BitmapDescriptor bitmap = BitmapDescriptorFactory
        //        .fromResource(R.drawable.address_car);
        //构建MarkerOption，用于在地图上添加Marker
      //  OverlayOptions option = new MarkerOptions()
         //       .position(point)
        //        .icon(bitmap);
        //在地图上添加Marker，并显示
      //  baiduMap.addOverlay(option);
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v){
            finish();
        }
    }

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
