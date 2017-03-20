package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.AppConfig;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.CheckUpdateDialog;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.impl.GetAdvertPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.MineFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.ServiceFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.WirelessFragment;
import com.yunxingzh.wireless.mvp.view.IGetAdvertView;
import com.yunxingzh.wireless.utils.FileUtil;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.UpdateVo;
import wireless.libs.bean.vo.User;

/***
 * 首页底部导航
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, IGetAdvertView {

    private final static int SECONDS = 2000;//按下的间隔秒数
    private final static int STATUS = 0;//0 正常结束程序;1 异常关闭程序

    private RadioGroup main_class_group;
    private HeadLineFragment headlineFragment;
    private ServiceFragment serviceFragment;
    private WirelessFragment wirelessFragment;
    private MineFragment mineFragment;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private long exitTime = 0;
    private RadioButton mHeadLineRadio, mWirelessRadio, mServiceRadio, mMineRadio;
    private GetAdvertPresenterImpl getAdvertPresenter;

    private String url;
    private Bitmap drawableStream;
    private String path = Environment.getExternalStorageDirectory().toString() + "/advert_img";
    private CheckUpdateDialog checkUpdateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AppConfig.DEV_MODEL) {
            //听云
            NBSAppAgent.setLicenseKey("87fb7caacc08462a8aecd82cb1c6d4fd").withLocationServiceEnabled(true).start(this.getApplicationContext());

        }
        if (StringUtils.isEmpty(MainApplication.get().getToken()) || MainApplication.get().needLogin()) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        }

        initView();
        initData();

        //小米推送服务
        //设置标签
        User user = MainApplication.get().getUser();
        if (user != null) {
            if (user.pushtest == 1) {
                MiPushClient.subscribe(MainApplication.get(), "yuntitest", null);
            } else {
                MiPushClient.subscribe(MainApplication.get(), "yuntifree", null);
            }
            //设置别名
            MiPushClient.setAlias(MainApplication.get(), user.uid + "", null);
        }
    }

    public void initView() {
        main_class_group = findView(R.id.main_class_group);
        main_class_group.setOnCheckedChangeListener(this);
        mHeadLineRadio = findView(R.id.head_line_radio);
        mWirelessRadio = findView(R.id.wireless_radio);
        mServiceRadio = findView(R.id.service_radio);
        mMineRadio = findView(R.id.stretch_radio);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        getAdvertPresenter = new GetAdvertPresenterImpl(this);
        fragmentManager = getSupportFragmentManager();
        wirelessFragment = new WirelessFragment();
        currentFragment = wirelessFragment;
        fragmentManager.beginTransaction().replace(R.id.main_fragment_parent, currentFragment).commit();
        getStatusOnTimes();

        getAdvertPresenter.getAdvert();// 拉取广告
        getAdvertPresenter.checkUpdate();//检查版本更新
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.wireless_radio:
                MobclickAgent.onEvent(this, "tab_Index");
                if (wirelessFragment == null) {
                    wirelessFragment = new WirelessFragment();
                }
                getStatusOnTimes();
                Constants.FRAGMENT = Constants.WIRELESS_FLAG;
                showFragment(wirelessFragment);//无线
                break;
            case R.id.head_line_radio:
                MobclickAgent.onEvent(this, "tab_entertainment");
                if (headlineFragment == null) {
                    headlineFragment = new HeadLineFragment();
                }
                getStatusBarColor(R.color.blue_009CFB);
                Constants.FRAGMENT = Constants.HEADLINE_FLAG;
                showFragment(headlineFragment);//头条
//                Intent intent = new Intent();
//                intent.putExtra("isWork", true);
//                intent.setAction("com.yunxingzh.wireless.mvp.ui.fragment");
//                sendBroadcast(intent);
                break;
            case R.id.service_radio:
                MobclickAgent.onEvent(this, "tab_life");
                if (serviceFragment == null) {
                    serviceFragment = new ServiceFragment();
                }
                getStatusBarColor(R.color.blue_009CFB);
                Constants.FRAGMENT = Constants.SERVICE_FLAG;
                showFragment(serviceFragment);//服务
                break;
            case R.id.stretch_radio:
                MobclickAgent.onEvent(this, "tab_me");
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                getStatusBarColor(R.color.blue_009CFB);
                Constants.FRAGMENT = Constants.MINE_FLAG;
                showFragment(mineFragment);//我
                break;
        }
    }

    public void getStatusOnTimes() {
        //不同时间段状态栏显示的颜色
        String hour = StringUtils.getTime();
        int h = Integer.parseInt(hour);
        if (h >= 6 && h < 19) {
            getStatusBarColor(R.color.blue_009CFB);
        } else {
            getStatusBarColor(R.color.blue_236EC5);
        }
    }

    public void getStatusBarColor(int colorId) {
        StatusBarColor.compat(this, getResources().getColor(colorId));
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        //跳转头条（新闻）
        if (event.getMsg() == Constants.HEAD_LINE) {
            mHeadLineRadio.setChecked(true);
        }

//        //跳转视频-首页先跳转到头条父fragment再指定viewpager中的setCurrentItem
//        if (event.getChildMsg() == Constants.VIDEO) {
//            mHeadLineRadio.setChecked(true);
//        }
//
//        //跳转服务
//        if (event.getMsg() == Constants.SERVICE) {
//            mServiceRadio.setChecked(true);
//        }

        //跳转我
        if (event.getMsg() == Constants.MINE) {
            mMineRadio.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        if (drawableStream != null && !drawableStream.isRecycled()) {
            drawableStream.recycle();
            drawableStream = null;
        }
        if (getAdvertPresenter != null) {
            getAdvertPresenter.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //点击活动后返回上一次停留的界面
        switch (Constants.FRAGMENT) {
            case Constants.WIRELESS_FLAG:
                mWirelessRadio.setChecked(true);
                break;
            case Constants.HEADLINE_FLAG:
                mHeadLineRadio.setChecked(true);
                break;
            case Constants.SERVICE_FLAG:
                mServiceRadio.setChecked(true);
                break;
            case Constants.MINE_FLAG:
                mMineRadio.setChecked(true);
                break;
            default:
                mWirelessRadio.setChecked(true);
                break;
        }
    }

    public Fragment showFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                ft.hide(currentFragment).add(R.id.main_fragment_parent, fragment).commit();
            } else {
                ft.hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
        return currentFragment;
    }

    //代码实现控件添加drawableTop
//  mTwoRadio.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.news),null,null);

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > SECONDS) {
                ToastUtil.showMiddle(this, R.string.enter_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                MobclickAgent.onKillProcess(this);
                System.exit(STATUS);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void getAdvertSuccess(AdvertVo advertData) {
        boolean removeAd = false;
        if (advertData != null) {
            if (!StringUtils.isExpired(advertData.expire)) {//没有过期
                SPUtils.put(this, Constants.ADVERT_DATE, advertData.expire);
                url = SPUtils.get(MainApplication.get(), Constants.ADVERT_URL, "");

                SPUtils.put(MainApplication.get(), Constants.ADVERT_IMG_URL, advertData.img);
                String mPath = SPUtils.get(MainApplication.get(), Constants.ADVERT_IMG_URL, "");

                //因为dst字段有可能不存在，所以不能放到下面进行判断
                if (!advertData.img.equals(mPath)) {
                    SPUtils.put(MainApplication.get(), Constants.ADVERT_URL, advertData.dst);
                    SPUtils.put(MainApplication.get(), Constants.TITLE, advertData.title);
                    new DownLoadFile().execute(advertData.img);//下载图片
                }

                if (advertData.dst != null) {//如果活动地址为空
                    if (!(advertData.dst.equals(url) && FileUtil.isFileExist(path))) {
                        SPUtils.put(MainApplication.get(), Constants.ADVERT_URL, advertData.dst);
                        SPUtils.put(MainApplication.get(), Constants.TITLE, advertData.title);
                        new DownLoadFile().execute(advertData.img);//下载图片
                    }
                } else {
                    SPUtils.put(MainApplication.get(), Constants.ADVERT_URL, "");
                    SPUtils.put(MainApplication.get(), Constants.TITLE, advertData.title);
                    new DownLoadFile().execute(advertData.img);//下载图片
                }
            } else {
                // 广告到期
                removeAd = true;
            }
        } else {
            // 广告下线
            removeAd = true;
        }
        if (removeAd) {
            //如果广告下线或者到期，并清空本地数据
            SPUtils.remove(MainApplication.get(), Constants.ADVERT_URL);
            SPUtils.remove(MainApplication.get(), Constants.ADVERT_IMG);
            SPUtils.remove(MainApplication.get(), Constants.ADVERT_DATE);
        }
    }

    @Override
    public void checkUpdateSuccess(UpdateVo updateVo) {
        if (updateVo != null) {
            checkUpdateDialog = new CheckUpdateDialog(this, updateVo);
            checkUpdateDialog.show();
        }
    }

    private class DownLoadFile extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            String url = urls[0];
            HttpURLConnection conn;
            InputStream is;
            String ret = "";
            try {
                URL httpUrl = new URL(url);
                conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setConnectTimeout(5 * 1000); //超时设置
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = new URL(url).openStream();
                    // writeFile里面close了is
                    if (FileUtil.writeFile(path, is)) {
                        ret = path;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            if (!StringUtils.isEmpty(result)) {
                SPUtils.put(MainApplication.get(), Constants.ADVERT_IMG, path);//保存文件路径
            } else {
                LogUtils.e("lsd", "写入文件失败");
            }
        }
    }
}
