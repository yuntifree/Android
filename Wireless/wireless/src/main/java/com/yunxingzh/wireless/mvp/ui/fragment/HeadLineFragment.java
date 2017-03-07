package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mview.PagerSlidingTabStrip;
import com.yunxingzh.wireless.mvp.presenter.impl.GetHeadLineMenuPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetHeadLineMenuView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.MenuList;
import wireless.libs.bean.vo.MenuVo;

/**
 * Created by stephon_ on 2016/11/1.
 * 头条
 */

public class HeadLineFragment extends BaseFragment implements IGetHeadLineMenuView {

    private final static int SIZE = 15;//初始化字体size
    private final static int FOCUS_SIZE = 18;//获得焦点后字体size

    private final static int INDEX_ZERO = 0;
    private final static int INDEX_ONE = 1;
    private final static int INDEX_TWO = 2;
    private final static int INDEX_THREE = 3;
    private final static int INDEX_FOUR = 4;

    private ViewPager mViewPager;
    private MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabStrip;
    private GetHeadLineMenuPresenterImpl getHeadLineMenuPresenter;
    private List<MenuVo> menuInfos;
    private NetErrorLayout netErrorLayout;
    private LinearLayout mNetErrorLay;
    private WebViewFragment webViewFragment;
    // private MyReceiver rec;
    private int loadCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headline, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mViewPager = findView(view, R.id.id_viewpager);
        tabStrip = findView(view, R.id.tabs);
        mNetErrorLay = findView(view, R.id.net_error_news_lay);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        getHeadLineMenuPresenter = new GetHeadLineMenuPresenterImpl(this);
        if (!NetUtils.isNetworkAvailable(getActivity())) {
            netErrorState();
        }
        getHeadLineMenuPresenter.getHeadLineMenu();
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        rec = new MyReceiver();
//        //实例化过滤器并设置要过滤的广播
//        IntentFilter intentFilter = new IntentFilter("com.yunxingzh.wireless.mvp.ui.fragment");
//        //注册广播
//        getActivity().registerReceiver(rec, intentFilter);
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        if (event.getMsg() == Constants.HEAD_LINE) {//跳转到头条父fragment(头条)
            mViewPager.setCurrentItem(INDEX_ONE);
        }
        if (event.getChildMsg() == Constants.VIDEO) {
            mViewPager.setCurrentItem(INDEX_THREE);//跳转视频Fragment
        }
        if (event.getMsg() == Constants.NET_ERROR) {//网络不可用（无法上网）
            netErrorState();
        }
    }

    //接收
//    private class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            boolean iswork = intent.getBooleanExtra("isWork", false);
//            if (iswork) {
//                ToastUtil.showMiddle(getActivity(),"sssssss");
//            }
//        }
//    }

//    private void setLineSelect(boolean leftSelected, boolean rightSelected) {
//        mIdTabLeftLine.setVisibility(leftSelected ? View.VISIBLE : View.GONE);
//        mIdTabRightLine.setVisibility(rightSelected ? View.VISIBLE : View.GONE);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getHeadLineMenuPresenter != null) {
            getHeadLineMenuPresenter.onDestroy();
        }
        // getActivity().unregisterReceiver(rec);
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private void netErrorState() {
        if (netErrorLayout == null) {
            netErrorLayout = new NetErrorLayout(getActivity());
            netErrorLayout.setOnNetErrorClickListener(new NetErrorLayout.OnNetErrorClickListener() {
                @Override
                public void netErrorClick() {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), R.string.net_set);
                    } else {
                        netErrorLayout = null;
                        if (mNetErrorLay != null) {
                            mNetErrorLay.removeAllViews();
                        }
                        mNetErrorLay.setVisibility(View.GONE);
                        getHeadLineMenuPresenter.getHeadLineMenu();
                    }
                }
            });
            mNetErrorLay.setVisibility(View.VISIBLE);
            View netErrorView = netErrorLayout.netErrorLay(0);
            mNetErrorLay.addView(netErrorView);
        }
    }

    @Override
    public void getHeadLineMenuSuccess(MenuList menuList) {
        EventBus.getDefault().unregister(this);//反注册EventBus
        if (menuList != null) {
            int size = menuList.infos.size();
            menuInfos = new ArrayList<MenuVo>();
            String channelName = AppUtils.getPhoneModel();

            for (int i = 0; i < size; i++) {
                int ctype = menuList.infos.get(i).ctype;
                if (ctype == Constants.CTYPE_NEWS || ctype == Constants.CTYPE_VIDEO
                        || ctype == Constants.CTYPE_WEBVIEW || ctype == Constants.CTYPE_JOKE
                        || ctype == Constants.CTYPE_LIVE || ctype == Constants.CTYPE_EPISODE) {
                    //屏蔽小米3手机
                    if (channelName.equals("MI 3W")) {
                        if (ctype == Constants.CTYPE_LIVE) {
                            continue;
                        }
                    }
                    menuInfos.add(menuList.infos.get(i));
                }
            }

            adapter = new MyPagerAdapter(getChildFragmentManager(), menuInfos);
//            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
//                    .getDisplayMetrics());
//            mViewPager.setPageMargin(pageMargin);
            mViewPager.setAdapter(adapter);
            tabStrip.setViewPager(mViewPager);

            if (loadCount < 1) {
                loadCount++;
                String hour = StringUtils.getTime();
                int h = Integer.parseInt(hour);
                if (StringUtils.isWorkDay()) {//true为工作日
                    if (h >= 8 && h < 20) {

                    } else {
                        mViewPager.setCurrentItem(INDEX_FOUR);//跳转直播
                    }
                } else {
                    mViewPager.setCurrentItem(INDEX_FOUR);//跳转直播
                }
            }
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<MenuVo> menuVos;

        public MyPagerAdapter(FragmentManager fm, List<MenuVo> menuVos) {
            super(fm);
            this.menuVos = menuVos;
        }

        //阻止其销毁已存在的视图
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (menuVos.size() <= position) {
                LogUtils.e("headlinefragment", "out of index");
                return "";
            }
            return menuVos.get(position).title;
        }

        @Override
        public int getCount() {
            return menuVos.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (menuVos.size() <= position) {
                LogUtils.e("headlinefragment", "out of index");
                return null;
            }
            int ctype = menuVos.get(position).ctype;
            switch (ctype) {
                case Constants.CTYPE_NEWS://新闻
                    return HeadLineNewsFragment.getInstance(menuInfos.get(position).type);
                case Constants.CTYPE_VIDEO://视频
                    return new HeadLineVideoFragment();
                case Constants.CTYPE_WEBVIEW://网页
                    return argumentsFragment("http://www.baidu.com");
                case Constants.CTYPE_JOKE://搞笑
                    return new HeadLineAppFragment();
                case Constants.CTYPE_LIVE://直播
                    return new HeadLineLiveFragment();
                case Constants.CTYPE_EPISODE://段子
                    return new EpisodeFrament();
                default:
                    break;
            }
            return null;
        }
    }

    public Fragment argumentsFragment(String url) {
        if (webViewFragment == null) {
            webViewFragment = new WebViewFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }
}
