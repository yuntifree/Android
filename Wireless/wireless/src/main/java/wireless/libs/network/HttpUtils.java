package wireless.libs.network;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.yunxingzh.wireless.config.AppConfig;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wireless.libs.bean.resp.ServerTip;


/**
 * 对于网络请求框架的封装
 *
 * @author jerry
 * @date 2015/05/24
 * 修改 andy
 * 时间 20160505
 *
 * 修订: 2016/06/20 carlos
 * 1. 删除请求前判断断网后的 return语句.
 */
public class HttpUtils {

    /**
     * 连接超时
     */
    private static final long CONNECT_TIMEOUT_MILLIS = 5 * 1000;

    /**
     * 读取超时
     */
    private static final long READ_TIMEOUT_MILLIS = 5 * 1000;

    /**
     * 写入超时
     */
    private static final long WRITE_TIMEOUT_MILLIS = 5 * 1000;

    // 同步请求超时
    private static final long SYNC_TIMEOUT_MILLIS = 1500;

    /**
     * tag
     */
    public final static String TAG = HttpUtils.class.getSimpleName();

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * OkHttpClient实例
     */
    private static OkHttpClient client;
    private static OkHttpClient syncClient;

    private static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder().addInterceptor(new RetryInterceptor())
                    .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
        }
        return client;
    }

    private static OkHttpClient getSyncClient() {
        if (syncClient == null) {
            syncClient = new OkHttpClient.Builder().addInterceptor(new RetryInterceptor())
                    .connectTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .readTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .writeTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
        }
        return syncClient;
    }

    /**
     * 默认处理
     */
    private final static HttpHandler nullHttpHandler = new HttpHandler() {
        @Override
        public void onSuccess(ServerTip serverTip, Object o) {
            LogUtils.d(TAG, o + "");
        }
    };

    /**
     * 包装Post的参数
     * @param url
     * @param params
     * @return
     */
    private static Request getPostRequest(String url, HttpParams params, Object tag) {
        //处理url
        url = handleurl(url);
        //获取参数
        String paramsJson = params.toJson();
        LogUtils.i(TAG, "Request:\n" + url + "\n" + paramsJson);
        RequestBody requestBody = RequestBody.create(JSON, paramsJson);
        Request request = new Request.Builder().url(url).post(requestBody).tag(tag).build();
        return request;
    }

    /**
     * 封装的Post请求
     *
     * @param url     URL的全路径或子路径(即: 除去url公共前缀之后的那部分路径)
     * @param params
     * @param handler
     */
    public static void post(String url, HttpParams params, final HttpHandler handler) {
        Request request = getPostRequest(url, params, null);
        getClient().newCall(request).enqueue(handler != null ? handler : nullHttpHandler);
        notifyHandlerStart(handler);
    }

    /**
     * 带Tag的post请求
     * 方便撤销
     *
     * @param url
     * @param params
     * @param tag
     * @param handler
     */
    public static void tagPost(String url, HttpParams params, Object tag, final HttpHandler handler) {
        if (!NetUtils.isNetworkAvailable(MainApplication.get())) {
            ToastUtil.show("请检查网络～");
        }
        Request request = getPostRequest(url, params, tag);
        getClient().newCall(request).enqueue(handler != null ? handler : nullHttpHandler);
        notifyHandlerStart(handler);
    }

    public static Response tagPostSync(String url, HttpParams params, Object tag) {
        try {
            Request request = getPostRequest(url, params, tag);
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步post请求
     *
     * @param url
     * @param params
     */
    public static String postSync(String url, final HttpParams params) {
        Request request = getPostRequest(url, params, null);
        try {
            Response response = getSyncClient().newCall(request).execute();
            final String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCompleteUrl(String addurl) {
        if (AppConfig.DEV_MODEL) {
            return AppConfig.DEV_URL + addurl;
        } else {
            return AppConfig.API_URL + addurl;
        }
    }

    public static String handleurl(String url) {
        //判断是否为完整地址，如果不是，自动指向app服务器并补全
        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = getCompleteUrl(url);
        }
        return url;
    }
//
//    /**
//     * 撤销请求
//     *
//     * @param tag
//     */
//    public static void cancelRequest(Object tag) {
//        getClient().cancel(tag);
//    }

    /**
     * 通知handler请求已经开始
     *
     * @param handler
     */
    static void notifyHandlerStart(final HttpHandler handler) {
        if (handler != null) {
            MainApplication.runUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onStart();
                }
            });
        }
    }

    public static void getNGBRet(String url, String ws_url, int num,int type, Callback handler) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("WS_URL", ws_url)
                .addHeader("WS_RETIP_NUM", String.valueOf(num))
                .addHeader("WS_URL_TYPE", String.valueOf(type))
                .build();
        getClient().newCall(request).enqueue(handler != null ? handler : nullHttpHandler);
    }

    /**
     * -1:未知异常，0：无网络，1：无须认证的网络（可以上网），2：须要认证的网络
     * @param url
     * @return
     */
    public static int getReqForDGWifi(String url) {
        URL httpUrl = null;
        HttpURLConnection conn = null;
        int result = -1;
        try {
            httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setConnectTimeout(2 * 1000);
            conn.setReadTimeout(2 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            LogUtils.e("lsd", conn.getResponseMessage()+"");
            int code = conn.getResponseCode();
            if (code >= 300 && code < 400) {//发生重定向
                String agr = conn.getHeaderField("Location");//获取重定向后的地址
                if (!StringUtils.isEmpty(agr) && agr.contains("wlanacname") && agr.contains("ssid") && agr.contains("wlanuserip")) {//是DGFree，需要认证
                    parseUrl(agr);
                    result= 2;
                }
            } else if (code >= 200 && code < 300) {//可以上网，无须认证
                result = 1;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            result = 0;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * 解析出url参数并保存
     * @param url
     */
    public static void parseUrl(String url){
        String[] agrArray = url.trim().split("\\?");
        String[] mAgr = agrArray[1].split("&");

        for(String strSplit : mAgr) {
            String[] arrSplit;
            arrSplit = strSplit.split("=");
            //解析出键值
            if (arrSplit.length > 1) {
                //正确解析
                SPUtils.putForAgr(arrSplit[0], arrSplit[1]);
            } else {
                if (arrSplit[0] != "") {
                    //只有参数没有值，不加入
                }
            }
        }
    }

    /**
     *
     * @param url
     * @param ws_url
     * @param num
     * @return
     */
    public static String getNGBRet(String url, String ws_url, int num) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setConnectTimeout(2 * 1000);
            conn.setReadTimeout(2 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            conn.addRequestProperty("WS_URL", ws_url);
            conn.addRequestProperty("WS_RETIP_NUM", String.valueOf(num));
            conn.addRequestProperty("WS_URL_TYPE", "3");

            conn.connect();
            InputStream input = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            return stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重试策略
     */
    public static class RetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Response response = chain.proceed(original);
            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 2) {
                Log.d(TAG, "Retry " + tryCount++);
                response = chain.proceed(original);
            }
            return response;
        }
    }


}
