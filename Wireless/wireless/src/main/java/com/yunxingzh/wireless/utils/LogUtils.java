package com.yunxingzh.wireless.utils;

import android.util.Log;

/**
 * 日志辅助类
 */
public final class LogUtils {
	private final static boolean DEBUG = true;

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.w(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.i(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.d(tag, msg, tr);
		}
	}
}
