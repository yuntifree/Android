package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.Utility;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 *  无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView,AdapterView.OnItemClickListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;

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
        mMainNewsLv = findView(view,R.id.main_news_lv);
    }

    public void initData() {
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE,HEAD_LINE_SEQ);
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        if (newsVo != null){
            newsList = newsVo.getData().getInfos();
        }
        headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(),newsList,true);
        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_for_main_news,null);
        mMainNewsLv.addFooterView(footView);
        mMainNewsLv.setAdapter(headLineNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);
        mMainNewsLv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       ToastUtil.showMiddle(getActivity(),""+position);
    }
}
