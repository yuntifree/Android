package com.yunxingzh.wireless.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.fragment.BuyingFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.HeadLineFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.ServiceFragment;
import com.yunxingzh.wireless.mvp.ui.fragment.WirelessFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.UserInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.SPUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (StringUtils.isEmpty(MyApplication.sApplication.getToken())){
            startActivity(new Intent(this,RegisterActivity.class));
            finish();
            return;
        }
        initView();
        initData();
    }

    public void initView() {
        main_class_group = findView(R.id.main_class_group);
        main_class_group.setOnCheckedChangeListener(this);
    }

    public void initData() {
        fragmentManager = getSupportFragmentManager();
        wirelessFragment = new WirelessFragment();
        currentFragment = wirelessFragment;
        fragmentManager.beginTransaction().replace(R.id.main_fragment_parent, currentFragment).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.one_radio:
                if (wirelessFragment == null) {
                    wirelessFragment = new WirelessFragment();
                }
                showFragment(wirelessFragment);//无线
                break;
            case R.id.two_radio:
                if (headlineFragment == null) {
                    headlineFragment = new HeadLineFragment();
                }
                showFragment(headlineFragment);//头条
                break;
            case R.id.three_radio:
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