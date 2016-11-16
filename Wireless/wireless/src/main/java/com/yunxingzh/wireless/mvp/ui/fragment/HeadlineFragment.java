package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineFragmentPagerAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 头条
 */

public class HeadLineFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private final static int SIZE = 15;//初始化字体size
    private final static int FOCUS_SIZE = 18;//获得焦点后字体size

    private final static int INDEX_ZERO = 0;
    private final static int INDEX_ONE = 1;
//    private final static int INDEX_TWO = 2;
//    private final static int INDEX_THREE = 3;

    private TextView mIdApp,mIdGame,mIdVideo,mIdNews;
    private ViewPager mViewPager;
    private HeadLineFragmentPagerAdapter headLineFragmentAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private Resources res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headline, null);
        res = getResources();
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mViewPager = findView(view,R.id.id_viewpager);
       // mIdApp = findView(view,R.id.id_app);
       // mIdGame = findView(view,R.id.id_game);
        mIdVideo = findView(view,R.id.id_video);
        mIdNews = findView(view,R.id.id_news);
    }

    // 重置颜色
    private void resetTextView() {
        mIdNews.setTextColor(res.getColor(R.color.blue_C0D8F4));
        mIdNews.setTextSize(SIZE);
        mIdVideo.setTextColor(res.getColor(R.color.blue_C0D8F4));
        mIdVideo.setTextSize(SIZE);
      //  mIdApp.setTextColor(res.getColor(R.color.blue_C0D8F4));
       // mIdApp.setTextSize(SIZE);
       // mIdGame.setTextColor(res.getColor(R.color.blue_C0D8F4));
      //  mIdGame.setTextSize(SIZE);
    }

    public void initData() {
        mIdNews.setOnClickListener(new TabOnClickListener(INDEX_ZERO));
        mIdVideo.setOnClickListener(new TabOnClickListener(INDEX_ONE));
//        mIdApp.setOnClickListener(new TabOnClickListener(INDEX_TWO));
//        mIdGame.setOnClickListener(new TabOnClickListener(INDEX_THREE));
        fragments.add(new HeadLineNewsFragment());
        fragments.add(new HeadLineVideoFragment());
//        fragments.add(new HeadLineAppFragment());
//        fragments.add(new HeadLineGameFragment());

        headLineFragmentAdapter = new HeadLineFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(headLineFragmentAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    public class TabOnClickListener implements View.OnClickListener{
        private int index = INDEX_ZERO;
        public TabOnClickListener(int i){
            index=i;
        }
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);//选择某一页
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
            case INDEX_ZERO:
                mIdNews.setTextColor(res.getColor(R.color.white));
                mIdNews.setTextSize(FOCUS_SIZE);
                break;
            case INDEX_ONE:
                mIdVideo.setTextColor(res.getColor(R.color.white));
                mIdVideo.setTextSize(FOCUS_SIZE);
                break;
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

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}