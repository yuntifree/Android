package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.ScanCodeActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SpeedtestActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiManagerActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiMapActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.adapter.NetworkImageHolderView;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.MyScrollView;
import com.yunxingzh.wireless.mvp.ui.utils.Utility;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wirelesslibs.convenientbanner.ConvenientBanner;
import com.yunxingzh.wirelesslibs.convenientbanner.holder.CBViewHolderCreator;
import com.yunxingzh.wirelesslibs.convenientbanner.listener.OnItemClickListener;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView, AdapterView.OnItemClickListener, View.OnClickListener, ScrollViewListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int PULL_HEIGHT = 10;
    private final static int HEIGHT = 0;
    private final static int ITEM = 0;
    private final static int NEWS = 1;//新闻点击上报
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private LinearLayout mNoticeLay, mMainWifiManager, mMainMapLay, mMainSpeedtest;
    private MyScrollView scrollView;
    private TextView mTitleLeftContent, mNoticeTv, mConnectTv, mCircleSecondTv, mCircleThreeTv, mConnectCountTv, mEconomizeTv;
    private ImageView mTitleReturnIv, mShowMoreIv, mTitleRightIv;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;
    private AnimationSet alphaAnimation;

    private View footView;
    private List<NewsVo.Data.NewsData> newsList;

    private FontInfoVo.FontData.BannersVo bannersVo;
    private FontInfoVo.FontData.UserVo userVo;

    private ConvenientBanner mAdRotationBanner;
    private OnJumpListener onJumpListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mTitleRightIv = findView(view, R.id.title_right_iv);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mTitleRightIv.setOnClickListener(this);
        scrollView = findView(view, R.id.scrollView);
        scrollView.setScrollViewListener(this);
        mTitleReturnIv = findView(view, R.id.title_return_iv);
        mTitleReturnIv.setVisibility(View.GONE);
        mTitleLeftContent = findView(view, R.id.title_left_content);
        mTitleLeftContent.setVisibility(View.VISIBLE);
        mMainNewsLv = findView(view, R.id.main_news_lv);
        mNoticeTv = findView(view, R.id.notice_tv);
        mNoticeLay = findView(view, R.id.notice_lay);
        mShowMoreIv = findView(view, R.id.show_more_iv);
        mConnectTv = findView(view, R.id.connect_tv);
        mConnectTv.setOnClickListener(this);
        mCircleSecondTv = findView(view, R.id.circle_second_tv);
        mCircleThreeTv = findView(view, R.id.circle_three_tv);
        mMainWifiManager = findView(view, R.id.main_wifi_manager);
        mMainWifiManager.setOnClickListener(this);
        mMainMapLay = findView(view, R.id.main_map_lay);
        mMainMapLay.setOnClickListener(this);
        mAdRotationBanner = findView(view, R.id.banner_img);
        mConnectCountTv = findView(view, R.id.connect_count_tv);
        mEconomizeTv = findView(view, R.id.economize_tv);
        mMainSpeedtest = findView(view, R.id.main_speedtest_lay);
        mMainSpeedtest.setOnClickListener(this);

        alphaAnimation = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        mCircleSecondTv.startAnimation(alphaAnimation);
        mCircleThreeTv.startAnimation(alphaAnimation);
    }

    public void initData() {
        fragmentManager = getFragmentManager();
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        mAdRotationBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        if (newsVo != null) {
            newsList = newsVo.getData().getInfos();
            mNoticeTv.setText(newsList.get(ITEM).getTitle());
        }
        headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsList, true);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_for_main_news, null);
        mMainNewsLv.addFooterView(footView);
        footView.setOnClickListener(this);
        mMainNewsLv.setAdapter(headLineNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);
        mMainNewsLv.setOnItemClickListener(this);

        iHeadLinePresenter.weatherNews();
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
        weatherNewsVo.getData().getWeather();

        iHeadLinePresenter.getFontInfo();
    }

    @Override
    public void getFontInfoSuccess(FontInfoVo fontInfoVo) {
        bannersVo = fontInfoVo.getData().getBanner();
        userVo = fontInfoVo.getData().getUser();
        mConnectCountTv.setText(userVo.getTotal() + "");
        mEconomizeTv.setText(userVo.getSave() + "");

        if (bannersVo != null) {
            List<String> imageList = new ArrayList<String>();
            imageList.add(bannersVo.getImg());
            mAdRotationBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, imageList);
        }
        //banner图跳转
        mAdRotationBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!StringUtils.isEmpty(bannersVo.getImg())) {
                    startActivity(WebViewActivity.class, Constants.URL, bannersVo.getDst(), "", "");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (newsList.get(position).getImages().size() == NEWS) {//图片size=1表示点击的是广告
            iHeadLinePresenter.clickCount(newsList.get(position).getId(), NEWS);
        }
        iHeadLinePresenter.clickCount(newsList.get(position).getId(), NEWS);
        startActivity(WebViewActivity.class, Constants.URL, newsList.get(position).getDst(), Constants.TITLE, newsList.get(position).getTitle());
    }

    @Override
    public void onClick(View v) {
        if (mConnectTv == v) {//一键连接

        } else if (footView == v) {//查看更多新闻
            onJumpListener.onJump();//利用接口回调-fragment间的跳转
        } else if (mMainWifiManager == v) {//wifi管理
            startActivity(WifiManagerActivity.class, "", "", "", "");
        } else if (mMainMapLay == v) {//wifi地图
            startActivity(WifiMapActivity.class, "", "", "", "");
        } else if (mTitleRightIv == v) {//扫码连接东莞wifi
            Intent intent = new Intent();
            intent.setClass(getActivity(), ScanCodeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        } else if (mMainSpeedtest == v) { // wifi 测速
            startActivity(SpeedtestActivity.class, "", "", "", "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String s =bundle.getString("result");
                    System.out.print(s);
                    //显示扫描到的内容
                    // mTextView.setText(bundle.getString("result"));
                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y > PULL_HEIGHT) { //下拉高度大于10
            mNoticeLay.setVisibility(View.GONE);
            mShowMoreIv.setVisibility(View.INVISIBLE);
        } else if (y == HEIGHT) {
            mNoticeLay.setVisibility(View.VISIBLE);
            mNoticeTv.setText(R.string.head_line);
            mShowMoreIv.setVisibility(View.VISIBLE);
        }
    }

    public void startActivity(Class activity, String key, String videoUrl, String titleKey, String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdRotationBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdRotationBanner.startTurning(1500);
    }

    //接口回调-fragment间的跳转
    public void SetJumpListener(OnJumpListener onJump){
        this.onJumpListener = onJump;
    }

    public interface OnJumpListener{
        void onJump();
    }
}
