package com.yunxingzh.wireless.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.yunxingzh.wireless.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by hzg on 16/12/8.
 */

public class ShowNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //设置点击通知栏的动作为启动另外一个广播
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String content = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");
        //创建一个通知
        //Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
        //            R.drawable.state_title_notice);
        Notification noti = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                //.setLargeIcon(icon)
                .setSmallIcon(R.drawable.state_title_notice)
                .setContentIntent(pendingIntent)
                .build();

        //用NotificationManager的notify方法通知用户生成标题栏消息通知
        NotificationManager nManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;// FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        nManager.notify(100, noti);//id是应用中通知的唯一标识
    }
}
