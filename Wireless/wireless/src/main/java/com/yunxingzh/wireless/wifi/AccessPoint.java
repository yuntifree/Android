package com.yunxingzh.wireless.wifi;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Comparator;

public class AccessPoint implements Parcelable {

    private static final ConfigurationSecurities ConfigSec = ConfigurationSecurities.newInstance();
    private static final byte TRUE = 1;
    private static final byte FALSE = 0;

    public enum PasswordFrom {
        UNKNOWN, INPUT, CLOUD
    }

    public String ssid;

    public String bssid;

    public String password;

    public PasswordFrom passwordFrom;

    public String security;

    public int rssi;

    public boolean isConfiged;

    public WifiConfiguration config;

    public ScanResult scanResult;

    public WifiInfo wifiInfo;

    public NetworkInfo.DetailedState detailedState;

    public WifiSignal signal;

    public AccessPoint(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        ssid = source.readString();
        bssid = source.readString();
        password = source.readString();
        passwordFrom = PasswordFrom.valueOf(source.readString());
        security = source.readString();
        rssi = source.readInt();
        signal = new WifiSignal(rssi);
        isConfiged = source.readByte() == TRUE;
    }

    public AccessPoint(ScanResult result) {
        ssid = (result.SSID == null) ? "" : WifiUtility.removeDoubleQuotes(result.SSID);
        bssid = result.BSSID;
        password = "";
        passwordFrom = PasswordFrom.UNKNOWN;
        security = ConfigSec.getScanResultSecurity(result);
        scanResult = result;
        rssi = result.level;
        signal = new WifiSignal(rssi);
    }

    public void setConfig(WifiConfiguration config){
        this.config = config;
        this.isConfiged = config != null;
    }

    public void setPassword(String password, PasswordFrom from){
        this.password = password;
        this.passwordFrom = from;
    }

    public boolean isOpen(){
        return ConfigSec.isOpenNetwork(security);
    }

    public boolean isFreeWifi(){
        return isOpen() || isConfiged || !TextUtils.isEmpty(password);
    }

    public static final Creator<AccessPoint> CREATOR = new Creator<AccessPoint>() {
        @Override
        public AccessPoint createFromParcel(Parcel in) {
            return new AccessPoint(in);
        }

        @Override
        public AccessPoint[] newArray(int size) {
            return new AccessPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel target, int flags) {
        target.writeString(ssid);
        target.writeString(bssid);
        target.writeString(password);
        target.writeString(passwordFrom == null ? PasswordFrom.UNKNOWN.name() : passwordFrom.name());
        target.writeString(security);
        target.writeInt(rssi);
        target.writeByte(isConfiged ? TRUE : FALSE);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ")
                .append("ssid:").append(ssid).append(' ')
                .append(",bssid:").append(bssid).append(' ')
                .append(",password:").append(password).append(' ')
                .append(",security:").append(security).append(' ')
                .append(",rssi:").append(rssi)
                .append(" }");

        return builder.toString();
    }

    public static Comparator<AccessPoint> Comparator() {
        return sComparator;
    }

    private static final Comparator<AccessPoint> sComparator;
    static {
        sComparator = new AccessPointComparator();
    }

    private static class AccessPointComparator implements Comparator<AccessPoint> {
        @Override
        public int compare(AccessPoint lhs, AccessPoint rhs) {
            int difference = WifiManager.compareSignalLevel(rhs.rssi, lhs.rssi);
            if (difference != 0) {
                return difference;
            }

            return lhs.ssid.compareToIgnoreCase(rhs.ssid);
        }
    }
}