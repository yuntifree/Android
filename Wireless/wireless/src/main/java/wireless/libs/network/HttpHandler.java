package wireless.libs.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.ui.activity.RegisterActivity;
import com.yunxingzh.wireless.utils.JsonUtils;
import com.yunxingzh.wireless.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import wireless.libs.bean.resp.BaseResult;
import wireless.libs.bean.resp.ServerTip;
import wireless.libs.network.request.NetWorkWarpper;


/**
 * 标准接口的处理
 *
 * @author jerry
 * @date 2015/05/24
 *
 * 修订: 2016/06/18 carlos
 * 1. onFailure()方法删除 SocketTimeoutException 的情况.
 */
public abstract class HttpHandler<T> implements Callback {

    private static final String TAG = HttpHandler.class.getSimpleName();

    protected Context mAppContext;

    protected Type mGsonType;

    protected ParseType mParseType = ParseType.object;    // data解析类型  是数组还是object

    protected Class<T> entityClass;   //T.class 泛型的class类型  用于gson解析\

    public HttpHandler() {
        this.mAppContext = MainApplication.get();
        try {
            entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            entityClass = (Class<T>) Object.class;
        }
    }

    @Override
    public final void onFailure(Call call, final IOException e) {
        LogUtils.d(TAG, "onFailure " + e.toString());
        if (e instanceof UnknownHostException || e instanceof SocketException) {
            onFailureOnUIThread(new ServerTip(ErrorType.E_LOCAL, "请检查网络～"));
        } else {
            onFailureOnUIThread(new ServerTip(ErrorType.E_IO_EXCEPTION, ""));
        }
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        if (response != null) {
            if (response.code() == 200) {
                String isLiveUrl = response.request().url().url().getPath();
                //请求码成功
                String respBodyStr = response.body().string();
                if (isLiveUrl.contains("huajiao")) {//找出huajiao直播的url
                    respBodyStr = respBodyStr.substring(2, respBodyStr.length() - 1);
                }
                final String httpUrl = response.request().url().toString();
                LogUtils.d("url", "respBodyStr    " + httpUrl + "\r\n :" + respBodyStr);

                if (!TextUtils.isEmpty(respBodyStr)) {
                    parseResult(respBodyStr);
                } else {
                    onFailureOnUIThread(new ServerTip(ErrorType.E_NETWORK, mAppContext.getString(R.string.http_error_msg)));
                }
            } else {
                onFailureOnUIThread(new ServerTip(ErrorType.E_NETWORK, mAppContext.getString(R.string.http_error_msg)));
            }
        }
    }

    /**
     * 解析请求结果
     *
     * @param respBodyStr
     */
    protected void parseResult(String respBodyStr) {
        try {
            BaseResult resp = JsonUtils.parseObject(respBodyStr, BaseResult.class);
            if (resp != null) {
                if (resp.errno() == ErrorType.E_OK) {
                    //请求成功
                    //后台没有返回data类型
                    if (resp.data == null) {
                        onSuccessOnUIThread(resp, null);
                    } else {
                        T data = JsonUtils.parseObject(resp.data, entityClass);
                        if (data != null) {
                            onSuccessOnUIThread(resp, data);
                        } else {
                            onFailureOnUIThread(new ServerTip(ErrorType.E_JSON_PARSE, mAppContext.getString(R.string.json_parse_error)));
                        }
                    }
                } else {
                    onFailureOnUIThread(resp);
                }
            } else {
                onFailureOnUIThread(new ServerTip(ErrorType.E_JSON_PARSE, mAppContext.getString(R.string.json_parse_error)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailureOnUIThread(new ServerTip(ErrorType.E_JSON_PARSE, mAppContext.getString(R.string.json_parse_error)));
        }
    }

    private final void onSuccessOnUIThread(final ServerTip serverTip, final T t) {
        MainApplication.runUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(serverTip, t);
                onFinish();
            }
        });
    }

    private final void onFailureOnUIThread(final ServerTip serverTip) {
        MainApplication.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverTip.errno == ErrorType.E_TOKEN || serverTip.errno == ErrorType.E_DELETED_USER) {
                    //无Token或者被拉黑
                    MainApplication app = MainApplication.get();
                    app.setToken("");
                    app.setUser(null);
                    Intent intent = new Intent(mAppContext, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//清空栈
                    mAppContext.startActivity(intent);
                } else {
                    if (serverTip.errno == ErrorType.E_IO_EXCEPTION) {
                        EventBus.getDefault().post(new EventBusType(Constants.NET_ERROR));
                    }
                    onFailure(serverTip);
                }
                onFinish();
            }
        });
    }

    public abstract void onSuccess(ServerTip serverTip, T t);

    /**
     * 需要对错误进行特殊处理 请覆写这个方法
     *
     * @param serverTip
     */
    public void onFailure(ServerTip serverTip) {
        LogUtils.e(TAG, "Code:" + serverTip.errno() + "  Msg:" + serverTip.desc());
        if (ErrorType.isNeedTipToUser(serverTip.errno())) {
            Toast.makeText(mAppContext, serverTip.desc() + "", Toast.LENGTH_SHORT).show();
        } else {
            //112表明当前应用是最新版本
        }
    }

    /**
     * 网络请求开始
     */
    public void onStart() {

    }

    /**
     * 网络请求结束
     */
    public void onFinish() {

    }

    /**
     * data 解析类型
     */
    public enum ParseType {
        object, array
    }
}
