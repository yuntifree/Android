package com.yunxingzh.wirelesslibs.wireless.lib.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by carey on 2016/7/6 0006.
 */
public class LocationUtils {

    private static double EARTH_RADIUS = 6378.137;

    // 纬度
    public static double latitude = 0.0;
    // 经度
    public static double longitude = 0.0;

    /**
     * 初始化位置信息
     *
     * @param context
     */
    public static void initLocation(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        } else {
            LocationListener locationListener = new LocationListener() {
                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {

                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
            }
        }
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
