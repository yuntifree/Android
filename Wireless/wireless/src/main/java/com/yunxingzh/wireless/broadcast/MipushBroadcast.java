package com.yunxingzh.wireless.broadcast;

import android.content.Context;
import android.content.Intent;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.activity.MainActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.JsonUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by stephen on 2017/2/9.
 * 小米推送
 */

public class MipushBroadcast extends PushMessageReceiver {

    //透传消息到达客户端时调用
    //作用：可通过参数message从而获得透传消息，具体请看官方SDK文档
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        //打印消息方便测试
        System.out.println("透传消息到达了");
        System.out.println("透传消息是" + message.toString());

    }

    //通知消息到达客户端时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：通过参数message从而获得通知消息，具体请看官方SDK文档
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        //打印消息方便测试
        System.out.println("通知消息到达了");
        System.out.println("通知消息是" + message.toString());
    }

    //用户手动点击通知栏消息时调用
    //注：应用在前台时不弹出通知的通知消息到达客户端时也会回调函数
    //作用：1. 通过参数message从而获得通知消息，具体请看官方SDK文档
    //2. 设置用户点击消息后打开应用 or 网页 or 其他页面
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        String msgJson = message.getContent();
        if (!StringUtils.isEmpty(msgJson)) {
            Map<String, Object> msgMap = JsonUtils.getMapForJson(msgJson);

            if (!AppUtils.isAppAlive(context, "com.yunxingzh.wireless")) {//app进程不存活
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.yunxingzh.wireless");
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            } else {
                if (msgMap != null) {
                    int type = (int) msgMap.get("type");
                    if (type == 1) {//type:1-跳转应用
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mainIntent);
                    } else if (type == 2) {//type:2-跳转连接
                        String dst = msgMap.get("dst").toString();
                        if (!StringUtils.isEmpty(dst)) {
                            Intent mainIntent = new Intent(context, WebViewActivity.class);
                            mainIntent.putExtra(Constants.URL, dst);
                            context.startActivity(mainIntent);
                        }
                    }
                }
            }
        }
        //打印消息方便测试
        System.out.println("用户点击了通知消息");
        System.out.println("通知消息是" + message.toString());
        System.out.println("点击后,会进入应用");

    }

    //用来接收客户端向服务器发送命令后的响应结果。
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        System.out.println(command);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                //打印信息便于测试注册成功与否
                System.out.println("注册成功");

            } else {
                System.out.println("注册失败");
            }
        }
    }

    //用于接收客户端向服务器发送注册命令后的响应结果。
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        System.out.println(command);

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                //打印日志：注册成功
                System.out.println("注册成功");
            } else {
                //打印日志：注册失败
                System.out.println("注册失败");
            }
        } else {
            System.out.println("其他情况" + message.getReason());
        }
    }
}
