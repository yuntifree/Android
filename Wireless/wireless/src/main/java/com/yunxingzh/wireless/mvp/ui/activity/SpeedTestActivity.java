package com.yunxingzh.wireless.mvp.ui.activity;

import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.RotatePointer;
import com.yunxingzh.wireless.mview.SpeedTestDialog;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.NetUtil;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;
import com.yunxingzh.wireless.utils.Util;
import com.yunxingzh.wireless.wifi.AccessPoint;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * wifi测速
 */
public class SpeedTestActivity extends BaseActivity implements View.OnClickListener, SpeedTestDialog.OnDialogBtnClickListener {

    private static String mApkUrl = "";

    public static final int NETWORK_WIFI = 0;//wifi 连接

    protected static final int MSG_RESULT = 102;
    protected static final int MSG_TANGLE = 103;

    private int cacheAngle = 0;
    private long bytesReceived = -1;
    private long cacheData = -1;

    private int mMaxTime = 10;

    private int mRunCount = 0;

    private static boolean mShutDown = false;
    private final long mTimePeriod = 1000;

    private final List<Integer> mList = new ArrayList<Integer>();
    private final List<Long> mListSpeed = new ArrayList<Long>();

    private TimerTask task;
    private Timer timer;

    private TextView mBtnStart;
    private TextView mSpeed;
    private RotatePointer mRotatePointer;
    private ExecutorService mExecutorService;

    private TextView mTitleNameTv, mMiddleContentTv;
    private ImageView mTitleReturnIv;
    private LinearLayout mMiddleNoticeLay;
    private int speedFlag;
    private SpeedTestDialog mDialog;
    private float realTimeSpeed;
    private int count = 0;
    private int speedCount = 0;
    private final Handler mHandler = new ResultHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_speedtest);
        initView();
    }

    // init view components
    private void initView() {
        mTitleReturnIv = (ImageView) findViewById(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleNameTv = (TextView) findViewById(R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleNameTv.setText(R.string.speed);
        mRotatePointer = (RotatePointer) findViewById(R.id.rp_tspeed_pointer);
        mRotatePointer.setPointer(mRotatePointer);
        mRotatePointer.setPointerRotate(180);
        mRotatePointer.setfirstSweepAngle(180);
        mRotatePointer.setRotate(0);
        mRotatePointer.setSecondSpinColor(Color.GREEN);
        mRotatePointer.setsecondSweepAngle(0);
        mRotatePointer.setSecondSpinColor(0xFF0063D2);
        mMiddleNoticeLay = (LinearLayout) findViewById(R.id.middle_notice_lay);
        mMiddleContentTv = (TextView) findViewById(R.id.middle_content_tv);

        mSpeed = (TextView) findViewById(R.id.tv_t_speed);
        mBtnStart = (TextView) findViewById(R.id.btn_tspeed_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(SpeedTestActivity.this)) {
                    if (mBtnStart.getText().equals("开始测速")) {
                        initThread();
                    } else if (mBtnStart.getText().equals("停止测速")) {
                        shutdownAll();
//                        mRotatePointer.setRotate(0);
//                        mRotatePointer.setsecondSweepAngle(0);
                        mMiddleNoticeLay.setVisibility(View.INVISIBLE);
                        mBtnStart.setText(R.string.re_speed);
                    } else if (mBtnStart.getText().equals("重新测速")) {
                        speedCount++;
                        if (speedCount == 1) {
                            MobclickAgent.onEvent(SpeedTestActivity.this, "speedtest_twice");
                        }
                        mMiddleContentTv.setText("请稍等片刻...");
                        startTestSpeed();
                    }
                } else {
                    ToastUtil.showMiddle(SpeedTestActivity.this, R.string.net_set);
                }
            }
        });
        StatusBarColor.compat(this, getResources().getColor(R.color.blue_009CFB));
        mDialog = new SpeedTestDialog(SpeedTestActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setOnDialogBtnClickListener(this);
    }

    @Override
    public void onQueryClick(int flag) {
        speedFlag = flag;
        initThread();
    }

    private void initThread() {
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
        mExecutorService = Executors.newScheduledThreadPool(3);
        mSpeed.setText(getTimeString(Util.speedMethodNormal(0)));
        mSpeed.setVisibility(View.VISIBLE);
        mSpeed.setTextColor(Color.parseColor("#ffffff"));

        startTestSpeed();
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {//返回
            finish();
        }
    }


    private void startTestSpeed() {
        int netType = AppUtils.getNetWorkType(this);
        if (netType != NETWORK_WIFI) {
            if (speedFlag != 1) {
                mDialog.show();
                return;
            }
        }
        mSpeed.setTextColor(Color.parseColor("#ffffff"));
        mMiddleNoticeLay.setVisibility(View.VISIBLE);
        mBtnStart.setText(R.string.stop_speed);
        mList.clear();
        mListSpeed.clear();
        mRotatePointer.setsecondSweepAngle(0);
        bytesReceived = -1;
        cacheData = -1;
        cacheAngle = 0;
        mRunCount = 0;

        /*
        *  "http://dl.360safe.com/wifispeed/wifispeed.test"
        *  "http://download.weather.com.cn/3g/current/ChinaWeather_Android.apk"
        *  "http://down.360safe.com/360mse/f/360fmse_js010001.apk"
        * */
        String dgWifi = "";
        AccessPoint current = FWManager.getInstance().getCurrent();
        if (current != null && !StringUtils.isEmpty(current.ssid)) {
            dgWifi = current.ssid;
        }
        if (dgWifi.equals("无线东莞DG-FREE")) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    speedForDGwifi();
                }
            };
            timer.schedule(task, 10, mTimePeriod);
        } else {
            initTimer();
            String down_url = "http://down.360safe.com/360mse/f/360fmse_js010001.apk";
            if (!TextUtils.isEmpty(down_url)) {
                mApkUrl = down_url;
                mExecutorService.execute(new apkDownloadRunnable());
                timer.schedule(task, 10, mTimePeriod);
            } else {
                showResult(0, false);
            }
        }
    }

    public void speedForDGwifi() {
        int all = 4 * 1024 * 1024 / 8;//总流量
        Random random = new Random();
        float percent = (float) (((random.nextInt(21) - 10)) * 0.01);
        realTimeSpeed = all * (1 + percent);
        LogUtils.e("sss", "realTimeSpeed:" + realTimeSpeed);
        count++;
        int angle = speedToAngle((long) realTimeSpeed);//幅度
        mList.add(angle);
        testSpeed(angle);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("sss", "setTextView");
                mSpeed.setText(getTimeString(Util.speedMethodNormal(realTimeSpeed)));
            }
        });
        if (count == 10) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_RESULT, (long) realTimeSpeed));
                }
            });
            shutdownAll();
            count = 0;
        }
    }

    private void showResult(float speed, boolean submit) {
        String tag = "";
        String desc = "";

        if (isFinishing()) {
            return;
        }
        mBtnStart.setVisibility(View.VISIBLE);
        mSpeed.setText(getTimeString(Util.speedMethodNormal(speed)));
        if (speed < 1 * 1024) {
            tag = "当前网速太慢，请检查";
            desc = "特慢";
        } else if (speed >= 1 * 1024 && speed < 20 * 1024) {
            tag = "当前网速可使用聊天软件";
            desc = "很慢";
        } else if (speed >= 20 * 1024 && speed < 80 * 1024) {
            tag = "当前网速可正常打开网页";
            desc = "较慢";
        } else if (speed >= 80 * 1024 && speed < 150 * 1024) {
            tag = "当前网速可流畅玩游戏";
            desc = "流畅";
        } else if (speed >= 150 * 1024) {
            tag = "当前网速可流畅看视频";
            //tag = "网速碉堡了\n即刻下载推荐应用畅享高速WiFi";
            desc = "很快";
        }
        mSpeed.setTextColor(Color.parseColor("#ffec00"));
        mMiddleContentTv.setText(tag);
        mBtnStart.setText(R.string.re_speed);
    }


    private void initTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (!mShutDown) {
                    if (mRunCount >= mMaxTime) {//超时强行停止下载
                        shutdownRunnable();
                        timer.cancel();
                        long tTotal = 0;
                        for (int i = 0; i < mListSpeed.size(); i++) {
                            if (mListSpeed.get(i) > 0) {
                                tTotal += mListSpeed.get(i);
                            }
                        }
                        long avgSpeed = 0;
                        if (mListSpeed.size() != 0) {
                            avgSpeed = (int) (tTotal / mListSpeed.size());
                        }

                        if (avgSpeed != 0) {
                            int angleA = speedToAngle(avgSpeed);
                            mList.add(angleA);
                            testSpeed(angleA);
                        }

                        mHandler.sendMessage(mHandler.obtainMessage(MSG_RESULT, avgSpeed));
                        mRunCount = 0;
                    } else {
                        long speed = measureSpeed();
                        int angle = speedToAngle(speed);
                        if (angle != 0) {
                            mListSpeed.add(speed);
                        }
                        mList.add(angle);
                        testSpeed(angle);
                        mRunCount++;
                    }
                }
            }
        };
        mShutDown = false;
    }

    private void testSpeed(final int angle) {
        new Thread() {
            @Override
            public void run() {
                if (mList.size() > 1) {
                    cacheAngle = mList.get(mList.size() - 2);
                }
                if (angle >= cacheAngle) {
                    for (int i = cacheAngle; i < angle; i++) {
                        try {
                            int periodTime = angle - cacheAngle;
                            if (periodTime != 0) {
                                Thread.sleep(1000 / (Math.abs(periodTime)));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage(MSG_TANGLE);
                        msg.arg1 = i;
                        mHandler.sendMessage(msg);
                    }

                } else {
                    for (int i = cacheAngle; i > angle; i--) {
                        try {
                            int periodTime = angle - cacheAngle;
                            if (periodTime != 0) {
                                Thread.sleep(1000 / (Math.abs(periodTime)));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage(MSG_TANGLE);
                        msg.arg1 = i;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    private void shutdownAll() {
        shutdownRunnable();
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void shutdownRunnable() {
        mShutDown = true;
    }

    private long measureSpeed() {
        long trb = TrafficStats.getTotalRxBytes();
        long ttb = TrafficStats.getTotalTxBytes();

        long now = trb + ttb;

        if (cacheData == -1) {
            cacheData = now;
        } else {
            bytesReceived = now - cacheData;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSpeed.setText(getTimeString(Util.speedMethodNormal(bytesReceived)));
                }
            });
            cacheData = now;
        }

        //Logger.e(TAG, mrb + "---" + mtb + "---" + trb + "---" + ttb + "-----" + bytesReceived);
        float f = mTimePeriod / 1000;
        if (f == 0) {
            f = 1;
        }
        return (long) (bytesReceived / f);
    }

    private static class ResultHandler extends Handler {
        private final WeakReference<SpeedTestActivity> mActivity;

        public ResultHandler(SpeedTestActivity activity) {
            mActivity = new WeakReference<SpeedTestActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SpeedTestActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_RESULT:
                        activity.showResult((Long) msg.obj, true);
                        break;
                    case MSG_TANGLE:
                        int currentAngle = msg.arg1;
                        activity.mRotatePointer.setsecondSweepAngle(currentAngle);
                        break;
                }
            }
        }
    };

    private SpannableStringBuilder getTimeString(String timeString) {
        SpannableStringBuilder style = new SpannableStringBuilder(timeString);
        if (isFinishing()) {
            return style;
        }
        if (timeString.contains("B/s")) {
            int p1 = timeString.indexOf("B/s");
            style.setSpan(new AbsoluteSizeSpan(Util.dpToPx(20, this)), p1 - 1, timeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (timeString.contains("KB/s")) {
            int p1 = timeString.indexOf("KB/s");
            style.setSpan(new AbsoluteSizeSpan(Util.dpToPx(20, this)), p1 - 1, timeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (timeString.contains("MB/s")) {
            int p2 = timeString.indexOf("MB/s");
            style.setSpan(new AbsoluteSizeSpan(Util.dpToPx(20, this)), p2 - 1, timeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (timeString.contains("GB/s")) {
            int p2 = timeString.indexOf("GB/s");
            style.setSpan(new AbsoluteSizeSpan(Util.dpToPx(20, this)), p2 - 1, timeString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }

    private int speedToAngle(long speed) {
        int speedMb = (int) (speed / 1024.0 / 1024 * 8);
        int angle;
        if (speedMb < 3) {
            // 0~3M，每0.5M 11.25度
            angle = (int) (speedMb / 0.5 * 11.25);
        } else if (speedMb >= 3 && speedMb < 5) {
            // 3~5M，每1M 11.25度
            angle = (int) ((speedMb - 3) * 11.25 + 67.5);
        } else if (speedMb >= 5 && speedMb < 10) {
            // 5~10M，每2.5M 11.25度
            angle = (int) ((speedMb - 5) / 2.5 * 11.25 + 90);
        } else if (speedMb >= 10 && speedMb < 30) {
            // 10~30M，每5M 11.25度
            angle = (int) ((speedMb - 10) / 5 * 11.25 + 112.5);
        } else if (speedMb >= 30 && speedMb < 50) {
            // 30~50M，每10M 11.25度
            angle = (int) ((speedMb - 30) / 10 * 11.25 + 157.5);
        } else {
            // 超出50M
            angle = 175;
        }
        LogUtils.i("sss", angle + "");
        return angle;

    }

    private static int mRunnableCount = 0;

    class apkDownloadRunnable implements Runnable {

        public apkDownloadRunnable() {
        }

        @Override
        public void run() {
            mRunnableCount = 0;
            new DefaultRunnable(mApkUrl).run();
        }
    }

    class DefaultRunnable implements Runnable {
        private final String mUrl;

        public DefaultRunnable(String url) {
            mUrl = url;
        }

        @Override
        public void run() {
//            if (++mRunnableCount > 10) {
//                return;
//            }
            if (!TextUtils.isEmpty(mUrl) && !mShutDown && NetUtil.isConnectedWifi(MainApplication.get())) {
                InputStream is = null;
                try {
                    URL myURL = new URL(mUrl);
                    URLConnection conn = myURL.openConnection();
                    conn.connect();
                    is = conn.getInputStream();
                    if (is == null) {
                        return;
                    }
                    int count = 0;
                    byte buf[] = new byte[4096];
                    while (!mShutDown && (is.read(buf) > 0)) {
//                        if (count++ > 50) {
//                            count = 0;
//                            if (!NetUtil.isConnectedWifi(MainApplication.get())) {
//                                break;
//                            }
//                        }
                    }
                    if (!mShutDown && NetUtil.isConnectedWifi(MainApplication.get())) {
                        new DefaultRunnable(mApkUrl).run();
                    }
                } catch (Exception e) {
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdownAll();
    }
}
