package com.yunxingzh.wireless.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import java.util.List;

/**
 * 跟App相关的辅助类
 * 
 * 
 * 
 */
public class AppUtils {

	/**
	 * 未知网络类型
	 */
	public static final int NETWORK_CLASS_UNKNOWN = -1;

	/**
	 * wifi 连接
	 */
	public static final int NETWORK_WIFI = 0;

	/**
	 * "4G" 连接
	 */
	public static final int NETWORK_CLASS_4G = 1;

	/**
	 * "3G" 连接
	 */
	public static final int NETWORK_CLASS_3G = 2;

	/**
	 * "2G" 连接
	 */
	public static final int NETWORK_CLASS_2G = 3;

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取版本Code
	 *
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static int getVersionCode(Context ctx, String packageName) {
		final PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int getVersionCode(Context ctx) {
		final PackageManager packageManager = ctx.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 判断本机是否安装有某应用
	 * @param context
	 * @param packageName
     * @return
     */
	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public static int getNetWorkClass(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return NETWORK_CLASS_2G;

			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return NETWORK_CLASS_3G;

			case TelephonyManager.NETWORK_TYPE_LTE:
				return NETWORK_CLASS_4G;

			default:
				return NETWORK_CLASS_UNKNOWN;
		}
	}

	/***
	 * 获取手机连接的网络类型（是WIFI还是手机网络[2G/3G/4G]）
	 * @param context
	 * @return
     */
	public static int getNetWorkType(Context context) {
		int netWorkType = NETWORK_CLASS_UNKNOWN;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			int type = networkInfo.getType();

			if (type == ConnectivityManager.TYPE_WIFI) {
				netWorkType = NETWORK_WIFI;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				netWorkType = getNetWorkClass(context);
			}
		}
		return netWorkType;
	}

	/**
	 * 获取渠道名
	 * @return 如果没有获取成功，那么返回值为空
	 */
	public static String getChannelName(Context ctx) {
		if (ctx == null) {
			return null;
		}
		String channelName = "";
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				//注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
					}
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return channelName;
	}

	/**
	 * 获取手机型号
	 */
	public static String getPhoneModel(){
		return android.os.Build.MODEL;
	}

	/**
	 * 判断进程是否存活
	 * @param context
	 * @param packageName
     * @return
     */
	public static boolean isAppAlive(Context context, String packageName){
		ActivityManager activityManager =
				(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos
				= activityManager.getRunningAppProcesses();
		for (int i = 0; i < processInfos.size(); i++){
			if (processInfos.get(i).processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 缩放动画效果
	 */
	public static void animation(View view) {
		// 参数1：x轴的初始值
		// 参数2：x轴收缩后的值
		// 参数3：y轴的初始值
		// 参数4：y轴收缩后的值
		// 参数5：确定x轴坐标的类型
		// 参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
		// 参数7：确定y轴坐标的类型
		// 参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.95f, 1.0f, 0.95f,
				1.0f, Animation.RELATIVE_TO_SELF, 0.8f,
				Animation.RELATIVE_TO_SELF, 0.8f);
		scaleAnimation.setDuration(200);
		animationSet.addAnimation(scaleAnimation);
		view.startAnimation(animationSet);

	}
}