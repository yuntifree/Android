package com.yunxingzh.wireless.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.AppConfig;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mvp.ui.activity.RegisterActivity;

import wireless.libs.api.HttpCode;

public class ToastUtil {

    private static Toast mToast = null;

    public static void showMiddle(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        if (context == null) {
            context = MainApplication.get().getApplicationContext();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
        //mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
    }

    public static void wifiSpiritedshow(Context context, int resId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
        mToast.setText(context.getString(resId));
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showMiddle(Context context, int resId) {
        if (context != null) {
            //context = context.getApplicationContext();
            showMiddle(context, context.getString(resId));
        }
    }

    public static void showDebug(String msg) {
        if (AppConfig.DEV_MODEL) {
            MainApplication appContext = MainApplication.get();
            Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(String msg) {
        MainApplication appContext = MainApplication.get();
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, int error) {
        if (error == HttpCode.E_TOKEN ) {
            showMiddle(context, R.string.lost_token);
            MainApplication.get().setToken("");
            MainApplication.get().setUser(null);
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//清空栈
            context.startActivity(intent);
            return;
        } else if(error == HttpCode.E_MISS_PARAM){//即缺少参数
            showMiddle(context, R.string.net_error);
            return;
        } else if(error == HttpCode.E_INVAL_PARAM){
            showMiddle(context, R.string.param_error);
            return;
        } else if(error == HttpCode.E_DATABASE){
            showMiddle(context, R.string.data_error);
            return;
        } else if(error == HttpCode.E_INNER){
            showMiddle(context, R.string.inner_error);
            return;
        } else if(error == HttpCode.E_CODE){
            showMiddle(context, R.string.code_error);
            return;
        } else if(error == HttpCode.E_GET_CODE){
            showMiddle(context, R.string.get_code_error);
            return;
        } else if(error == HttpCode.E_USED_PHONE){
            showMiddle(context, R.string.phone_error);
            return;
        } else if(error < HttpCode.E_SERVER){
            showMiddle(context, R.string.server_error);
            return;
        }
    }
}
