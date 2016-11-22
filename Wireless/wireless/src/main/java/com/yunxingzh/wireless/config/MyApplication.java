package com.yunxingzh.wireless.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.mvp.ui.activity.RegisterActivity;
import com.yunxingzh.wireless.utility.Logg;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.AreaDataVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.OkHttpUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.AreaUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.DeviceUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import com.yunxingzh.wireless.service.FWService;

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

    private AreaDataVo mAreaDataVo;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        if (isUIApplication(this)) {
            Logg.d(TAG, "on create in ui thread");
            try {
                startService(new Intent(this, FWService.class));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            FWManager.init(this);
            OkHttpUtil.init(sApplication);
            startService();
            bindService();
        } else {
            Logg.d(TAG, "on create in service thread");
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

    public AreaDataVo getAreaDataVo() {
        if (mAreaDataVo == null) {
            mAreaDataVo = (AreaDataVo) SPUtils.getObject(sApplication, Constants.SP_KEY_AREA);
        }
        return mAreaDataVo;
    }

    public void setAreaDataVo(int pId, int cId, int aId) {
        setAreaDataVo(pId, cId, aId, AreaUtils.getInstance(this).getProvinceNameById(pId)
                , AreaUtils.getInstance(this).getCityNameById(pId, cId), AreaUtils.getInstance(this).getAreaNameById(pId, cId, aId));
    }

    public void setAreaDataVo(int pId, int cId, int aId, String pName, String cName, String aName) {
        if (getAreaDataVo() == null) {
            this.mAreaDataVo = new AreaDataVo(pId, cId, aId, pName, cName, aName);
            SPUtils.putObject(sApplication, Constants.SP_KEY_AREA, mAreaDataVo);
        } else {
            mAreaDataVo.setData(pId, cId, aId, pName, cName, aName);
        }
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
