package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.Utility;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView, AdapterView.OnItemClickListener,View.OnClickListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextView mTitleLeftContent;
    private ImageView mTitleReturnIv;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;

    private View footView;
    private List<NewsVo.Data.NewsData> newsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mTitleReturnIv = findView(view, R.id.title_return_iv);
        mTitleReturnIv.setVisibility(View.GONE);
        mTitleLeftContent = findView(view, R.id.title_left_content);
        mTitleLeftContent.setVisibility(View.VISIBLE);
        mMainNewsLv = findView(view, R.id.main_news_lv);
    }

    public void initData() {
        fragmentManager = getFragmentManager();

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        if (newsVo != null) {
            newsList = newsVo.getData().getInfos();
        }
        headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsList, true);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_for_main_news, null);
        mMainNewsLv.addFooterView(footView);
        footView.setOnClickListener(this);
        mMainNewsLv.setAdapter(headLineNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);
        mMainNewsLv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(WebViewActivity.class, Constants.URL,newsList.get(position).getDst(),Constants.TITLE,newsList.get(position).getTitle());
    }

    public void startActivity(Class activity,String key,String videoUrl,String titleKey,String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (footView == v){
//            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
//                    .replace(R.id.main_fragment_parent, new HeadLineFragment());
//            fragmentTransaction.commit();
        }
    }
}
