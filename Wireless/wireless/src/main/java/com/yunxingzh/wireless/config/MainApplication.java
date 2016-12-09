package com.yunxingzh.wireless.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.baidu.mapapi.SDKInitializer;
import com.dgwx.app.lib.bl.WifiInterface;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.service.FWService;
import com.yunxingzh.wireless.utils.DeviceUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import wireless.libs.bean.vo.User;
import wireless.libs.okhttp.OkHttpUtil;

/**
 * Created by Carey on 2016/5/25.
 */
public class MainApplication extends Application {
    private static final String TAG = "Application";

    //TODO: change to private and getInstance
    public static MainApplication sApplication;
    private static String mCurrentProcessName;

    public static MainApplication getInstance() { return sApplication; }
    private static final String UI_PROCESS_NAME = "com.yunxingzh.wireless";
    private static final String SERVICE_PROCESS_NAME = "com.yunxingzh.wireless.service";

    private boolean isExit = false;
    private String mMark;
    private String mToken;
    private String wifiPwd;
    private String userName;
    private User mUser;

    public static Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        if (isUIApplication(this)) {
            try {
                startService(new Intent(this, FWService.class));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            FWManager.init(this);
            OkHttpUtil.init(sApplication);
            startService();
            bindService();
            //此方法要再setContentView方法之前实现
            SDKInitializer.initialize(getApplicationContext());//百度地图初始化
            //东莞无线sdk初始化
            WifiInterface.init(this);
            WifiInterface.initEnv(getResources().getString(R.string.wsmpurl), getResources().getString(R.string.ssids),getResources().getString(R.string.vnocode));

            mHandler = new Handler(Looper.getMainLooper());

        } else {
            LogUtils.d(TAG, "on create in service thread");
        }

    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public boolean needLogin() {
        return StringUtils.isEmpty(getToken()) || MainApplication.sApplication.getUser() == null;
    }

    public String getMark() {
        if (StringUtils.isEmpty(mMark)) {
            mMark = SPUtils.get(sApplication, Constants.SP_KEY_MARK, "");
            if (StringUtils.isEmpty(mMark.trim())) {
                mMark = DeviceUtils.createUUID(sApplication);
            }
        }
        return mMark;
    }

    public String getToken() {
        if (StringUtils.isEmpty(mToken)) {
            mToken = SPUtils.get(sApplication, Constants.SP_KEY_TOKEN, "");
//            if (StringUtils.isEmpty(mToken)) {
//                startActivity(new Intent(sApplication, RegisterActivity.class));
//            }
        }
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        SPUtils.put(sApplication, Constants.SP_KEY_TOKEN, token);
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
        SPUtils.put(sApplication, Constants.SP_WIFI_PWD, wifiPwd);
    }

    public String getWifiPwd() {
        if (wifiPwd == null) {
            wifiPwd = SPUtils.get(sApplication, Constants.SP_WIFI_PWD,"");
        }
        return wifiPwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SPUtils.put(sApplication, Constants.SP_TIME_OUT, userName);
    }

    public String getUserName() {
        if (userName == null) {
            userName = SPUtils.get(sApplication, Constants.SP_USER_NAME,"");
        }
        return userName;
    }

    public User getUser() {
        if (mUser == null) {
            mUser = (User) SPUtils.getObject(sApplication, Constants.SP_KEY_USER);
        }
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
        SPUtils.putObject(sApplication, Constants.SP_KEY_USER, mUser);
    }

    public void loginOut() {
        MainApplication.sApplication.setToken("");
        MainApplication.sApplication.setUser(null);
    }

    public static boolean isUIApplication(Context context) {
        return UI_PROCESS_NAME.equals(getCurrentProcessName(context));
    }

    public static boolean isServiceApplication(Context context) {
        return SERVICE_PROCESS_NAME.equals(getCurrentProcessName(context));
    }

    private void startService() {
        String cmd = "am startservice --user 0 -n com.yunxingzh.wireless/.service.FWService";
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(cmd);
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                startService(new Intent(this, FWService.class));
            } catch (Throwable e1) {
                e1.printStackTrace();
            }
        }
    }

    private void bindService(){
        FWManager.getInstance().bindService(this);
    }

    private static String getCurrentProcessName(Context context) {
        if (mCurrentProcessName == null) {
            synchronized (Application.class) {
                if (mCurrentProcessName == null) {
                    int myPid = android.os.Process.myPid();
                    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    for (ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()) {
                        if (appProcessInfo.pid == myPid) {
                            mCurrentProcessName = appProcessInfo.processName;
                            break;
                        }
                    }
                }
            }
        }
        return mCurrentProcessName;
    }

    /**
     * run code on UI thread
     * @param runnable
     */
    public static void runUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static void runUiThread(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }
}
