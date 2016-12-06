package com.yunxingzh.wireless.mvp.ui.utils;

import android.os.Build.VERSION;

import java.lang.reflect.Field;

;

public class Version {

    public final static int SDK = getSDK();

    private static int getSDK(){
        final Class<VERSION> versionClass = VERSION.class;
        try {
            // First try to read the recommended field android.os.Build.VERSION.SDK_INT.
            final Field sdkIntField = versionClass.getField("SDK_INT");
            return sdkIntField.getInt(null);
        } catch (NoSuchFieldException e) {
            // If SDK_INT does not exist, read the deprecated field SDK.
            return Integer.valueOf(VERSION.SDK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}