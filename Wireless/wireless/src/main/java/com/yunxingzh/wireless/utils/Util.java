package com.yunxingzh.wireless.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.lang.ref.SoftReference;

/**
 * Created by hzg on 16/11/18.
 */

public class Util {
    private static final String TAG = "Util";

    static float defdensity;

    public static String speedMethodNormal(float speed) {
        // 对结果进行格式化（保留小数点后的2位）
        //java.text.DecimalFormat format = new java.text.DecimalFormat("0.0");
        // 对结果进行格式化（不保留小数点后）
        java.text.DecimalFormat format1 = new java.text.DecimalFormat("0.0");
        String res = "";
        // 原始bit
        double speedIn = speed;
        // 如果是bit那么直接返回bit
        if (speed == 0) {
            return "0 KB/s";
        }
        if (speed < 1024) {
            String r = "B/s";
            res = format1.format(speedIn) + " " + r;
        } else {
            // 如果比bit大，那么直接换算成KB
            speedIn = speedIn / 1024;
            if (speedIn < 1024) {
                String r = "KB/s";
                res = format1.format(speedIn) + " " + r;
            } else {
                // 如果比KB大，那么直接换算成MB，当换算成gB的时候
                speedIn = speedIn / 1024;
                if (speedIn < 1024) {
                    String r = "MB/s";
                    res = format1.format(speedIn) + " " + r;
                } else {
                    // 如果比mB大，那么直接换算成gB，当换算成gB的时候
                    speedIn = speedIn / 1024;
                    if (speedIn < 1024) {
                        String r = "GB/s";
                        res = format1.format(speedIn) + " " + r;
                    }
                }
            }
        }
        return res;
    }

    public static int dpToPx(int dp, Context context) {
        if (context == null)
            return (int) (dp * defdensity);
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        if (d == null)
            return (int) (dp * defdensity);
        defdensity = d.density;
        return (int) (dp * d.density);
    }

    /***********************************************************
     * Toast相关
     ***********************************************************/
    private static SoftReference<Toast> sToastRef = null;
    public static void hideToast() {
        if (sToastRef != null) {
            Toast previousToast = sToastRef.get();
            if (previousToast != null) {
                previousToast.cancel();
            }
        }
    }

    public static void showToast(Context context, int s) {
        showToast(context, context.getString(s));
    }

    public static void showToast(Context context, String s) {
        showToast(context, s, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String s, boolean show) {
        if (show) {
            showToast(context, s, Toast.LENGTH_SHORT);
        }
    }

    public static void showToastLong(Context context, int s) {
        showToast(context, context.getString(s), Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, String s) {
        showToast(context, s, Toast.LENGTH_LONG);
    }

    private static void showToast(final Context context, final String s, int length) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast toast = Toast.makeText(context, s, length);
            hideToast();
            toast.show();
            sToastRef = new SoftReference<Toast>(toast);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(context, s);
                }
            });
        }
    }

}
