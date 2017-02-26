package com.yunxingzh.wireless.mvp.view;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/26.
 */

public interface IGetJokesView extends IBaseView {
    void getJokesSuccess(JokeList jokeList);
    void getJokesFaild();
}
