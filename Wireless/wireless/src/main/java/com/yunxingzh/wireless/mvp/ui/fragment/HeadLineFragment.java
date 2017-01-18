package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
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
import com.yunxingzh.wireless.utils.NetUtils;
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

public class HeadLineFragment extends BaseFragment implements IGetHeadLineMenuView, NetErrorLayout.OnNetErrorClickListener {

    private final static int SIZE = 15;//初始化字体size
    private final static int FOCUS_SIZE = 18;//获得焦点后字体size

    private final static int INDEX_ZERO = 0;
    private final static int INDEX_ONE = 1;
    private final static int INDEX_TWO = 2;
    private final static int INDEX_THREE = 3;

    private ViewPager mViewPager;
    private MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabStrip;
    private GetHeadLineMenuPresenterImpl getHeadLineMenuPresenter;
    private List<MenuVo> menuInfos;
    private NetErrorLayout netErrorLayout;
    private LinearLayout mNetErrorLay;
    private WebViewFragment webViewFragment;

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
            netErrorLayout = new NetErrorLayout(getActivity());
            netErrorLayout.setOnNetErrorClickListener(this);
            mNetErrorLay.setVisibility(View.VISIBLE);
            View netErrorView = netErrorLayout.netErrorLay(0);
            mNetErrorLay.addView(netErrorView);
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
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        if (event.getMsg() == Constants.HEAD_LINE) {//跳转到头条父fragment(头条)
            mViewPager.setCurrentItem(INDEX_ONE);
        }
        if (event.getChildMsg() == Constants.VIDEO) {
            mViewPager.setCurrentItem(INDEX_THREE);//跳转视频Fragment
        }
    }

//    private void setLineSelect(boolean leftSelected, boolean rightSelected) {
//        mIdTabLeftLine.setVisibility(leftSelected ? View.VISIBLE : View.GONE);
//        mIdTabRightLine.setVisibility(rightSelected ? View.VISIBLE : View.GONE);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void getHeadLineMenuSuccess(MenuList menuList) {
        if (menuList != null) {
            int size = menuList.infos.size();
            menuInfos = menuList.infos;
            List<String> titleList = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                titleList.add(menuInfos.get(i).title);
            }
            adapter = new MyPagerAdapter(getChildFragmentManager(), titleList);
//            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
//                    .getDisplayMetrics());
//            mViewPager.setPageMargin(pageMargin);
            mViewPager.setAdapter(adapter);
            tabStrip.setViewPager(mViewPager);
        }
    }

    @Override
    public void netErrorClick() {
        if (!NetUtils.isNetworkAvailable(getActivity())) {
            ToastUtil.showMiddle(getActivity(), R.string.net_set);
        } else {
            mNetErrorLay.setVisibility(View.GONE);
            getHeadLineMenuPresenter.getHeadLineMenu();
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<String> titleList) {
            super(fm);
            this.titleList = titleList;
        }

        //阻止其销毁已存在的视图
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public Fragment getItem(int position) {
            int ctype = menuInfos.get(position).ctype;
            switch (ctype){
                case 0://新闻
                    return HeadLineNewsFragment.getInstance(menuInfos.get(position).type);
                case 1://视频
                    return new HeadLineVideoFragment();
                case 2://网页
                    return argumentsFragment("http://www.baidu.com");
                case 3://搞笑
                    return new HeadLineAppFragment();
                default:
                    break;
            }
            return null;
        }
    }

    public Fragment argumentsFragment(String url){
        if (webViewFragment == null) {
            webViewFragment = new WebViewFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }
}
