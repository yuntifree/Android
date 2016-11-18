package com.yunxingzh.wireless.utility;

import android.os.Build;
import android.util.Log;

public class Logg {

    private static boolean PRINT_ENABLE = true;
    private static boolean FILE_SAVE = false;
    private static int mProcessID;

    public static void e(String tag, String message) {
        String log = format(tag, message);
        if (PRINT_ENABLE) Log.e(tag, log);
        if(FILE_SAVE) save(tag, log);
    }

    public static void d(String tag, String message) {
        String log = format(tag, message);
        if (PRINT_ENABLE)
            Log.e(tag, log);
        if(FILE_SAVE) save(tag, log);
    }

    public static void w(String tag, String message) {
        String log = format(tag, message);
        if (PRINT_ENABLE) Log.w(tag, log);
        if(FILE_SAVE) save(tag, log);
    }

    public static void v(String tag, String message) {
        String log = format(tag, message);
        if (PRINT_ENABLE) Log.v(tag, log);
        if(FILE_SAVE) save(tag, log);
    }

    public static void i(String tag, String message) {
        String log = format(tag, message);
        if (PRINT_ENABLE) Log.i(tag, log);
        if(FILE_SAVE) save(tag, log);
    }

    private static String format(String tag, String log) {
        int lineNumber = -1;
        String methodName = "unknown";
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        if (sElements.length > 2) {
            methodName = sElements[2].getMethodName();
            lineNumber = sElements[2].getLineNumber();
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(':');
        buffer.append(lineNumber);
        buffer.append(']');
        buffer.append(log);
        return buffer.toString();
    }

    private static int getProcessID() {
        if (mProcessID <= 0) {
            mProcessID = android.os.Process.myPid();
        }
        return mProcessID;
    }

    private static void save(String tag, String msg, Object... params) {
        if (params != null && params.length > 0) {
            msg = String.format(msg, params);
        }

        String initContent = "---- Phone=" + Build.BRAND + "/" + Build.MODEL + " ----";

        //TODO: save message to local file
    }
}
