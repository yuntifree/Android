package com.yunxingzh.wireless.mvp.presenter.impl;

import com.yunxingzh.wireless.mvp.presenter.IFeedBackPresenter;
import com.yunxingzh.wireless.mvp.view.IFeedBackView;

import wireless.libs.model.IFeedBackModel;
import wireless.libs.model.impl.FeedBackModelImpl;

/**
 * Created by stephen on 2016/12/23.
 */

public class FeedBackPresenterImpl implements IFeedBackPresenter, IFeedBackModel.onFeedBackListener {

    private IFeedBackModel iFeedBackModel;
    private IFeedBackView iFeedBackView;

    public FeedBackPresenterImpl(IFeedBackView view) {
        iFeedBackView = view;
        iFeedBackModel = new FeedBackModelImpl();
    }

    @Override
    public void feedBack(String content, String contact) {
        if (iFeedBackView != null){
            iFeedBackModel.feedBack(content,contact,this);
        }
    }

    @Override
    public void onFeedBackSuccess() {
        if (iFeedBackView != null){
            iFeedBackView.feedBackSuccess();
        }
    }

    @Override
    public void onDestroy() {
        iFeedBackView = null;
    }
}
