package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.fragment.BuyingFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineNewsFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineVideoFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.ServiceFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.WirelessFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/***
 * 首页底部导航
 */

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private final static int SECONDS = 2000;//按下的间隔秒数
    private final static int STATUS = 0;//0 正常结束程序;1 异常关闭程序

    private RadioGroup main_class_group;
    // private BuyingFragment buyingFragment;
    private HeadLineFragment headlineFragment;
    private ServiceFragment serviceFragment;
    private WirelessFragment wirelessFragment;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private long exitTime = 0;
    private RadioButton mHeadLineRadio, mWirelessRadio, mServiceRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (StringUtils.isEmpty(MyApplication.sApplication.getToken()) || MyApplication.sApplication.needLogin()) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
            return;
        }
        initView();
        initData();
    }

    public void initView() {
        main_class_group = findView(R.id.main_class_group);
        main_class_group.setOnCheckedChangeListener(this);
        mHeadLineRadio = findView(R.id.head_line_radio);
        mWirelessRadio = findView(R.id.wireless_radio);
        mServiceRadio = findView(R.id.service_radio);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);

        fragmentManager = getSupportFragmentManager();
        wirelessFragment = new WirelessFragment();
        currentFragment = wirelessFragment;
        fragmentManager.beginTransaction().replace(R.id.main_fragment_parent, currentFragment).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.wireless_radio:
                if (wirelessFragment == null) {
                    wirelessFragment = new WirelessFragment();
                }
                showFragment(wirelessFragment);//无线
                break;
            case R.id.head_line_radio:
                if (headlineFragment == null) {
                    headlineFragment = new HeadLineFragment();
                }
                showFragment(headlineFragment);//头条
                break;
            case R.id.service_radio:
                if (serviceFragment == null) {
                    serviceFragment = new ServiceFragment();
                }
                showFragment(serviceFragment);//服务
                break;
//            case R.id.four_radio:
//                if (buyingFragment == null) {
//                    buyingFragment = new BuyingFragment();
//                }
//                showFragment(buyingFragment);//抢购
//                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        //跳转头条（新闻）
        if (event.getMsg() == Constants.HEAD_LINE) {
            mHeadLineRadio.setChecked(true);
        }

        //首页先跳转到头条父fragment再指定viewpager中的setCurrentItem
        if (event.getMsg() == Constants.HEAD_LINE && event.getChildMsg() == Constants.VIDEO) {
            mHeadLineRadio.setChecked(true);
        }

        //跳转服务
        if (event.getMsg() == Constants.SERVICE) {
            mServiceRadio.setChecked(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
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
//        mTwoRadio.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.news),null,null);

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > SECONDS) {
                ToastUtil.showMiddle(this, R.string.enter_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(STATUS);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
