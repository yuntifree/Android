package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.PagerSlidingTabStrip;
import com.yunxingzh.wireless.mvp.presenter.impl.GetHeadLineMenuPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetHeadLineMenuView;
import com.yunxingzh.wireless.utils.LogUtils;
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

public class HeadLineFragment extends BaseFragment implements IGetHeadLineMenuView, ViewPager.OnPageChangeListener {

    private final static int SIZE = 15;//初始化字体size
    private final static int FOCUS_SIZE = 18;//获得焦点后字体size

    private final static int INDEX_ZERO = 0;
    private final static int INDEX_ONE = 1;
//    private final static int INDEX_TWO = 2;
//    private final static int INDEX_THREE = 3;

    private ViewPager mViewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private Resources res;
    private PagerSlidingTabStrip tabStrip;
    private GetHeadLineMenuPresenterImpl getHeadLineMenuPresenter;
    private List<MenuVo> menuInfos;

    private WebViewFragment webViewFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headline, null);
        //res = getResources();
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mViewPager = findView(view, R.id.id_viewpager);
        tabStrip = findView(view, R.id.tabs);
    }

    // 重置颜色
    private void resetTextView() {
//        mIdNews.setTextColor(res.getColor(R.color.blue_C0D8F4));
//        mIdNews.setTextSize(SIZE);
//        mIdVideo.setTextColor(res.getColor(R.color.blue_C0D8F4));
//        mIdVideo.setTextSize(SIZE);
        //  mIdApp.setTextColor(res.getColor(R.color.blue_C0D8F4));
        // mIdApp.setTextSize(SIZE);
        // mIdGame.setTextColor(res.getColor(R.color.blue_C0D8F4));
        //  mIdGame.setTextSize(SIZE);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        getHeadLineMenuPresenter = new GetHeadLineMenuPresenterImpl(this);
        getHeadLineMenuPresenter.getHeadLineMenu();
//        mIdNews.setOnClickListener(new TabOnClickListener(INDEX_ZERO));
//        mIdVideo.setOnClickListener(new TabOnClickListener(INDEX_ONE));
//        mIdApp.setOnClickListener(new TabOnClickListener(INDEX_TWO));
//        mIdGame.setOnClickListener(new TabOnClickListener(INDEX_THREE));
//        fragments.add(new HeadLineNewsFragment());
//        fragments.add(new HeadLineVideoFragment());
//        fragments.add(new HeadLineAppFragment());
//        fragments.add(new HeadLineGameFragment());
        //headLineFragmentAdapter = new HeadLineFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        // mViewPager.setAdapter(headLineFragmentAdapter);
        //mViewPager.setOnPageChangeListener(this);
    }

//    public class TabOnClickListener implements View.OnClickListener{
//        private int index = INDEX_ZERO;
//        public TabOnClickListener(int i){
//            index=i;
//        }
//        public void onClick(View v) {
//            mViewPager.setCurrentItem(index);//选择某一页
//        }
//    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        if (event.getMsg() == Constants.HEAD_LINE) {//跳转到头条父fragment(头条)
            mViewPager.setCurrentItem(INDEX_ZERO);
        }
        if (event.getMsg() == Constants.HEAD_LINE && event.getChildMsg() == Constants.VIDEO) {
            mViewPager.setCurrentItem(INDEX_ONE);//跳转视频Fragment
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
            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mViewPager.setPageMargin(pageMargin);
            mViewPager.setAdapter(adapter);
            tabStrip.setViewPager(mViewPager);
        }
    }

    //当前页面被滑动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {
        resetTextView();
        switch (position) {
//            case INDEX_ZERO:
//                mIdNews.setTextColor(res.getColor(R.color.white));
//                mIdNews.setTextSize(FOCUS_SIZE);
//                setLineSelect(true,false);
//                break;
//            case INDEX_ONE:
//                mIdVideo.setTextColor(res.getColor(R.color.white));
//                mIdVideo.setTextSize(FOCUS_SIZE);
//                setLineSelect(false,true);
//                break;
//            case INDEX_TWO:
//                mIdApp.setTextColor(res.getColor(R.color.white));
//                mIdApp.setTextSize(FOCUS_SIZE);
//                break;
//            case INDEX_THREE:
//                mIdGame.setTextColor(res.getColor(R.color.white));
//                mIdGame.setTextSize(FOCUS_SIZE);
//                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<String> titleList;

        public MyPagerAdapter(FragmentManager fm, List<String> titleList) {
            super(fm);
            this.titleList = titleList;
        }




//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ToastUtil.showMiddle(getActivity(),"ins");
//            HeadLineNewsFragment headLineNewsFragment = (HeadLineNewsFragment) super.instantiateItem(container, position);
//
//            return headLineNewsFragment;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return  PagerAdapter.POSITION_NONE;
//        }



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
                    return new HeadLineNewsFragment();
                case 1://视频
                    return new HeadLineVideoFragment();
                case 2://网页
                    return argumentsFragment("http://www.baidu.com");
                case 3://搞笑
                    return new HeadLineAppFragment();
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

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
