package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wireless.utility.Logg;

/**
 * Created by stephon on 2016/11/11.
 * 自定义scrollView：设置滚动监听事件
 */

public class MyScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    private Runnable scrollerTask;
    private int initialPosition;

    private int newCheck = 100;
    private static final String TAG = "MyScrollView";

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        scrollerTask = new Runnable() {

            public void run() {
            int newPosition = getScrollY();
            if (initialPosition - newPosition == 0){//has stopped
                if (onScrollStoppedListener != null) {
                    onScrollStoppedListener.onScrollStopped();
                }
            } else {
                initialPosition = getScrollY();
                MyScrollView.this.postDelayed(scrollerTask, newCheck);
            }
            }
        };
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnScrollStoppedListener{
        void onScrollStopped();
    }

    private OnScrollStoppedListener onScrollStoppedListener;

    public void setOnScrollStoppedListener(MyScrollView.OnScrollStoppedListener listener){
        onScrollStoppedListener = listener;
    }

    public void startScrollerTask(){
        initialPosition = getScrollY();
        MyScrollView.this.postDelayed(scrollerTask, newCheck);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * 滑动事件 控制滑动速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }
}
