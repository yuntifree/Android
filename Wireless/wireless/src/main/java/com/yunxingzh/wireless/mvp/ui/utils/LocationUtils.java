package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;

import java.util.List;

/**
 * Created by carey on 2016/7/6 0006.
 */
public class LocationUtils {

    private static double EARTH_RADIUS = 6378.137;

    private static LocationUtils mInstance;
    private BDLocation mLocation = null;
    private MLocation  mBaseLocation = new MLocation();

    public static LocationUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationUtils(context);
        }
        return mInstance;
    }

    public BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;

    public LocationUtils(Context context) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        initParams();
        mLocationClient.registerLocationListener(myListener);
    }

    public void startMonitor() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        } else {

        }
    }

    public void stopMonitor() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public BDLocation getLocation() {
        return mLocation;
    }

    public MLocation getBaseLocation() {
        return mBaseLocation;
    }

    private void initParams() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        //option.setPriority(LocationClientOption.NetWorkFirst);
        //option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
       // option.disableCache(true);//禁止启用缓存定位
      //  option.setPoiNumber(5);    //最多返回POI个数
       // option.setPoiDistance(1000); //poi查询距离
       // option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return ;
            }
            mLocation = location;
            mBaseLocation.latitude = mLocation.getLatitude();
            mBaseLocation.longitude = mLocation.getLongitude();

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            sb.append("\ncity : ");
            sb.append(location.getCity());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public class MLocation {
        public double latitude;
        public double longitude;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static AreaUtils.Data getCity(Context context) {
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location == null){
            return null;
        }
        AreaUtils areaUtils = AreaUtils.getInstance(context);
        List<AreaUtils.Data> provinceList = areaUtils.getProvinceList();
        AreaUtils.Data closestData = null;
        double closestLength = 0;
        for (AreaUtils.Data provinceData : provinceList) {
            List<AreaUtils.Data> cityList = areaUtils.getCityListById(provinceData.getId());
            for (AreaUtils.Data cityData : cityList) {
                double len = getDistance(location.getLatitude(), location.getLongitude(), cityData.getLatitude(), cityData.getLongitude());
                if (closestLength == 0) {
                    closestLength = len;
                    closestData = cityData;
                } else if (len < closestLength) {
                    closestLength = len;
                    closestData = cityData;
                }
            }
        }
        return closestData;
    }

    /**
     * 根据两个位置的经纬度，来计算两地的距离
     *
     * @param lat1 当前经度
     * @param lng1 当前纬度
     * @param lat2 城市经度
     * @param lng2 城市纬度
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        return distance;
    }
}
