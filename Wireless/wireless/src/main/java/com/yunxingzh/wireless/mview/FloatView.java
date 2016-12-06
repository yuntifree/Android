package com.yunxingzh.wireless.mview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by carey on 2016/6/4 0004.
 * 可拖动按钮
 * floatView = new FloatView(this); // 创建窗体
 * floatView.setOnClickListener(this); //设置事件，你需要实现FloatView里的onclick接口
 * floatView.show(); // 显示该窗体
 * floatView.hide(); // 隐藏窗体
 */
public class FloatView extends ImageView {
    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private int startX;
    private int startY;
    private int controlledSpace = 20;
    private int screenWidth;
    private int screenHeight;
    boolean isShow = false;
    private OnClickListener mClickListener;

    private WindowManager windowManager;

    private WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FloatView(Context context) {
        super(context);
        initView(context);
    }

    // 初始化view
    private void initView(Context context) {
        windowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        windowManagerParams.format = PixelFormat.RGBA_8888; // 背景透明
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗口至左上角，便于调整坐标
        windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        windowManagerParams.x = 0;
        windowManagerParams.y = screenHeight >> 1;
        // 设置悬浮窗口长宽数据
        windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x = event.getRawX();
        y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchX = event.getX();
                mTouchY = event.getY();
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;

            }
            case MotionEvent.ACTION_MOVE: {
                updateViewPosition();
                break;
            }
            case MotionEvent.ACTION_UP: {

                if (Math.abs(x - startX) < controlledSpace
                        && Math.abs(y - startY) < controlledSpace) {
                    if (mClickListener != null) {
                        mClickListener.onClick(this);
                    }
                }
                if (x <= screenWidth / 2) {
                    x = 0;
                } else {
                    x = screenWidth;
                }

                updateViewPosition();

                break;
            }
        }

        return super.onTouchEvent(event);
    }

    // 隐藏该窗体
    public void hide() {
        if (isShow) {
            windowManager.removeView(this);
            isShow = false;
        }

    }

    // 显示该窗体
    public void show() {
        if (isShow == false) {
            windowManager.addView(this, windowManagerParams);
            isShow = true;
        }

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mClickListener = l;
    }

    private void updateViewPosition() {
        // 更新浮动窗口位置参数
        windowManagerParams.x = (int) (x - mTouchX);
        windowManagerParams.y = (int) (y - mTouchY);
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }
}
