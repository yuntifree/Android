package com.yunxingzh.wireless.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.dgwx.app.lib.bl.WifiInterface;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.utils.DeviceUtils;
import com.yunxingzh.wireless.mvp.ui.utils.LogUtils;
import com.yunxingzh.wireless.mvp.ui.utils.SPUtils;
import com.yunxingzh.wireless.mvp.ui.utils.StringUtils;
import com.yunxingzh.wireless.service.FWService;

import wireless.libs.bean.vo.UserInfoVo;
import wireless.libs.okhttp.OkHttpUtil;

/**
 * Created by Carey on 2016/5/25.
 */
public class MyApplication extends Application {
    private static final String TAG = "Application";

    //TODO: change to private and getInstance
    public static MyApplication sApplication;
    private static String mCurrentProcessName;

    public static MyApplication getInstance() { return sApplication; }
    private static final String UI_PROCESS_NAME = "com.yunxingzh.wireless";
    private static final String SERVICE_PROCESS_NAME = "com.yunxingzh.wireless.service";

    private boolean isExit = false;
    private String mMark;
    private String mToken;
    private String wifiPwd;
    private String userName;
    private UserInfoVo mUser;


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

            WifiInterface.init(this);
            WifiInterface.initEnv(getResources().getString(R.string.wsmpurl), getResources().getString(R.string.ssids),getResources().getString(R.string.vnocode));

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
        return StringUtils.isEmpty(getToken()) || MyApplication.sApplication.getUser() == null;
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



    public UserInfoVo getUser() {
        if (mUser == null) {
            mUser = (UserInfoVo) SPUtils.getObject(sApplication, Constants.SP_KEY_USER);
        }
        return mUser;
    }

    public void setUser(UserInfoVo mUser) {
        this.mUser = mUser;
        SPUtils.putObject(sApplication, Constants.SP_KEY_USER, mUser);
    }

    public void loginOut() {
        MyApplication.sApplication.setToken("");
        MyApplication.sApplication.setUser(null);
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
}
