package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.activity.RegisterActivity;
import com.yunxingzh.wireless.utility.NetUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.api.HttpCode;
import com.yunxingzh.wirelesslibs.wireless.lib.okhttp.response.OkHttpCallback;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;

public class ToastUtil {

    private static Toast mToast = null;

    public static void showMiddle(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
        //mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
    }

    public static void showMiddle(Context context, int resId) {
        context = context.getApplicationContext();
        showMiddle(context, context.getString(resId));
    }

    public static void showError(Context context, int error) {
        if (error == HttpCode.E_TOKEN ) {
            showMiddle(context, R.string.lost_token);
            MyApplication.sApplication.setToken("");
            MyApplication.sApplication.setUser(null);
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//清空栈
            context.startActivity(intent);
            return;
        } else if(error == HttpCode.E_MISS_PARAM){//即缺少参数
            showMiddle(context, R.string.net_error);
            if (!NetUtils.isNetworkAvailable(context)){
                EventBus.getDefault().post(new EventBusType(Constants.NET_CHAGED));
            }
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
        switch (error) {
            case OkHttpCallback.RESPONSE_ERROR_NET:
                showMiddle(context, R.string.net_error_net);
                break;
            case OkHttpCallback.RESPONSE_ERROR_SERVER:
                showMiddle(context, R.string.net_error_server);
                break;
            case OkHttpCallback.RESPONSE_ERROR_TIMEOUT:
                showMiddle(context, R.string.net_error_timeout);
                break;
            case HttpCode.HTTP_OK:
                showMiddle(context, R.string.success);
                break;
        }
    }
}
