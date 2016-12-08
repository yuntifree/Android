package com.yunxingzh.wireless.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.LLSInterface;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.ui.activity.MainActivity;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.LogUtils;

/**
 * Created by hzg on 16/12/8.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    private static final String UI_APPLICATION = "com.yunxingzh.wireless";
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if (AppUtils.isAppAlive(context, UI_APPLICATION)) {
            LogUtils.d(TAG, "~~Notify~~ in app start");
            //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
            //DetailActivity前，要先启动MainActivity。
            Intent mainIntent = new Intent(context, MainActivity.class);
            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
            //如果Task栈不存在MainActivity实例，则在栈顶创建
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent[] intents = {mainIntent};
            context.startActivities(intents);
        } else {
            LogUtils.d(TAG, "~~Notify~~ out app start");
            //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
            //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
            //参数跳转到DetailActivity中去了
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage(UI_APPLICATION);
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }
    }
}
