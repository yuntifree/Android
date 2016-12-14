package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IConnectDGCountPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.ConnectDGCountPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;
import com.yunxingzh.wireless.utils.BitmapUtils;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import u.aly.d;
import wireless.libs.bean.vo.AdvertVo;

/**
 * Created by asus_ on 2016/11/26.
 * 欢迎界面
 */

public class WelcomActivity extends BaseActivity implements IConnectDGCountView {

    boolean isFirst;
    private IConnectDGCountPresenter iConnectDGCountPresenter;
    private String url;
    public static Bitmap drawableStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        iConnectDGCountPresenter = new ConnectDGCountPresenterImpl(this);
        //实现欢迎界面的自动跳转
        Timer timer = new Timer();
        isFirst = SPUtils.get(WelcomActivity.this, "isFirst", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isFirst) {//第一次
                    SPUtils.put(WelcomActivity.this, "isFirst", false);
                    startActivity(GuidedActivity.class, "", "", "", "");
                    finish();
                } else {
                    //没网直接跳首页
                    if (NetUtils.isNetworkAvailable(WelcomActivity.this)) {
                        iConnectDGCountPresenter.getAdvert();
                    } else {
                        String imgStream = SPUtils.get(WelcomActivity.this,Constants.ADVERT_IMG,"");
                        byte[] mByte = imgStream.getBytes();

                        //把string格式的byte传至advertactivity处理？
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);  //这里 80 是图片质量，取值范围 0-100，100为品质最高
                        // byte[] jdata = baos.toByteArray();//这时候 bmp 就不为 null 了
                        // Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                        drawableStream = BitmapFactory.decodeByteArray(mByte, 0, mByte.length);
                        url = SPUtils.get(WelcomActivity.this, Constants.ADVERT_URL, "");
                        if (drawableStream != null && !StringUtils.isEmpty(url)) {
                            startActivity(AdvertActivity.class, Constants.ADVERT_URL, url, "", "");
                        } else {
                            //本地没有直接跳首页
                            startActivity(MainActivity.class, "", "", "", "");
                        }
                        finish();
                    }
                }
            }
        };
        timer.schedule(task, 1000 * 2); //2秒后
    }

    private class DownLoadImage extends AsyncTask<String, Integer, byte[]> {

        protected byte[] doInBackground(String... urls) {
            String url = urls[0];
            byte[] tmpBuffer = new byte[1024];
            HttpURLConnection connection = null;
            ByteArrayOutputStream outStream = null;
            try {
                URL httpUrl = new URL(url);
                connection = (HttpURLConnection) httpUrl.openConnection();
                connection.setConnectTimeout(5000); //超时设置
                connection.setDoInput(true);
                connection.setUseCaches(false); //设置不使用缓存
                InputStream is = new URL(url).openStream();
                outStream = new ByteArrayOutputStream();
                int len = 0;
                while ((len = is.read(tmpBuffer)) != -1) {
                    outStream.write(tmpBuffer, 0, len);
                }
                outStream.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return outStream.toByteArray();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(byte[] result) {
            SPUtils.put(WelcomActivity.this,Constants.ADVERT_IMG,result.toString());
            drawableStream = BitmapFactory.decodeByteArray(result, 0, result.length);
            if (drawableStream != null && !StringUtils.isEmpty(url)) {
                startActivity(AdvertActivity.class, Constants.ADVERT_URL, url, Constants.ADVERT_IMG, result.toString());
            } else {
                startActivity(MainActivity.class, "", "", "", "");
            }
            finish();
        }
    }

    public void startActivity(Class activity, String urlKey, String url, String byteKey, String mByte) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(urlKey, url);
        intent.putExtra(byteKey, mByte);
        startActivity(intent);
    }

    @Override
    public void getAdvertSuccess(AdvertVo advertData) {
        if (advertData != null) {
            url = advertData.target;
            new DownLoadImage().execute(advertData.img);//下载图片
            SPUtils.put(this, Constants.ADVERT_URL, advertData.target);
        }
    }

    @Override
    public void connectDGCountSuccess() {
    }
}
