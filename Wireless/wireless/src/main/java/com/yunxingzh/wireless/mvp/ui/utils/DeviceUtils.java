package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by Carey on 2016/5/27.
 * 获取设备信息工具类
 */
public class DeviceUtils {
    private DeviceUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 生成设备唯一标识
     * @param context
     * @return
     */
    public static String createUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode()).toString();
    }
}
