package com.yunxingzh.wireless.mvp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class BView extends View {

    public static int density_width;            //屏幕宽度
    public static int density_height;            //屏幕高度            
    public static float density = 0;                //屏幕像素精度
    
    public BView(Context context) {
        super(context);
        init();
    }
    
    public BView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init(){
        if(density == 0){
            DisplayMetrics dm = getResources().getDisplayMetrics();
            density = dm.density;
            density_width = dm.widthPixels;
            density_height = dm.heightPixels;
        }
    }
    
    public int px(int dp){
        return (int)(dp * density + 0.5f);
    }

}
