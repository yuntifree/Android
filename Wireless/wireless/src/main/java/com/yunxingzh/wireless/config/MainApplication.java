package com.yunxingzh.wireless.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.baidu.mapapi.SDKInitializer;
import com.dgwx.app.lib.bl.WifiInterface;
import com.squareup.leakcanary.LeakCanary;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.service.FWService;
import com.yunxingzh.wireless.utils.DeviceUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.List;

import wireless.libs.bean.vo.User;

/**
 * Created by Carey on 2016/5/25.
 */
public class MainApplication extends Application {
    private static final String TAG = "Application";

    //TODO: change to private and get
    private static MainApplication sInst;
    private static String mCurrentProcessName;

    public static MainApplication get() { return sInst; }
    private static final String UI_PROCESS_NAME = "com.yunxingzh.wireless";
    private static final String SERVICE_PROCESS_NAME = "com.yunxingzh.wireless.service";

    private boolean isExit = false;
    private String mMark;
    private String mToken;
    private String wifiPwd;
    private String userName;
    private User mUser;
    private String privdata;
    private String expire;
    private String nick;
    private String headUrl;

    public static Handler mHandler;

    //小米推送
    private static final String APP_ID = "2882303761517531135";
    private static final String APP_KEY = "5191753131135";



    @Override
    public void onCreate() {
        super.onCreate();

        if (shouldInit()) {
            //注册推送服务
            //注册成功后会向DemoMessageReceiver发送广播
            // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        sInst = this;
        if (AppConfig.DEV_MODEL) {//reless包无需监测
            //监测内存泄漏
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        if (isUIApplication(this)) {
            try {
                startService(new Intent(this, FWService.class));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            FWManager.init(this);
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
        return StringUtils.isEmpty(getToken()) || sInst.getUser() == null;
    }

    public String getMark() {
        if (StringUtils.isEmpty(mMark)) {
            mMark = SPUtils.get(sInst, Constants.SP_KEY_MARK, "");
            if (StringUtils.isEmpty(mMark.trim())) {
                mMark = DeviceUtils.createUUID(sInst);
            }
        }
        return mMark;
    }

    public String getToken() {
        if (StringUtils.isEmpty(mToken)) {
            mToken = SPUtils.get(sInst, Constants.SP_KEY_TOKEN, "");
//            if (StringUtils.isEmpty(mToken)) {
//                startActivity(new Intent(sInst, RegisterActivity.class));
//            }
        }
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        SPUtils.put(sInst, Constants.SP_KEY_TOKEN, token);
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
        SPUtils.put(sInst, Constants.SP_WIFI_PWD, wifiPwd);
    }

    public String getWifiPwd() {
        if (wifiPwd == null) {
            wifiPwd = SPUtils.get(sInst, Constants.SP_WIFI_PWD,"");
        }
        return wifiPwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SPUtils.put(sInst, Constants.SP_USER_NAME, userName);
    }

    public String getUserName() {
        if (userName == null) {
            userName = SPUtils.get(sInst, Constants.SP_USER_NAME,"");
        }
        return userName;
    }

    public void setNick(String nick) {
        this.nick = nick;
        SPUtils.put(sInst, Constants.SP_NICK, nick);
    }

    public String getNick() {
        if (nick == null) {
            nick = SPUtils.get(sInst, Constants.SP_NICK,"");
        }
        return nick;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
        SPUtils.put(sInst, Constants.SP_HEAD_URL, headUrl);
    }

    public String getHeadUrl() {
        if (headUrl == null) {
            headUrl = SPUtils.get(sInst, Constants.SP_HEAD_URL,"");
        }
        return headUrl;
    }


    public User getUser() {
        if (mUser == null) {
            mUser = (User) SPUtils.getObject(sInst, Constants.SP_KEY_USER);
        }
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
        SPUtils.putObject(sInst, Constants.SP_KEY_USER, mUser);
    }

    //过期时间
    public void setExpire(String expire) {
        this.expire = expire;
        SPUtils.put(sInst, Constants.SP_KEY_EXPIRE, expire);
    }

    public String getExpire() {
        if (expire == null) {
            expire = SPUtils.get(sInst, Constants.SP_KEY_EXPIRE, "");
        }
        return expire;
    }

    //用于刷新token
    public void setPrivdata(String privdata) {
        this.privdata = privdata;
        SPUtils.put(sInst, Constants.SP_KEY_PRIVDATA, privdata);
    }

    public String getPrivdata() {
        if (privdata == null) {
            privdata = SPUtils.get(sInst, Constants.SP_KEY_PRIVDATA,"");
        }
        return privdata;
    }

    public void loginOut() {
        MainApplication.sInst.setToken("");
        MainApplication.sInst.setUser(null);
    }

    public static boolean isUIApplication(Context context) {
        return UI_PROCESS_NAME.equals(getCurrentProcessName(context));
    }

    public static boolean isServiceApplication(Context context) {
        return SERVICE_PROCESS_NAME.equals(getCurrentProcessName(context));
    }

    //通过判断手机里的所有进程是否有这个App的进程
    //从而判断该App是否有打开
    private boolean shouldInit() {
        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();

        //获取本App的唯一标识
        int myPid = Process.myPid();
        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
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
