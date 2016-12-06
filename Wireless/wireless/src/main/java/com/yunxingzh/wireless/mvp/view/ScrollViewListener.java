package com.yunxingzh.wireless.mvp.view;

import com.yunxingzh.wireless.mview.MyScrollView;

/**
 * Created by stephon on 2016/11/11.
 */

public interface ScrollViewListener  {
    void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
}
